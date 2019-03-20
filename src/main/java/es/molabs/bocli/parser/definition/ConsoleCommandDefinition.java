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

    private final String option;
    private final String argumentName;
    private final String description;
    private final Options commandOptions;

    public ConsoleCommandDefinition(String option, String argumentName, String description, Option...options) {
        this.option = option;
        this.argumentName = argumentName;
        this.description = description;

        commandOptions = new Options();

        Arrays
            .stream(options)
            .forEach(value -> commandOptions.addOption(value));
    }

    public Options options() {
        return commandOptions;
    }

    public String option() {
        return option;
    }

    public String argumentName() {
        return argumentName;
    }

    public String description() {
        return description;
    }

    public abstract T parse(Output output, WebClient webClient, String host, CommandLineParser parser, String[] args) throws ParseException;
}
