package es.molabs.bocli.command;

import com.eclipsesource.json.Json;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.io.IOException;
import java.util.Objects;

public class DeleteNoteCommand extends WebClientCommand {

    private static final String PATH_CREATOR_NOTE = "/api/creator/note";

    private final int id;

    public DeleteNoteCommand(Output output, WebClient webClient, String host, int id) {
        super(output, webClient, host);

        this.id = id;
    }

    @Override
    public void execute() {
        getOutput()
            .printLine(
                call(() ->
                    getWebClient()
                        .delete(
                            getHost() + PATH_CREATOR_NOTE,
                            Integer.toString(id)
                        )
                )
            );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeleteNoteCommand that = (DeleteNoteCommand) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }
}
