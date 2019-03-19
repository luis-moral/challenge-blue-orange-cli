package es.molabs.bocli.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class WebClient {

    public String get(String url, Map<String, String> queryString) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url + buildQueryString(queryString)).openConnection();
        String body = readBody(urlConnection);
        urlConnection.disconnect();

        return body;
    }

    private String buildQueryString(Map<String, String> queryString) {
        String query =
            queryString
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + encodeUrl(entry.getValue()))
                .collect(Collectors.joining("&"));

        return query.length() > 0 ? "?" + query : "";
    }

    private String encodeUrl(String value) {
        String encodedValue;

        try {
            encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            encodedValue = value;
        }

        return encodedValue;
    }

    private String readBody(HttpURLConnection urlConnection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder body = new StringBuilder();
        String inputLine;

        while ((inputLine = reader.readLine()) != null) {
            if (body.length() != 0) {
                body.append("\n");
            }

            body.append(inputLine);
        }

        return body.toString();
    }
}
