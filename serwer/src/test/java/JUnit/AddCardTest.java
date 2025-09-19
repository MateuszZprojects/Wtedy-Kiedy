package JUnit;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddCardTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testAddCardEndpoint() {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        String jsonRequest = "{\"year\":2024,\"description\":\"Example description\"}";

        // Wywołanie endpointu
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl + "/addcard", jsonRequest, String.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        String responseBody = responseEntity.getBody();
        assertEquals("New card added successfully", responseBody);
    }

}
