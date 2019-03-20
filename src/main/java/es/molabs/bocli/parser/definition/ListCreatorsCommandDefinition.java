package es.molabs.bocli.parser.definition;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.ListCreatorsCommand;
import es.molabs.bocli.ouput.Output;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

public class ListCreatorsCommandDefinition extends ConsoleCommandDefinition<ListCreatorsCommand> {

    private static final String ARGUMENT_FILTER = "filter";
    private static final String ARGUMENT_SORT = "sort";

    public ListCreatorsCommandDefinition() {
        super(
            "list",
            "",
            "Lists creators",
            Option.builder(ARGUMENT_FILTER).desc("Field to filter").numberOfArgs(2).valueSeparator('=').build(),
            Option.builder(ARGUMENT_SORT).hasArg().desc("Field to sort").build()
        );
    }

    @Override
    public ListCreatorsCommand parse(Output output, WebClient webClient, String host, CommandLineParser parser, String[] args) throws ParseException {
        CommandLine line = parser.parse(options(), args);
        String[] filter = line.getOptionValues(ARGUMENT_FILTER);

        return
            new ListCreatorsCommand(
                output,
                webClient,
                host,
                filter != null ? filter[0] : null,
                filter != null ? filter[1] : null,
                line.getOptionValue(ARGUMENT_SORT)
            );
    }
}
