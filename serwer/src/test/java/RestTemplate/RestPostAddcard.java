package RestTemplate;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestPostAddcard {

    public static void main(String[] args) {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Adres URL endpointu
        String endpointUrl = "http://localhost:8081/addcard";

        // Dane do wysłania w formacie JSON
        String jsonData = "{\"year\": 2023, \"description\": \"Sample card description\"}"; // Dane nowej karty

        // Utworzenie nagłówków z odpowiednimi ustawieniami dla wysłania danych JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Utworzenie żądania POST z danymi i nagłówkami
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonData, headers);

        // Wykonanie zapytania POST
        ResponseEntity<String> responseEntity = restTemplate.exchange(endpointUrl, HttpMethod.POST, requestEntity, String.class);

        // Pobranie odpowiedzi
        String response = responseEntity.getBody();

        // Przetworzenie odpowiedzi
        System.out.println("Odpowiedź z serwera: " + response);
    }
}