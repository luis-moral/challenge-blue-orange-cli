package es.molabs.bocli;

import es.molabs.bocli.ouput.SystemOutput;
import es.molabs.bocli.parser.SystemCommandParser;

public class Application {

    public static void main(String[] args) {
        new SystemCommandParser(new SystemOutput()).parse(args);
    }
}
