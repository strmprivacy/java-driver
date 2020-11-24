package io.streammachine.driver.serializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.JsonEncoder;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class AvroSerializer implements EventSerializer {
    private final Schema writerSchema;
    private DatumWriter<GenericRecord> writer;

    /**
     * construct an instance with a reader and a writer schema
     *
     * @param writerSchema the Avro writer schema
     */
    public AvroSerializer(Schema writerSchema) {
        this.writerSchema = writerSchema;
        this.writer = new SpecificDatumWriter<>(writerSchema);
    }


    /**
     * serialize a GenericRecord or something compatible (like DemoUserV1 in the tests)
     *
     * @param event             the avro record
     * @param serializationType
     * @return a bytes representation of the record contents.
     * @throws IllegalStateException problem with the Avro serialization.
     */
    public byte[] serialize(Object event, SerializationType serializationType) throws IllegalStateException, IOException {
        if (!(event instanceof GenericRecord)) {
            throw new IllegalArgumentException("Event is not of type GenericRecord");
        }
        GenericRecord msg = (GenericRecord) event;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        switch (serializationType) {
            case AVRO_BINARY: {
                BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
                writer.write(msg, encoder);
                encoder.flush();
            }
            break;
            case AVRO_JSON: {
                // JSON encoding of the object (a single record)
                GenericDatumWriter<GenericRecord> jsonWriter = new GenericDatumWriter<GenericRecord>(writerSchema);
                JsonEncoder encoder = EncoderFactory.get().jsonEncoder(writerSchema, baos);
                jsonWriter.write(msg, encoder);
                encoder.flush();
            }
        }
        baos.flush();
        return baos.toByteArray();
    }

    public void tearDown() {
        this.writer = null;
    }
}
