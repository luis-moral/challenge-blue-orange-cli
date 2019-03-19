package es.molabs.bocli;

import es.molabs.bocli.cli.SystemCommandParser;
import es.molabs.bocli.cli.SystemOutput;

public class Application {

    public static void main(String[] args) {
        new SystemCommandParser(new SystemOutput()).execute(args);
    }
}
