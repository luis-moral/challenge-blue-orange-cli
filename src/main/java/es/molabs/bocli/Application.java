package es.molabs.bocli;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.ConsoleOutput;
import es.molabs.bocli.parser.ConsoleCommandParser;

public class Application {

    public static void main(String[] args) {
        new ConsoleCommandParser(new ConsoleOutput(), new WebClient()).parse(args).execute();
    }
}
