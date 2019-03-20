package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.util.Objects;

public class DeleteNoteCommand implements Command{
    private final Output output;
    private final WebClient webClient;
    private final String host;
    private final String id;

    public DeleteNoteCommand(Output output, WebClient webClient, String host, String id) {
        this.output = output;
        this.webClient = webClient;
        this.host = host;
        this.id = id;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteNoteCommand that = (DeleteNoteCommand) o;
        return Objects.equals(output, that.output) &&
            Objects.equals(webClient, that.webClient) &&
            Objects.equals(host, that.host) &&
            Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, webClient, host, id);
    }
}
