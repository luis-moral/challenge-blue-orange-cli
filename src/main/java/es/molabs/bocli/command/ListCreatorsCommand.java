package es.molabs.bocli.command;

import es.molabs.bocli.ouput.Output;

import java.util.Objects;

public class ListCreatorsCommand implements Command {

    private final Output output;
    private final String filter;
    private final String sort;

    public ListCreatorsCommand(Output output, String filter, String sort) {
        this.output = output;
        this.filter = filter;
        this.sort = sort;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListCreatorsCommand that = (ListCreatorsCommand) o;
        return Objects.equals(output, that.output) &&
                Objects.equals(filter, that.filter) &&
                Objects.equals(sort, that.sort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, filter, sort);
    }
}
