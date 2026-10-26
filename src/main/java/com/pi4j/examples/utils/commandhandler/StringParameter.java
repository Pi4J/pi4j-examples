/*
 *  /*
 *  *
 *
 */

package com.pi4j.examples.utils.commandhandler;

import java.util.Arrays;

public class StringParameter extends ParameterSpecification<String> {

    String[] allowedValues = {"s", "m", "f"};

    /** If empty, we accept any string */
    public StringParameter(String... allowedValues) {
        this.allowedValues = allowedValues;
    }

    @Override
    String process(String value) {
        if (Arrays.asList(allowedValues).contains(value)) {
            return value;
        } else {
            throw new IllegalArgumentException("Value " + value);
        }
    }

    @Override
    public String toString() {
        String options = "";
        for(String allowedValues  : allowedValues){
            options+= allowedValues;
        }
        return "Permissible options [" + options + "] ";
    }
}