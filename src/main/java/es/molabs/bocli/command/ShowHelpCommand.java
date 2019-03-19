package es.molabs.bocli.command;

import es.molabs.bocli.ouput.Output;

public class ShowHelpCommand implements Command {

    private final Output output;
    private final String message;

    public ShowHelpCommand(Output output, String message) {
        this.output = output;
        this.message = message;
    }

    @Override
    public void execute() {
        output.printLine(message);
    }
}