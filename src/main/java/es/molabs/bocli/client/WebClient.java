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

        String body = readResponseBody(connection);

        validateResponse(connection.getResponseCode(), body);
        connection.disconnect();

        return body;
    }

    public String post(String url, String requestBody) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        sendRequestBody(connection, "POST", requestBody);
        String body = readResponseBody(connection);

        validateResponse(connection.getResponseCode(), body);
        connection.disconnect();

        return body;
    }

    public String put(String url, String id, String requestBody) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url + "/" + id).openConnection();

        sendRequestBody(connection, "PUT", requestBody);
        String body = readResponseBody(connection);

        validateResponse(connection.getResponseCode(), body);
        connection.disconnect();

        return body;
    }

    public String delete(String url, String id) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url + "/" + id).openConnection();

        sendRequestBody(connection, "DELETE", null);
        String body = readResponseBody(connection);

        validateResponse(connection.getResponseCode(), body);
        connection.disconnect();

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

    private void sendRequestBody(HttpURLConnection connection, String method, String requestBody) throws IOException {
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
    }

    private void validateResponse(int responseCode, String body) throws IOException {
        if (responseCode >= 300) {
            throw new InvalidResponseException(responseCode, body);
        }
    }

    private String readResponseBody(HttpURLConnection urlConnection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(urlConnection)));
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

    private InputStream getInputStream(HttpURLConnection urlConnection) {
        InputStream inputStream;

        try {
            inputStream = urlConnection.getInputStream();
        }
        catch (IOException e) {
            inputStream = urlConnection.getErrorStream();
        }

        return inputStream;
    }
}
