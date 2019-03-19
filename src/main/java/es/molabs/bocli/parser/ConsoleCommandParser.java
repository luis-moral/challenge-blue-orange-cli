package es.molabs.bocli.parser;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.Command;
import es.molabs.bocli.command.ErrorParsingCommand;
import es.molabs.bocli.command.ListCreatorsCommand;
import es.molabs.bocli.command.ShowHelpCommand;
import es.molabs.bocli.ouput.Output;
import org.apache.commons.cli.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ConsoleCommandParser implements CommandParser<String[]> {

    private static final String COMMAND_HELP = "help";

    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_LIST_FILTER = "filter";
    private static final String COMMAND_LIST_SORT = "sort";

    private final WebClient webClient;
    private final Output output;

    private final Options options;
    private final CommandLineParser parser;
    private final HelpFormatter helpFormatter;

    public ConsoleCommandParser(Output output, WebClient webClient) {
        this.output = output;
        this.webClient = webClient;

        options = new Options();
        options.addOption(COMMAND_HELP, false, "Show available options");
        options.addOption(COMMAND_LIST, false, "Lists creators");
        options.addOption(Option.builder(COMMAND_LIST_FILTER).desc("Field to filter").valueSeparator('=').build());
        options.addOption(COMMAND_LIST_SORT, false, "Field to sort");

        parser = new DefaultParser();
        helpFormatter = new HelpFormatter();
    }

    @Override
    public Command parse(String[] args) {
        Command command;

        try {
            command = getCommand(parser.parse(options, args));
        } catch (ParseException Pe) {
            command = new ErrorParsingCommand(output, Pe.getMessage());
        }

        return command;
    }

    private Command getCommand(CommandLine line) {
        Command command;
        String host = "host";

        if (line.hasOption(COMMAND_HELP)) {
            command = new ShowHelpCommand(output, buildHelpMessage());
        } else if (line.hasOption(COMMAND_LIST)) {
            String[] filter = line.getOptionValues(COMMAND_LIST_FILTER);
            command = new ListCreatorsCommand(output, webClient, host, filter[0], filter[1], line.getOptionValue(COMMAND_LIST_SORT));
        } else {
            command = new ErrorParsingCommand(output, "Invalid Command");
        }

        return command;
    }

    private String buildHelpMessage() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        helpFormatter.printHelp(printWriter, 74, "bo-cli", "", options, 1, 3, "");

        return stringWriter.toString();
    }
}
