package sjoquist.mathew.capstone.queryparser.controllers;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/parse")
public class QueryController {
    @GetMapping("/{query}")
    public ResponseEntity<String> parseQuery(@PathVariable String query) {
        return ResponseEntity.ok().body(parse(query));
    }

    /**
     * Takes a user provided string and parses it into an endpoint for the website retrieval service
     * @param query The user provided string to parse
     * @return The endpoint to query the search engine with
     */
    public static String parse(String query) {
        List<String> tokens = Stream.of(query.split("\\s+")).filter(s -> !s.isBlank()).map(String::toLowerCase).toList();
        List<String> specialTokens = tokens.stream().filter(s -> s.startsWith("-") || s.startsWith("url:")).toList();
        tokens.removeAll(specialTokens);
        StringBuilder endpoint = new StringBuilder("/webpages/search?");

        // Add all regular tokens
        endpoint.append("text=");
        for (String token : tokens) {
            if (token.startsWith(query))
                endpoint.append(token).append("+");
            endpoint.deleteCharAt(endpoint.length() - 1);
        }

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
