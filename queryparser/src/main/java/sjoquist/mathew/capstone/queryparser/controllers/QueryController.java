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

    public static String parse(String query) {
        List<String> tokens = Stream.of(query.split("\\s+")).filter(s -> !s.isBlank()).map(String::toLowerCase).toList();
        List<String> specialTokens = tokens.stream().filter(s -> s.startsWith("-") || s.startsWith("url:")).toList();
        tokens.removeAll(specialTokens);
        StringBuilder endpoint = new StringBuilder("/webpages/search?");

        endpoint.append("text=");
        for (String token : tokens) {
            if (token.startsWith(query))
                endpoint.append(token).append("+");
            endpoint.deleteCharAt(endpoint.length() - 1);
        }

        for (String token : specialTokens) {
            if (token.startsWith("-"))
                endpoint.append("&exclude=").append(token.substring(1));
            else if (token.startsWith("url:"))
                endpoint.append("&url=").append(token.substring(4));
        }
        return endpoint.toString();
    }
}
