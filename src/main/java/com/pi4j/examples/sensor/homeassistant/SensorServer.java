package com.pi4j.examples.sensor.homeassistant;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.drivers.display.character.CharacterDisplay;
import com.pi4j.drivers.sensor.SensorDescriptor;
import com.pi4j.examples.sensor.display.SensorAggregator;

import java.io.*;
import java.util.*;

public class SensorServer {
    private static final int DEFAULT_I2C_BUS = 1;
    private static final int DEFAULT_HTTP_PORT = 8147;
    private static final int UPDATE_INTERVAL_MS = 5_000;

    private final SensorAggregator sensorAggregator;
    private final int httpPort;
    private final Object lock = new Object();

    private Map<SensorDescriptor.Kind, Double> currentReadings;
    private String address;

    public SensorServer(SensorAggregator sensorAggregator, int httpPort) {
        this.sensorAggregator = sensorAggregator;
        this.httpPort = httpPort;

        System.out.println("Sensors found:");
        System.out.println(sensorAggregator.getSensorList());

        if (sensorAggregator.getSensorList().isEmpty()) {
            throw new IllegalStateException("No sensors found.");
        }

        System.out.println("Reading initial sensor values...");
        currentReadings = sensorAggregator.readAveragedMeasurements();
        System.out.println("Initial sensor values aquired: " + currentReadings);
        System.out.println();
    }

    public SensorServer(Context context, int bus, CharacterDisplay display, int httpPort) {
        this(new SensorAggregator(context, bus, display), httpPort);
    }

    void run() {
        try {
            TinyWebServer webServer = new TinyWebServer(httpPort);
            this.address = webServer.getUrl();

            webServer.addRequestHandler("config", "text/vnd.yaml", this::renderConfig);
            webServer.addRequestHandler("measurements", "application/json", this::renderMeasurements);
            webServer.addRequestHandler("", "text/plain", this::renderHelp);

            new Thread(webServer).start();

            System.out.println("Webserver started at " + webServer.getUrl());
            System.out.println();

            renderHelp(new PrintWriter(System.out, true));
            System.out.println();

            while (true) {
                Map<SensorDescriptor.Kind, Double> newReadings = sensorAggregator.readAveragedMeasurements();
                synchronized (lock) {
                    currentReadings = newReadings;
                }
                sensorAggregator.displayAveragedMeasurements(newReadings);
                Thread.sleep(UPDATE_INTERVAL_MS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void renderMeasurements(PrintWriter out) {
        boolean first = true;
        out.print("{");
        synchronized (lock) {
            for (Map.Entry<SensorDescriptor.Kind, Double> entry: currentReadings.entrySet()) {
                out.print(first ? "\n  " : ",\n  ");
                first = false;
                out.print('"' + entry.getKey().toString() + "\": " + entry.getValue());
            }
        }
        out.print("\n}\n");
    }

    void renderConfig(PrintWriter out) {
        // https://www.home-assistant.io/integrations/sensor.rest/
        out.println("# Add this snippet to the 'rest:' section in your HomeAssistant config.yaml:");
        out.println("# rest:");
        out.println("  - resource: \"" + address +"/measurements\"");
        out.println("    sensor:\n");
        synchronized (lock) {
            for (SensorDescriptor.Kind kind: currentReadings.keySet()) {
                String deviceClass = getHomeAssistantDeviceClass(kind);
                if (deviceClass != null) {
                    out.println("      - name: \"" + kind + '"');
                    out.println("        value_template: \"{{ value_json['" + kind + "'] }}\"");
                    out.println("        device_class: " + deviceClass);
                    out.println("        unit_of_measurement: \"" + kind.measurementUnit + '"');
                }
            }
        }
    }

    void renderHelp(PrintWriter out) {
        out.println("Pi4J I2C Autodetect Sensor Server");
        out.println();
        out.println("HomeAssistant YAML");
        out.println("configuration snippet: " + address + "/config");
        out.println("Measurements:          " + address + "/measurements\n");
        out.println("This help text:        " + address);
    }

    /** Returns the home assistant device class for the given sensor kind. */
    static String getHomeAssistantDeviceClass(SensorDescriptor.Kind kind) {
        return switch(kind) {
            case LIGHT, LIGHT_BLUE, LIGHT_RED, LIGHT_GREEN -> "illuminance";
            case CO2, DISTANCE, HUMIDITY, PRESSURE, TEMPERATURE -> kind.toString();
            default -> null;
        };
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Context pi4j = Pi4J.newAutoContext();
        SensorServer server = new SensorServer(pi4j, DEFAULT_I2C_BUS, null, DEFAULT_HTTP_PORT);
        server.run();
    }
}
