package JUnit;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testAddAccountEndpoint() {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        String jsonRequest = "{\"login\":\"testLogin\",\"password\":\"testPassword\"}";

        // Wywołanie endpointu
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl + "/register", jsonRequest, String.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        String responseBody = responseEntity.getBody();
        assertEquals("New account added successfully", responseBody);
    }

}
