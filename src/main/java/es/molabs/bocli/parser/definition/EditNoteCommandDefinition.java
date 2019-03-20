package es.molabs.bocli.parser.definition;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.EditNoteCommand;
import es.molabs.bocli.ouput.Output;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class EditNoteCommandDefinition extends ConsoleCommandDefinition<EditNoteCommand> {

    private static final String ARGUMENT_CREATOR_ID = "id";
    private static final String ARGUMENT_NOTE = "note";

    public EditNoteCommandDefinition() {
        super(
            "edit_note",
            false,
            "-" + ARGUMENT_CREATOR_ID + " id -" + ARGUMENT_NOTE + " text",
            "Edits a creator's custom note",
            Option.builder(ARGUMENT_CREATOR_ID).required().hasArg().desc("Note id").build(),
            Option.builder(ARGUMENT_NOTE).required().hasArg().desc("Text of the note").build()
        );
    }

    @Override
    public EditNoteCommand parse(Output output, WebClient webClient, String host, CommandLineParser parser, String[] args) throws ParseException {
        CommandLine line = parser.parse(options(), args);

        return
            new EditNoteCommand(
                output,
                webClient,
                host,
                Integer.parseInt(line.getOptionValue(ARGUMENT_CREATOR_ID)),
                line.getOptionValue(ARGUMENT_NOTE)
            );
    }
}
