package RestTemplate;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestPostAddselect {

    public static void main(String[] args) {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Adres URL endpointu
        String endpointUrl = "http://localhost:8081/addselect";

        // Dane do wysłania w formie mapy
        Map<String, Object> payload = new HashMap<>();
        payload.put("time", "after");
        payload.put("cardYear", 2023);
        payload.put("pickedYear", 2022);
        payload.put("userID", 3);
        payload.put("gameID", 2);
        payload.put("gameRole", "player1");

        // Utworzenie nagłówków z odpowiednimi ustawieniami dla wysłania danych JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Utworzenie żądania POST z danymi i nagłówkami
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        // Wykonanie zapytania POST
        ResponseEntity<String> responseEntity = restTemplate.exchange(endpointUrl, HttpMethod.POST, requestEntity, String.class);

        // Pobranie odpowiedzi
        String response = responseEntity.getBody();

        // Przetworzenie odpowiedzi
        System.out.println("Odpowiedź z serwera: " + response);
    }
}

