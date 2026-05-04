# Weather Temperature Service
## a. Brief Description
This is a serverless Java application deployed on AWS Lambda. It fetches the current temperature for a given city (defaulting to Wrocław) using the Open-Meteo API. The app maps the numeric temperature to a category (e.g., Cold, Warm) and returns the result as a structured JSON via a public Lambda Function URL.

## b. Key Design Decisions

The code is structured around Object-Oriented Design and the Single Responsibility Principle:
* **Separation of Concerns:** External API calls are isolated in `OpenMeteoClient` and `GeocodingClient`. The core business logic (categorization) lives separately in `WeatherService`.
* **Orchestration Only:** The `WeatherHandler` contains no business logic. It simply reads parameters, orchestrates the clients/services, and builds the HTTP response.
* **Error Handling:** If a city is not found or an API fails, the handler catches the exceptions and returns meaningful HTTP status codes (e.g., 404 or 500) instead of crashing.


## c. Unit Testing Strategy (Without Real API)
To test this without hitting the real API, I would use **JUnit** and **Mockito**.
By applying Dependency Injection, I could pass mocked versions of `OpenMeteoClient` and `GeocodingClient` into the handler. This allows me to stub their responses (e.g., `when(client.getTemperature(...)).thenReturn(25.0)`) and safely test the handler's flow and the `WeatherService` categorization in isolation, without relying on a network connection.

## Task 3.

* **Publicly accessible URL:** `https://gdaz4k3gonasayrjzbdrdr33ti0ztrsz.lambda-url.eu-north-1.on.aws/`
* **GET Parameter:** `city`

### Example Request
```http
GET https://gdaz4k3gonasayrjzbdrdr33ti0ztrsz.lambda-url.eu-north-1.on.aws/?city=Warsaw
```

### Example Response
```JSON
{
  "city": "Warsaw",
  "temperature": 23.3,
  "category": "Warm"
}
```

## Task 4.

## Task 4. Design Reflection

The current design makes a good first step by separating concerns. Because all the HTTP communication is isolated inside the `OpenMeteoClient`, the main handler and business logic (`WeatherService`) don't need to know how the specific API works. This supports future changes. However, the design is currently limited by tight coupling. Right now, `WeatherHandler` directly creates a `new OpenMeteoClient()`. If we wanted to add a second weather provider, we would have to modify the handler's code directly to switch between them, which isn't very flexible.

If I had more time, I would introduce a `WeatherProvider` interface. By using Dependency Injection, the handler would just rely on this interface instead of a concrete class. This way, adding a new provider would just mean creating a new class, without touching the core handler logic. I would also move hardcoded values (like API URLs) into AWS environment variables and add a simple caching layer to reduce external API calls and save Lambda execution costs.