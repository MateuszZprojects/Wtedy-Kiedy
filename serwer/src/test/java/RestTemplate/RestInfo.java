package RestTemplate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
public class RestInfo {

    public static void main(String[] args) {
        // Tworzenie obiektu RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // Adres URL endpointu
        String endpointUrl = "http://localhost:8081/status";

        // Wykonanie zapytania GET
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(endpointUrl, String.class);

        // Pobranie odpowiedzi
        String response = responseEntity.getBody();

        // Przetworzenie odpowiedzi
        System.out.println("Odpowiedź z serwera: " + response);
    }
}