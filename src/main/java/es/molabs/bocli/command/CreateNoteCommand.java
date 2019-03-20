package es.molabs.bocli.command;

import com.eclipsesource.json.Json;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.io.IOException;
import java.util.Objects;

public class CreateNoteCommand implements Command {

    private static final String PATH_CREATOR_NOTE = "/api/creator/note";

    private final Output output;
    private final WebClient webClient;
    private final String host;
    private final int creatorId;
    private final String note;

    public CreateNoteCommand(Output output, WebClient webClient, String host, int creatorId, String note) {
        this.output = output;
        this.webClient = webClient;
        this.host = host;
        this.creatorId = creatorId;
        this.note = note;
    }

    @Override
    public void execute() {
        String message;

        try {
            message =
                webClient
                    .post(
                        host + PATH_CREATOR_NOTE,
                        Json
                            .object()
                            .add("creatorId", creatorId)
                            .add("note", note)
                            .toString()
                    );
        }
        catch (IOException IOe) {
            message = IOe.getMessage();
        }

        output.printLine(message);
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
