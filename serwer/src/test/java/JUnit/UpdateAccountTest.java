package JUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateAccountTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testUpdateAccountEndpoint() throws JSONException {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Przygotowanie danych do wysłania jako JSON
        JSONObject requestBody = new JSONObject();
        requestBody.put("accountidtoupdate", "1"); // ID konta do aktualizacji

        // Ustawienie nagłówków żądania
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + "/updateaccount", HttpMethod.POST, requestEntity, String.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        String responseBody = responseEntity.getBody();
        assertEquals("ok", responseBody); // Zakładając, że aktualizacja konta zakończyła się sukcesem
    }
}
