package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.util.Objects;

public class DeleteNoteCommand extends WebClientCommand {

    public static final String PATH_CREATOR_NOTE = "/api/v1/public/creators-notes";

    private final int id;

    public DeleteNoteCommand(Output output, WebClient webClient, String host, int id) {
        super(output, webClient, host);

        this.id = id;
    }

    @Override
    public void execute() {
        getOutput()
            .printLine(
                call(client -> client
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
