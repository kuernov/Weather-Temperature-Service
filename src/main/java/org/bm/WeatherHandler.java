package org.bm;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class WeatherHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>{
    private final OpenMeteoClient weatherClient = new OpenMeteoClient();
    private final GeocodingClient geoClient = new GeocodingClient();
    private final WeatherService weatherService = new WeatherService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context){
        try {
            String city = extractCity(request);

            double[] coords = geoClient.getCoordinates(city);
            double temp = weatherClient.getTemperature(coords[0], coords[1]);

            String category = weatherService.determineCategory(temp);
            WeatherResponse weatherResponse = new WeatherResponse(city, temp, category);
            return createResponse(200, weatherResponse);

        } catch (IllegalArgumentException e) {
            return createErrorResponse(404, e.getMessage());
        } catch (Exception e) {
            return createErrorResponse(500, "Internal Server Error: " + e.getMessage());
        }

    }

    private String extractCity(APIGatewayProxyRequestEvent request) {
        if (request.getQueryStringParameters() != null && request.getQueryStringParameters().containsKey("city")) {
            String city = request.getQueryStringParameters().get("city");
            if (!city.trim().isEmpty()) return city;
        }
        return "Wrocław";
    }

    private APIGatewayProxyResponseEvent createResponse(int statusCode, Object body) throws Exception {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(objectMapper.writeValueAsString(body));
    }

    private APIGatewayProxyResponseEvent createErrorResponse(int statusCode, String message) {
        return new APIGatewayProxyResponseEvent()
                .withStatusCode(statusCode)
                .withHeaders(Map.of("Content-Type", "application/json"))
                .withBody(String.format("{\"error\": \"%s\"}", message));
    }

}
