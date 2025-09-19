package JUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteAccountTest {

    private final String baseUrl = "http://localhost:8081"; // Zaktualizuj zgodnie z adresem swojego serwera

    @Test
    public void testDeleteAccountEndpoint() throws JSONException {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Przygotowanie danych do wysłania jako JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Przygotowanie danych do wysłania jako JSON
        JSONObject requestBody = new JSONObject();
        requestBody.put("accountidtodelete", "1"); // ID konta do usunięcia

        // Ustawienie nagłówków żądania
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + "/deleteaccount", HttpMethod.POST, requestEntity, String.class);

        // Sprawdzenie statusu odpowiedzi
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Sprawdzenie zawartości odpowiedzi
        String responseBody = responseEntity.getBody();
        assertEquals("Oznaczono konto o id: 1 jako usuniętą", responseBody); // Zakładając, że usunięto konto o ID 1
    }
}
