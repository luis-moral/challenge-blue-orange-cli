package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.util.Objects;

public class CreateNoteCommand implements Command{
    private final Output output;
    private final WebClient webClient;
    private final String host;
    private final String creatorId;
    private final String note;

    public CreateNoteCommand(Output output, WebClient webClient, String host, String creatorId, String note) {
        this.output = output;
        this.webClient = webClient;
        this.host = host;
        this.creatorId = creatorId;
        this.note = note;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateNoteCommand that = (CreateNoteCommand) o;
        return Objects.equals(output, that.output) &&
            Objects.equals(webClient, that.webClient) &&
            Objects.equals(host, that.host) &&
            Objects.equals(creatorId, that.creatorId) &&
            Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, webClient, host, creatorId, note);
    }
}
