package es.molabs.bocli.client;

import com.eclipsesource.json.Json;

import java.io.IOException;

public class InvalidResponseException extends IOException {

    private static String readMessage(String body) {
        String message;

        try {
            message =
                Json
                    .parse(body)
                    .asObject()
                    .get("message")
                    .asString();
        }
        catch (Exception e) {
            message = "";
        }

        return message;
    }

    public InvalidResponseException(int responseCode, String body) {
        super("Invalid Response [status=" + responseCode + ",message=" + readMessage(body)+"]");
    }
}
