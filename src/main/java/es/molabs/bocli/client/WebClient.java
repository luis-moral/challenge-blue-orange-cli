package es.molabs.bocli.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class WebClient {

    public String get(String url, Map<String, String> queryString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url + buildQueryString(queryString)).openConnection();
        String body = readBody(connection);
        connection.disconnect();

        return body;
    }

    public void post(String url, String requestBody) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        sendBody(connection, "POST", requestBody);
        connection.disconnect();
    }

    public void put(String url, String id, String requestBody) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url + "/" + id).openConnection();
        sendBody(connection, "PUT", requestBody);
        connection.disconnect();
    }

    public void delete(String url, String id) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url + "/" + id).openConnection();
        sendBody(connection, "DELETE", null);
        connection.disconnect();
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

    private void sendBody(HttpURLConnection connection, String method, String requestBody) throws IOException {
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestProperty("Accept", "*/*");

        if (requestBody != null) {
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(requestBody);
            writer.flush();
            writer.close();
        }

        connection.getResponseCode();
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

        reader.close();

        return body.toString();
    }
}
