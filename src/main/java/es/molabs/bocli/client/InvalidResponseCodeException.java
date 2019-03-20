package es.molabs.bocli.client;

import java.io.IOException;

public class InvalidResponseCodeException extends IOException {

    public InvalidResponseCodeException(int responseCode) {
        super("Invalid Response Code: " + responseCode);
    }
}
