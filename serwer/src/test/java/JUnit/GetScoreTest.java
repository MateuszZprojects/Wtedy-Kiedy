package JUnit;

import common.TGame;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetScoreTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testGetScoreEndpointWithValidGameId() {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        int gameId = 2; // ID gry do testowania
        String url = baseUrl + "/getscore?gameId=" + gameId;

        // Wywołanie endpointu
        ResponseEntity<TGame> responseEntity = restTemplate.getForEntity(url, TGame.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        TGame responseBody = responseEntity.getBody();
        assertEquals("JNowak", responseBody.getPlayer1login());
        assertEquals(10, responseBody.getPlayer1score());
        assertEquals("Removed", responseBody.getPlayer2login());
        assertEquals(3, responseBody.getPlayer2score());
        assertEquals(true, responseBody.getDeleted());
    }


}
