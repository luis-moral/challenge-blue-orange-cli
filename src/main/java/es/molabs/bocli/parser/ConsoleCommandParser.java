package es.molabs.bocli.parser;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.Command;
import es.molabs.bocli.command.ErrorParsingCommand;
import es.molabs.bocli.command.ListCreatorsCommand;
import es.molabs.bocli.command.ShowHelpCommand;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.definition.CreateNoteCommandDefinition;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class ConsoleCommandParser implements CommandParser<String[]> {

    public static final String DEFAULT_HOST = "http://localhost:8080";

    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_CREATE_NOTE = "add";

    private static final String COMMAND_LIST_FILTER = "filter";
    private static final String COMMAND_LIST_SORT = "sort";

    private final WebClient webClient;
    private final Output output;

    private final CommandLineParser parser;
    private final HelpFormatter helpFormatter;

    public ConsoleCommandParser(Output output, WebClient webClient) {
        this.output = output;
        this.webClient = webClient;

        parser = new DefaultParser();
        helpFormatter = new HelpFormatter();
    }

    private Options commandOptions() {
        Options actions = new Options();
        actions.addOption(actionOption(COMMAND_HELP, "Show available options"));
        actions.addOption(actionOption(COMMAND_LIST, "Lists creators"));
        actions.addOption(actionOption(COMMAND_CREATE_NOTE, "Adds a custom note to a creator"));

        return actions;
    }

    private Options listCommandOptions() {
        Options options = new Options();
        options.addOption(Option.builder(COMMAND_LIST_FILTER).desc("Field to filter").numberOfArgs(2).valueSeparator('=').build());
        options.addOption(COMMAND_LIST_SORT, true, "Field to sort");

        return options;
    }

    private Option actionOption(String argument, String description) {
        return
            Option
                .builder(argument)
                .hasArg(false)
                .required(false)
                .desc(description)
                .build();
    }

    @Override
    public Command parse(String[] args) {
        Command command;

        try {
            CommandLine actionLine = parser.parse(commandOptions(), args, true);

            if (actionLine.hasOption(COMMAND_HELP)) {
                command = new ShowHelpCommand(output, buildHelpMessage());
            }
            else if (actionLine.hasOption(COMMAND_LIST)) {
                command =
                    parseListCommand(
                        Arrays
                            .stream(args)
                            .filter(value -> !value.equals("-" + COMMAND_LIST))
                            .toArray(length -> new String[length])
                    );
            }
            else if (actionLine.hasOption(COMMAND_CREATE_NOTE)) {
                command =
                    new CreateNoteCommandDefinition()
                        .parse(output, webClient, DEFAULT_HOST, parser, removeArgument(args, COMMAND_CREATE_NOTE));
            }
            else {
                command = new ErrorParsingCommand(output, "Invalid Command");
            }
        } catch (ParseException Pe) {
            command = new ErrorParsingCommand(output, Pe.getMessage());
        }

        return command;
    }

    private String[] removeArgument(String args[], String argument) {
        return
            Arrays
                .stream(args)
                .filter(value -> !value.equals("-" + argument))
                .toArray(length -> new String[length]);
    }

    private Command parseListCommand(String[] args) throws ParseException {
        CommandLine line = parser.parse(listCommandOptions(), args);
        String[] filter = line.getOptionValues(COMMAND_LIST_FILTER);

        return
            new ListCreatorsCommand(
                output,
                webClient,
                DEFAULT_HOST,
                filter != null ? filter[0] : null,
                filter != null ? filter[1] : null,
                line.getOptionValue(COMMAND_LIST_SORT));
    }

    private String buildHelpMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        helpFormatter.printHelp(printWriter, 74, "bo-cli", "", commandOptions(), 1, 3, "");

        return stringWriter.toString();
    }
}
