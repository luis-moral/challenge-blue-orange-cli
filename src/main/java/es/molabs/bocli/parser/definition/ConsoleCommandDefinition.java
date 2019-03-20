package es.molabs.bocli.parser.definition;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.command.Command;
import es.molabs.bocli.ouput.Output;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;

public abstract class ConsoleCommandDefinition<T extends Command> {

    private final Options commandOptions;

    public ConsoleCommandDefinition(Option...options) {
        commandOptions = new Options();

        Arrays
            .stream(options)
            .forEach(option -> commandOptions.addOption(option));
    }

    public Options options() {
        return commandOptions;
    }

    public abstract T parse(Output output, WebClient webClient, String host, CommandLineParser parser, String[] args) throws ParseException;
}
