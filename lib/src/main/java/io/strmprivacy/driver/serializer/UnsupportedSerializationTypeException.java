package io.strmprivacy.driver.serializer;

public class UnsupportedSerializationTypeException extends IllegalArgumentException {
    public UnsupportedSerializationTypeException(String message) {
        super(message);
    }
}
