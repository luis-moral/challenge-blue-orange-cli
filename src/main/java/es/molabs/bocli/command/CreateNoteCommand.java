package es.molabs.bocli.command;

import com.eclipsesource.json.Json;
import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.util.Objects;

public class CreateNoteCommand extends WebClientCommand {

    public static final String PATH_CREATOR_NOTE = "/api/v1/public/creators-notes";

    private final int creatorId;
    private final String text;

    public CreateNoteCommand(Output output, WebClient webClient, String host, int creatorId, String text) {
        super(output, webClient, host);

        this.creatorId = creatorId;
        this.text = text;
    }

    @Override
    public void execute() {
        getOutput()
            .printLine(
                call(() -> getWebClient()
                    .post(
                        getHost() + PATH_CREATOR_NOTE,
                        Json
                            .object()
                            .add("creatorId", creatorId)
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
        CreateNoteCommand that = (CreateNoteCommand) o;
        return creatorId == that.creatorId &&
            Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), creatorId, text);
    }
}
