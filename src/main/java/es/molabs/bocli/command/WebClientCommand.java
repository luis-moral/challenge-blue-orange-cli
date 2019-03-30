package es.molabs.bocli.command;

import es.molabs.bocli.client.InvalidResponseException;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public abstract class WebClientCommand implements Command {

    private final Output output;
    private final WebClient webClient;
    private final String host;

    public WebClientCommand(Output output, WebClient webClient, String host) {
        this.output = output;
        this.webClient = webClient;
        this.host = host;
    }

    public Output getOutput() {
        return output;
    }

    public WebClient getWebClient() {
        return webClient;
    }

    public String getHost() {
        return host;
    }

    protected String call(WebClientCall call) {
        String message;

        try {
            message = call.process(getWebClient());
        }
        catch (IOException IOe) {
            message = formatException(IOe);
        }

        return message;
    }

    private String formatException(IOException exception) {
        String message;

        if (exception instanceof FileNotFoundException) {
            message = "Error connecting: " + exception.getMessage();
        }
        else if (exception instanceof InvalidResponseException) {
            message = exception.getMessage();
        }
        else {
            message = exception.getClass().getSimpleName() + ": " + exception.getMessage();
        }

        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebClientCommand that = (WebClientCommand) o;
        return Objects.equals(output, that.output) &&
            Objects.equals(webClient, that.webClient) &&
            Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, webClient, host);
    }

    @FunctionalInterface
    public interface WebClientCall {

        String process(WebClient webClient) throws IOException;
    }
}
