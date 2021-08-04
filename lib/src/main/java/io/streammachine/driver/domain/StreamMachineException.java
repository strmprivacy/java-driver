package io.streammachine.driver.domain;

public class StreamMachineException extends RuntimeException {

    public StreamMachineException(String message, Throwable cause) {
        super(message, cause);
    }

    public StreamMachineException(Throwable cause) {
        super(cause);
    }
}
