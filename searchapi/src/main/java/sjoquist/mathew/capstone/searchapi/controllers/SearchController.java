package sjoquist.mathew.capstone.searchapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
public class SearchController {
    private final RestTemplate restTemplate;

    public SearchController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity<String> search(@RequestParam String search) {
        // Parse the query
        String searchEndpoint = restTemplate.getForObject("http://QUERY-PARSER-SERVICE/parse?query=" + search,
                String.class);

        if (searchEndpoint == null || searchEndpoint.isBlank()) {
            return ResponseEntity.internalServerError().body("Error parsing query");
        }

        // Retrieve the results
        String result = restTemplate
                .getForObject("http://WEBSITE-RETRIEVAL-SERVICE" + searchEndpoint.replace(" ", "+"), String.class);

        if ("[]".equals(result)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok().body(result);
    }
}
