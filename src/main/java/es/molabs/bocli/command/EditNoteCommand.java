package es.molabs.bocli.command;

import com.eclipsesource.json.Json;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.io.IOException;
import java.util.Objects;

public class EditNoteCommand implements Command {

    private static final String PATH_CREATOR_NOTE = "/api/creator/note";

    private final Output output;
    private final WebClient webClient;
    private final String host;
    private final int id;
    private final String note;

    public EditNoteCommand(Output output, WebClient webClient, String host, int id, String note) {
        this.output = output;
        this.webClient = webClient;
        this.host = host;
        this.id = id;
        this.note = note;
    }

    @Override
    public void execute() {
        String message;

        try {
            message =
                webClient
                    .post(
                        host + PATH_CREATOR_NOTE + "/" + id,
                        Json
                            .object()
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
        EditNoteCommand that = (EditNoteCommand) o;
        return Objects.equals(output, that.output) &&
            Objects.equals(webClient, that.webClient) &&
            Objects.equals(host, that.host) &&
            Objects.equals(id, that.id) &&
            Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, webClient, host, id, note);
    }
}
