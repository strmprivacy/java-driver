package io.streammachine.driver.serializer;

public class UnsupportedSerializationTypeException extends IllegalArgumentException {
    public UnsupportedSerializationTypeException(String message) {
        super(message);
    }
}
