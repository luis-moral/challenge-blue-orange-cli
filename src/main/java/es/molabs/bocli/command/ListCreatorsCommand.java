package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListCreatorsCommand implements Command {

    private static final String PATH_CREATORS = "/api/creators";
    private static final String FIELD_SORT = "sort";

    private final Output output;
    private final WebClient webClient;
    private final String host;
    private final String filterField;
    private final String filterValue;
    private final String sortValue;

    public ListCreatorsCommand(Output output, WebClient webClient, String host, String filterField, String filterValue, String sortValue) {
        this.output = output;
        this.webClient = webClient;
        this.host = host;
        this.filterField = filterField;
        this.filterValue = filterValue;
        this.sortValue = sortValue;
    }

    @Override
    public void execute() {
        String message;

        try {
            message = webClient.get(host + PATH_CREATORS, buildQueryString());
        }
        catch (IOException e) {
            message = e.getMessage();
        }

        output.printLine(message);
    }

    private Map<String, String> buildQueryString() {
        Map<String, String> queryString = new HashMap<>();

        if (filterField != null && filterValue != null) {
            queryString.put(filterField, filterValue);
        }

        if (sortValue != null) {
            queryString.put(FIELD_SORT, sortValue);
        }

        return queryString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListCreatorsCommand that = (ListCreatorsCommand) o;
        return Objects.equals(output, that.output) &&
            Objects.equals(webClient, that.webClient) &&
            Objects.equals(filterField, that.filterField) &&
            Objects.equals(filterValue, that.filterValue) &&
            Objects.equals(sortValue, that.sortValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, webClient, filterField, filterValue, sortValue);
    }
}
