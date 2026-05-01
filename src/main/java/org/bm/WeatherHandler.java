package org.bm;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class WeatherHandler implements RequestHandler<Object, WeatherResponse>{
    private final OpenMeteoClient client = new OpenMeteoClient();
    private final WeatherService service = new WeatherService();

    @Override
    public WeatherResponse handleRequest(Object input, Context context){
        try{
            double temp = client.getTemperature(51.10, 17.03);
            String category = service.determineCategory(temp);
            return new WeatherResponse("Wrocław", temp, category);
        } catch (Exception e){
            throw new RuntimeException("Error fetching weather", e);
        }
    }

}
