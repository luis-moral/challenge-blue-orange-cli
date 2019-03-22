package es.molabs.bocli.command;

import com.eclipsesource.json.Json;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.util.Objects;

public class EditNoteCommand extends WebClientCommand {

    public static final String PATH_CREATOR_NOTE = "/api/v1/public/creators-notes";

    private final int id;
    private final String text;

    public EditNoteCommand(Output output, WebClient webClient, String host, int id, String text) {
        super(output, webClient, host);

        this.id = id;
        this.text = text;
    }

    @Override
    public void execute() {
        getOutput()
            .printLine(
                call(() ->
                    getWebClient()
                        .put(
                            getHost() + PATH_CREATOR_NOTE ,
                            Integer.toString(id),
                            Json
                                .object()
                                .add("text", text)
                                .toString()
                        )
                )
            );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EditNoteCommand that = (EditNoteCommand) o;
        return id == that.id &&
            Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, text);
    }
}
