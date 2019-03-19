package es.molabs.bocli.parser;

import es.molabs.bocli.command.Command;

public interface CommandParser<T> {

    Command parse(T args);
}
