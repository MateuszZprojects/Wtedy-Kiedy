package JUnit;

import org.junit.Test;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testLoginEndpointWithValidCredentials() {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Przygotowanie danych do wysłania jako JSON
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("login", "exampleLogin");
        loginRequest.put("password", "examplePassword");

        // Ustawienie nagłówków żądania
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(baseUrl + "/login", HttpMethod.POST, requestEntity, Map.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        Map<String, String> responseBody = responseEntity.getBody();
        assertEquals("exampleLogin", responseBody.get("userLogin"));
        assertEquals("4", responseBody.get("userId")); // Załóżmy, że ID użytkownika wynosi 1
        assertEquals("1", responseBody.get("userRole")); // Załóżmy, że rola użytkownika to "user"
    }

}
