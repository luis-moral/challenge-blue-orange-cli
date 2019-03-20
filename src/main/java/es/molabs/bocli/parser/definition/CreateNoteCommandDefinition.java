package es.molabs.bocli.parser.definition;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.CreateNoteCommand;
import es.molabs.bocli.ouput.Output;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class CreateNoteCommandDefinition extends ConsoleCommandDefinition<CreateNoteCommand> {

    private static final String ARGUMENT_CREATOR_ID = "creatorId";
    private static final String ARGUMENT_NOTE = "note";

    public CreateNoteCommandDefinition() {
        super(
            Option.builder(ARGUMENT_CREATOR_ID).required().hasArg().desc("Creator id").build(),
            Option.builder(ARGUMENT_NOTE).required().hasArg().desc("Text of the custom note").build()
        );
    }

    @Override
    public CreateNoteCommand parse(Output output, WebClient webClient, String host, CommandLineParser parser, String[] args) throws ParseException {
        CommandLine line = parser.parse(options(), args);

        return
            new CreateNoteCommand(
                output,
                webClient,
                host,
                line.getOptionValue(ARGUMENT_CREATOR_ID),
                line.getOptionValue(ARGUMENT_NOTE));
    }
}
