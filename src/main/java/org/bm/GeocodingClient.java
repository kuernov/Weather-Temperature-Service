package org.bm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeocodingClient {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public double[] getCoordinates(String cityName) throws Exception{
        String url = String.format("https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json",
                cityName.replace(" ", "+"));
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode results = root.path("results");

        if (results.isArray() && !results.isEmpty()) {
            double lat = results.get(0).path("latitude").asDouble();
            double lon = results.get(0).path("longitude").asDouble();
            return new double[]{lat, lon};
        } else {
            throw new IllegalArgumentException("City not found: " + cityName);
        }
    }
}
