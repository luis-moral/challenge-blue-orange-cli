package es.molabs.bocli.cli;

public class SystemCommandParser implements CommandParser<String[]> {

    private final Output output;

    public SystemCommandParser(Output output) {
        this.output = output;
    }

    @Override
    public void execute(String[] args) {
        throw new UnsupportedOperationException();
    }
}
