package com.pi4j.examples.utils.commandhandler;

public class DoubleParameter extends ParameterSpecification<Double> {
    private final double min;
    private final double max;

    public DoubleParameter(double min, double max) {
        if( !Double.isFinite(min) ) {
            throw new IllegalArgumentException("Value " + min);
        }
        if( !Double.isFinite(max) ) {
            throw new IllegalArgumentException("Value " + max);
        }
        this.min = min;
        this.max = max;
    }

    @Override
    Double process(String text) {
        double parsed = Double.parseDouble(text);
        if (parsed < min || parsed > max) {
            throw new IllegalArgumentException("Value " + text);
        }
        return parsed;
    }

    public String toString() {
        return "[" + min + ".." + max + "] / [0x" + Double.toHexString(min) + "..0x" + Double.toHexString(max) + "]";
    }
}
