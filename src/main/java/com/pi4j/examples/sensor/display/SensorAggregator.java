package com.pi4j.examples.sensor.display;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.drivers.display.character.CharacterDisplay;
import com.pi4j.drivers.sensor.Sensor;
import com.pi4j.drivers.sensor.SensorDescriptor;
import com.pi4j.drivers.sensor.SensorDetector;

import java.util.*;

/**
 * Sensor aggregation helper class, bundling a list of sensors and a character display.
 * It's able to aggregates sensor readings and display them on the given display.
 */
public class SensorAggregator {
    private static final int CONSOLE_WIDTH = 32;
    private static final int DEFAULT_I2C_BUS = 1;

    private final CharacterDisplay display;
    private final List<Sensor> sensorList;

    private int displayOffset;

    /**
     * Creates a SensorDisplay for the given sensor list and the given display. If display is null, we'll
     * render to the console.
     */
    public SensorAggregator(List<Sensor> sensorList, CharacterDisplay display) {
        this.display = display;
        this.sensorList = sensorList;
    }

    public List<Sensor> getSensorList() {
        return Collections.unmodifiableList(sensorList);
    }

    /**
     * Creates a SensorDisplay for the I2C sensors detected on the given bus and the given display.
     * If display is null, we'll render to the console.
     */
    public SensorAggregator(Context context, int bus, CharacterDisplay display) {
        this(SensorDetector.detectI2cSensors(context, bus), display);
    }

    /**
     * Reads measurements and returns them in a nested map grouped by type and sensor.
     * This (somewhat complex) format allows simple aggregation of sensor readings of the same type from
     * different sensors.
     */
    public Map<SensorDescriptor.Kind, Map<Sensor, Double>> readMeasurements() {
        Map<SensorDescriptor.Kind, Map<Sensor, Double>> results = new TreeMap<>();
        for (Sensor sensor: sensorList) {
            SensorDescriptor descriptor = sensor.getDescriptor();
            List<SensorDescriptor.Value> valueDescriptorList = descriptor.getValues();
            double[] readings = new double[valueDescriptorList.size()];
            sensor.readMeasurement(readings);
            for (SensorDescriptor.Value valueDescriptor: valueDescriptorList) {
                results.putIfAbsent(valueDescriptor.getKind(), new LinkedHashMap<>());
                Map<Sensor, Double> valueMap = results.get(valueDescriptor.getKind());
                valueMap.put(sensor, readings[valueDescriptor.getIndex()]);
            }
        }
        return results;
    }

    public Map<SensorDescriptor.Kind, Double> readAveragedMeasurements() {
        Map<SensorDescriptor.Kind, Map<Sensor, Double>> measurements = readMeasurements();
        Map<SensorDescriptor.Kind, Double> result = new TreeMap<>();
        for (Map.Entry<SensorDescriptor.Kind, Map<Sensor, Double>> entry: measurements.entrySet()) {
            double average = entry.getValue().values().stream()
                .mapToDouble(Number::doubleValue)
                .average()
                .getAsDouble();
            result.put(entry.getKey(), average);
        }
        return result;
    }

    /**
     * Renders the given averaged measurements on the display, keeping track of an offset if there
     * is not enough space on the display.
     */
    public Map<SensorDescriptor.Kind, Double> displayAveragedMeasurements(Map<SensorDescriptor.Kind, Double> measurements) {
        int count = measurements.size();
        int displayWidth = display == null ? CONSOLE_WIDTH : display.getWidth();
        int displayHeight = display == null ? Integer.MAX_VALUE : display.getHeight();

        int columnWidth = displayWidth;
        int columnCount = 1;
        if (count > displayHeight && displayWidth >= 16) {
            columnCount = 2;
            columnWidth = displayWidth / 2;
        }

        int row = -displayOffset;
        int column = 0;

        for (Map.Entry<SensorDescriptor.Kind,Double> entry: measurements.entrySet()) {
            String formatted = formatSensorValue(entry.getKey(), entry.getValue(), columnWidth);
            if (display == null) {
                System.out.println(formatted);
            } else {
                if (row >= 0 && row < display.getHeight()) {
                    display.writeAt(column, row, formatted);
                }
                column += columnWidth;
                if (column > displayWidth - columnWidth) {
                    column = 0;
                    row++;
                }
            }
        }
        if (display == null) {
            System.out.println();
        } else {
            displayOffset += display.getHeight() * columnCount;
            if (displayOffset >= count) {
                displayOffset = 0;
            }
        }
        return measurements;
    }

    /**
     * Convenience method combining reading and rendering sensor data.
     */
    public Map<SensorDescriptor.Kind, Double> readAndDisplayAveragedMeasurements() {
        return displayAveragedMeasurements(readAveragedMeasurements());
    }

    /**
     * Formats a sensor reading to a given number of columns.
     */
    public static String formatSensorValue(SensorDescriptor.Kind kind, double value, int columns) {
        String renderedUnit = kind.measurementUnit.toString();
        if (kind.measurementUnit == SensorDescriptor.MeasurementUnit.PASCAL) {
            renderedUnit = "hPa";
            value /= 100;
        }
        String renderedValue = String.valueOf(Math.round(value));
        if (columns < 12) {
            return String.format("%" + (columns - 3) + "s%-3s", renderedValue, renderedUnit);
        }
        int available = columns - renderedValue.length() - 6;
        String renderedLabel = Character.toUpperCase(kind.toString().charAt(0)) + kind.toString().substring(1);
        if (renderedLabel.length() > available) {
            renderedLabel = renderedLabel.substring(0, available - 1) + ".";
        }
        return String.format("%-" + (available + 1) + "s %s %-3s", (renderedLabel + ":"), renderedValue, renderedUnit);
    }

    /** Simple main to test aggregation and rendering using the console */
    public static void main(String[] args) throws InterruptedException {
        Context pi4j = Pi4J.newAutoContext();
        SensorAggregator sensorDisplay = new SensorAggregator(pi4j, DEFAULT_I2C_BUS, null);

        while (true) {
            sensorDisplay.readAndDisplayAveragedMeasurements();
            Thread.sleep(5_000);
        }
    }

 }
