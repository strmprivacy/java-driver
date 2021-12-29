package io.strmprivacy.driver.serializer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializer implements EventSerializer {

    private DatumWriter<GenericRecord> writer;

    /**
     * construct an instance with a reader and a writer schema
     *
     * @param writerSchema the Avro writer schema
     */
    public AvroSerializer(Schema writerSchema) {
        //    private static final Logger log = LoggerFactory.getLogger(AvroSerializer.class);
        this.writer = new SpecificDatumWriter<>(writerSchema);
    }


    /**
     * serialize a GenericRecord or something compatible (like DemoUserV1 in the tests)
     *
     * @param event             the avro record
     * @return a bytes representation of the record contents.
     * @throws IllegalStateException problem with the Avro serialization.
     */
    public byte[] serialize(Object event) throws IllegalStateException, IOException {
        if (!(event instanceof GenericRecord)) {
            throw new IllegalArgumentException("Event is not of type GenericRecord");
        }
        GenericRecord msg = (GenericRecord) event;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
        writer.write(msg, encoder);
        encoder.flush();
        return baos.toByteArray();
    }

    public void tearDown() {
        this.writer = null;
    }
}
