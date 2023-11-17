package sjoquist.mathew.capstone.queryparser.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {
    @GetMapping("/parse")
    public ResponseEntity<String> parseQuery(@RequestParam String query) {
        return ResponseEntity.ok().body(parse(query));
    }

    /**
     * Takes a user provided string and parses it into an endpoint for the website
     * retrieval service
     * 
     * @param query The user provided string to parse
     * @return The endpoint to query the search engine with
     */
    public static String parse(String query) {
        List<String> tokens = new ArrayList<>(Arrays.asList(Stream.of(query.split("\\+")).filter(s -> !s.isBlank())
                .map(String::toLowerCase).toArray(String[]::new)));
        List<String> specialTokens = tokens.stream().filter(s -> s.startsWith("-") || s.startsWith("url:")).toList();
        tokens.removeAll(specialTokens);
        StringBuilder endpoint = new StringBuilder("/webpages/search?");

        // Add all regular tokens
        endpoint.append("text=");
        for (String token : tokens) {
            endpoint.append(token).append("+");
        }
        if (!tokens.isEmpty())
            endpoint.deleteCharAt(endpoint.length() - 1);

        // Add all excluded tokens
        if (specialTokens.stream().anyMatch(s -> s.startsWith("-"))) {
            endpoint.append("&exclude=");
            for (String token : specialTokens.stream().filter(s -> s.startsWith("-")).toList()) {
                endpoint.append(token.substring(1)).append("+");
            }
            endpoint.deleteCharAt(endpoint.length() - 1);
        }

        // Add all url tokens
        if (specialTokens.stream().anyMatch(s -> s.startsWith("url:"))) {
            endpoint.append("&url=");
            for (String token : specialTokens.stream().filter(s -> s.startsWith("url:")).toList()) {
                endpoint.append(token.substring(4)).append("+");
            }
            endpoint.deleteCharAt(endpoint.length() - 1);
        }

        return endpoint.toString();
    }
}
