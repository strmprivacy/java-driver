package io.strmprivacy.driver.domain;

public class StrmPrivacyException extends RuntimeException {

    public StrmPrivacyException(String message, Throwable cause) {
        super(message, cause);
    }

    public StrmPrivacyException(Throwable cause) {
        super(cause);
    }
}
