package io.strmprivacy.driver.domain;

import io.strmprivacy.driver.serializer.EventSerializer;
import io.strmprivacy.driver.serializer.SerializationType;
import io.strmprivacy.driver.serializer.SerializerProvider;
import io.strmprivacy.driver.serializer.UnsupportedSerializationTypeException;
import io.strmprivacy.schemas.StrmPrivacyEvent;

import java.io.IOException;

public class StrmPrivacyEventDTO {
    private final StrmPrivacyEvent event;
    private final SerializationType serializationType;

    public StrmPrivacyEventDTO(StrmPrivacyEvent event, SerializationType serializationType) {
        this.event = event;
        this.serializationType = serializationType;
    }

    public String getSchemaRef() {
        return this.event.getSchemaRef();
    }

    public String getSerializationTypeHeader() {
        switch (this.serializationType) {
            case JSON:
            case AVRO_JSON:
                return "application/json";
            case AVRO_BINARY:
                return "application/x-avro-binary";
            default:
                throw new UnsupportedSerializationTypeException("Unsupported Serialization Type '" + this.serializationType + "'.");
        }
    }

    public byte[] serialize() {
        try {
            final EventSerializer serializer = SerializerProvider.getSerializer(getSchemaRef(), event.getSchema());
            return serializer.serialize(event, serializationType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
