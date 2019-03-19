package es.molabs.bocli.ouput;

public class ConsoleOutput implements Output {

    @Override
    public void printLine(String line) {
        System.out.println(line);
    }
}