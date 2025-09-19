package RestTemplate;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class RestPostLogin {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        String endpointUrl = "http://localhost:8081/login";

        // Dane do wysłania w formacie JSON
        String jsonData = "{\"login\": \"exampleLogin\", \"password\": \"examplePassword\"}";

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
