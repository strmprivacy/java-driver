package io.strmprivacy.driver.domain;

import io.strmprivacy.driver.serializer.EventSerializer;
import io.strmprivacy.driver.serializer.SerializationType;
import io.strmprivacy.driver.serializer.SerializerProvider;
import io.strmprivacy.driver.serializer.UnsupportedSerializationTypeException;
import io.strmprivacy.schemas.StrmEvent;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Getter
public class StrmPrivacyEventDTO {
    private final StrmEvent event;
    private final SerializationType serializationType;

    @Builder
    public StrmPrivacyEventDTO(StrmEvent event, SerializationType serializationType) {
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
            final Object strmEventSchema = event.getSchema();
            final EventSerializer serializer = SerializerProvider.getSerializer(getSchemaRef(), strmEventSchema);
            return serializer.serialize(event, serializationType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
