package es.molabs.bocli.parser;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.Command;
import es.molabs.bocli.command.ErrorParsingCommand;
import es.molabs.bocli.command.ShowHelpCommand;
import es.molabs.bocli.ouput.Output;
import es.molabs.bocli.parser.definition.CreateNoteCommandDefinition;
import es.molabs.bocli.parser.definition.DeleteNoteCommandDefinition;
import es.molabs.bocli.parser.definition.EditNoteCommandDefinition;
import es.molabs.bocli.parser.definition.ListCreatorsCommandDefinition;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Optional;

public class ConsoleCommandParser implements CommandParser<String[]> {

    public static final String DEFAULT_HOST = "http://localhost:8080";

    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_CREATE_NOTE = "add_note";
    private static final String COMMAND_EDIT_NOTE = "edit_note";
    private static final String COMMAND_DELETE_NOTE = "delete_note";

    private static final String ARGUMENT_SERVER = "server";

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
        actions.addOption(actionOption(COMMAND_EDIT_NOTE, "Edits a creator's custom note"));
        actions.addOption(actionOption(COMMAND_DELETE_NOTE, "Deletes a creator's custom note"));
        actions.addOption(Option.builder(ARGUMENT_SERVER).argName("url").hasArg().desc("Specifies the api server base url").build());

        return actions;
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
            CommandLine line = parser.parse(commandOptions(), args, true);

            if (line.hasOption(COMMAND_HELP)) {
                command = new ShowHelpCommand(output, buildHelpMessage());
            }
            else if (line.hasOption(COMMAND_LIST)) {
                command =
                    new ListCreatorsCommandDefinition()
                        .parse(output, webClient, getHost(line), parser, removeCommandAndRemote(args, COMMAND_LIST));
            }
            else if (line.hasOption(COMMAND_CREATE_NOTE)) {
                command =
                    new CreateNoteCommandDefinition()
                        .parse(output, webClient, getHost(line), parser, removeCommandAndRemote(args, COMMAND_CREATE_NOTE));
            }
            else if (line.hasOption(COMMAND_EDIT_NOTE)) {
                command =
                    new EditNoteCommandDefinition()
                        .parse(output, webClient, getHost(line), parser, removeCommandAndRemote(args, COMMAND_EDIT_NOTE));
            }
            else if (line.hasOption(COMMAND_DELETE_NOTE)) {
                command =
                    new DeleteNoteCommandDefinition()
                        .parse(output, webClient, getHost(line), parser, removeCommandAndRemote(args, COMMAND_DELETE_NOTE));
            }
            else {
                command = new ErrorParsingCommand(output, "Invalid Command");
            }
        } catch (ParseException Pe) {
            command = new ErrorParsingCommand(output, Pe.getMessage());
        }

        return command;
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

        helpFormatter.printHelp(printWriter, 74, "bo-cli", "", commandOptions(), 1, 3, "");

        return stringWriter.toString();
    }
}
