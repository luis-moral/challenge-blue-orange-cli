package es.molabs.bocli.parser;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.Command;
import es.molabs.bocli.command.ErrorParsingCommand;
import es.molabs.bocli.command.ShowHelpCommand;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.definition.*;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConsoleCommandParser implements CommandParser<String[]> {

    public static final String DEFAULT_HOST = "http://localhost:8080";

    private static final String COMMAND_HELP = "help";
    private static final String ARGUMENT_SERVER = "server";

    private static final Map<String, ConsoleCommandDefinition> definitionMap;

    static {
        ListCreatorsCommandDefinition listCreatorsCommandDefinition = new ListCreatorsCommandDefinition();
        CreateNoteCommandDefinition createNoteCommandDefinition = new CreateNoteCommandDefinition();
        EditNoteCommandDefinition editNoteCommandDefinition = new EditNoteCommandDefinition();
        DeleteNoteCommandDefinition deleteNoteCommandDefinition = new DeleteNoteCommandDefinition();

        definitionMap = new HashMap<>();
        definitionMap.put(listCreatorsCommandDefinition.option(), listCreatorsCommandDefinition);
        definitionMap.put(createNoteCommandDefinition.option(), createNoteCommandDefinition);
        definitionMap.put(editNoteCommandDefinition.option(), editNoteCommandDefinition);
        definitionMap.put(deleteNoteCommandDefinition.option(), deleteNoteCommandDefinition);
    }

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
        Options options = new Options();

        options.addOption(Option.builder(COMMAND_HELP).desc("Show available options").build());
        options.addOption(Option.builder(ARGUMENT_SERVER).argName("url").hasArg().desc("Specifies the api server url (defaults to localhost)").build());

        definitionMap
            .values()
            .stream()
            .forEach(
                definition ->
                    options
                        .addOption(
                            Option
                                .builder(definition.option())
                                .hasArg(true)
                                .optionalArg(true)
                                .argName(definition.argumentName())
                                .required(false)
                                .desc(definition.description())
                                .build()
                        )
            );

        return options;
    }

    @Override
    public Command parse(String[] args) {
        Command command;

        try {
            CommandLine line = parser.parse(commandOptions(), args, true);

            if (line.hasOption(COMMAND_HELP)) {
                command = new ShowHelpCommand(output, buildHelpMessage());
            }
            else {
                command =
                    definitionMap
                        .keySet()
                        .stream()
                        .filter(key -> line.hasOption(key))
                        .map(option -> definitionMap.get(option))
                        .map(definition -> parseDefinition(definition, line, args))
                        .findFirst()
                        .orElse(new ErrorParsingCommand(output, "Invalid Command"));
            }
        }
        catch (ParseException Pe) {
            command = new ErrorParsingCommand(output, Pe.getMessage());
        }

        return command;
    }

    private Command parseDefinition(ConsoleCommandDefinition definition, CommandLine line, String[] args) {
        try {
            return definition.parse(output, webClient, getHost(line), parser, removeCommandAndRemote(args, definition.option()));
        }
        catch (ParseException Pe) {
            return new ErrorParsingCommand(output, Pe.getMessage());
        }
    }

    private String getHost(CommandLine line) {
        return Optional.ofNullable(line.getOptionValue(ARGUMENT_SERVER)).orElse(DEFAULT_HOST);
    }

    private String[] removeCommandAndRemote(String args[], String command) {
        return
            Arrays
                .stream(args)
                .filter(value -> !value.equals("-" + command) && !value.equals("-" + ARGUMENT_SERVER))
                .toArray(length -> new String[length]);
    }

    private String buildHelpMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        helpFormatter.printHelp(printWriter, 120, "bo-cli", "", commandOptions(), 1, 3, "");

        return stringWriter.toString();
    }
}
