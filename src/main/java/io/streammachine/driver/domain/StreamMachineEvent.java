package io.streammachine.driver.domain;

import java.io.IOException;

public interface StreamMachineEvent {
    String getStrmSchemaId();

    Object getStrmSchema() throws IOException;
}
