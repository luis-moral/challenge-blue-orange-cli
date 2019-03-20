package es.molabs.bocli.parser.definition;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.DeleteNoteCommand;
import es.molabs.bocli.ouput.Output;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class DeleteNoteCommandDefinition extends ConsoleCommandDefinition<DeleteNoteCommand> {

    private static final String ARGUMENT_CREATOR_ID = "id";

    public DeleteNoteCommandDefinition() {
        super(
            "delete_note",
            false,
            "-" + ARGUMENT_CREATOR_ID + " id",
            "Deletes a creator's custom note",
            Option.builder(ARGUMENT_CREATOR_ID).required().hasArg().desc("Note id").build()
        );
    }

    @Override
    public DeleteNoteCommand parse(Output output, WebClient webClient, String host, CommandLineParser parser, String[] args) throws ParseException {
        CommandLine line = parser.parse(options(), args);

        return
            new DeleteNoteCommand(
                output,
                webClient,
                host,
                Integer.parseInt(line.getOptionValue(ARGUMENT_CREATOR_ID))
            );
    }
}
