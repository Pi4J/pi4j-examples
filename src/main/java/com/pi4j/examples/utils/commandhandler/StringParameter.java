package com.pi4j.examples.utils.commandhandler;

import java.util.Arrays;
import java.util.List;

/** A string parameter that can take any value or a specific set of values. */
public class StringParameter  extends ParameterSpecification<String> {
    private List<String> permittedValues;

    public StringParameter(String... permittedValues) {
        this.permittedValues = Arrays.asList(permittedValues);
    }

    @Override
    String process(String value) {
        if (permittedValues.isEmpty() || permittedValues.contains(value)) {
            return value;
        }
        throw new IllegalArgumentException("Invalid value: " + value + "; permitted values: " + permittedValues);
    }

    @Override
    public String toString() {
        return permittedValues.isEmpty() ? "(string)" : permittedValues.toString();
    }
}
