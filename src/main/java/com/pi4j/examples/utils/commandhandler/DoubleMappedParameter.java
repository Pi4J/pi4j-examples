package com.pi4j.examples.utils.commandhandler;

public class DoubleMappedParameter extends ParameterSpecification<Double>{}{} {


    // Define the data structure
    public record Rotation(String name, double rpm) {}

    Rotation[] ranges;


    public DoubleMappedParameter(Rotation[] input) {
        ranges = input;
    }

    @Override
    Double process(String text) {
        double rotation = 90.0;
        for(Rotation ranges : ranges){
            if (text.equalsIgnoreCase(ranges.name)){
                return ranges.rpm;
            }
        }
        throw new IllegalArgumentException("Value " + text);
    }

@Override
public String toString() {
        String options = "";
        for(Rotation ranges : ranges){
            options+= ranges.name;
        }

            return "Permissible options [" + options + "] ";
    }
}
