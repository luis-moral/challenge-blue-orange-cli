package es.molabs.bocli.cli;

public interface CommandParser<T> {

    void execute(T args);
}
