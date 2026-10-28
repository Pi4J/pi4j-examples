package com.pi4j.examples.utils.commandhandler;

/** A parameter specification that can be used to process a string value into a specific type. */
public abstract class ParameterSpecification<PT> {

    /**
     * Processes a string value into a specific type. If the value is not valid, an IllegalArgumentException is
     * thrown. In this case, the UI will inform the user about the problem, rendering the exception message.
     */
    abstract PT process(String value);
}
