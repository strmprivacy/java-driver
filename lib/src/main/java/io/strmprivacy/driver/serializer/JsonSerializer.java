package io.strmprivacy.driver.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonSerializer implements EventSerializer {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final JsonNode schema;

    public JsonSerializer(JsonNode schema) {
        this.schema = schema;
    }

    public byte[] serialize(Object event, SerializationType serializationType) {
        JsonNode node = MAPPER.convertValue(event, JsonNode.class);
        JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
        try {
            final ProcessingReport report = jsonValidator.validate(schema, node);
            if (report.isSuccess()) {
                return MAPPER.writeValueAsBytes(event);
            } else {
                IllegalArgumentException validationException = new IllegalArgumentException("Provided JSON event does not match with schema");
                report.iterator().forEachRemaining(message -> validationException.addSuppressed(message.asException()));

                throw validationException;
            }

        } catch (JsonProcessingException e) {
            log.error("Error while converting provided event to bytes", e);
        } catch (ProcessingException e) {
            log.error("Error while validating provided event against schema", e);
        }

        return new byte[0];
    }
}
