package es.molabs.bocli.parser;

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

    private final Output output;

    private final Options options;
    private final CommandLineParser parser;
    private final HelpFormatter helpFormatter;

    public ConsoleCommandParser(Output output) {
        this.output = output;

        options = new Options();
        options.addOption(COMMAND_HELP, false, "Show available options");
        options.addOption(COMMAND_LIST, false, "Lists creators");
        options.addOption(COMMAND_LIST_FILTER, true, "Field to filter");
        options.addOption(COMMAND_LIST_SORT, true, "Field to sort");

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

        if (line.hasOption(COMMAND_HELP)) {
            command = new ShowHelpCommand(output, buildHelpMessage());
        }
        else if (line.hasOption(COMMAND_LIST)) {
            command = new ListCreatorsCommand(output, line.getOptionValue(COMMAND_LIST_FILTER), line.getOptionValue(COMMAND_LIST_SORT));
        }
        else {
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
