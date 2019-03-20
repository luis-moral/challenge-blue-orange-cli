package es.molabs.bocli.command;

import es.molabs.bocli.client.WebClient;
import es.molabs.bocli.ouput.Output;

import java.util.HashMap;
import java.util.Map;

public class ListCreatorsCommand extends WebClientCommand {

    private static final String PATH_CREATORS = "/api/creators";
    private static final String FIELD_SORT = "sort";

    private final String filterField;
    private final String filterValue;
    private final String sortValue;

    public ListCreatorsCommand(Output output, WebClient webClient, String host, String filterField, String filterValue, String sortValue) {
        super(output, webClient, host);

        this.filterField = filterField;
        this.filterValue = filterValue;
        this.sortValue = sortValue;
    }

    @Override
    public void execute() {
        getOutput()
            .printLine(
                call(() -> getWebClient().get(getHost() + PATH_CREATORS, buildQueryString()))
            );
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
}
