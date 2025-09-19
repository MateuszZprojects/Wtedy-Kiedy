package RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestGetCards {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String endpointUrl = "http://localhost:8081/getcards";

        // Wykonanie zapytania GET
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(endpointUrl, String.class);

        // Pobranie odpowiedzi
        String response = responseEntity.getBody();

        // Przetworzenie odpowiedzi
        System.out.println("Odpowiedź z serwera: " + response);
    }
}