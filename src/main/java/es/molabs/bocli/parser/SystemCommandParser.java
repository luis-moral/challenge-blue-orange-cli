package es.molabs.bocli.parser;

import es.molabs.bocli.command.Command;
import es.molabs.bocli.ouput.Output;

public class SystemCommandParser implements CommandParser<String[]> {

    private final Output output;

    public SystemCommandParser(Output output) {
        this.output = output;
    }

    @Override
    public Command parse(String[] args) {
        throw new UnsupportedOperationException();
    }
}
