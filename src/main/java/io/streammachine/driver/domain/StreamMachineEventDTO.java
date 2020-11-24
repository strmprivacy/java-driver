package io.streammachine.driver.domain;

import io.streammachine.driver.serializer.EventSerializer;
import io.streammachine.driver.serializer.SerializationType;
import io.streammachine.driver.serializer.SerializerProvider;
import io.streammachine.driver.serializer.UnsupportedSerializationTypeException;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

@Getter
public class StreamMachineEventDTO {
    private final StreamMachineEvent event;
    private final SerializationType serializationType;

    @Builder
    public StreamMachineEventDTO(StreamMachineEvent event, SerializationType serializationType) {
        this.event = event;
        this.serializationType = serializationType;
    }

    public String getSchemaId() {
        return this.event.getStrmSchemaId();
    }

    public String getSerializationTypeHeader() {
        return switch (this.serializationType) {
            case JSON, AVRO_JSON -> "application/json";
            case AVRO_BINARY -> "application/x-avro-binary";
            default -> throw new UnsupportedSerializationTypeException("Unsupported Serialization Type '" + this.serializationType + "'.");
        };
    }

    public byte[] serialize() {
        try {
            final Object streamMachineSchema = event.getStrmSchema();
            final EventSerializer serializer = SerializerProvider.getSerializer(getSchemaId(), streamMachineSchema);
            return serializer.serialize(event, serializationType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
