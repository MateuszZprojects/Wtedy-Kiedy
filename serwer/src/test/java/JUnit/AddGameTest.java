package JUnit;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddGameTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testAddGameEndpoint() {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        String jsonRequest = "{\"login\":\"exampleLogin\"}";

        // Wywołanie endpointu
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(baseUrl + "/addgame", jsonRequest, String.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        String responseBody = responseEntity.getBody();
        assertNotNull(responseBody); // Upewnienie się, że odpowiedź nie jest pusta

        // Przykładowa weryfikacja na podstawie zawartości odpowiedzi
        assertTrue(responseBody.contains("gameId"), "Odpowiedź powinna zawierać identyfikator gry");
    }
}
