package sjoquist.mathew.capstone.searchapi.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class SearchController {
    @GetMapping
    public ResponseEntity<String> search(@RequestParam String search) {
        try {
            // Parse the query
            URL parserUrl = new URL("http://QUERY-PARSER-SERVICE/parse?query=" + search);

            HttpURLConnection queryConnection = (HttpURLConnection) parserUrl.openConnection();
            queryConnection.setRequestMethod("GET");

            // Use parsed query to perform search
            String searchEndpoint = queryConnection.getResponseMessage();
            URL searchUrl = new URL("http://WEBSITE-RETRIEVAL-SERVICE" + searchEndpoint);

            HttpURLConnection searchConnection = (HttpURLConnection) searchUrl.openConnection();
            searchConnection.setRequestMethod("GET");

            // Read the response from the search
            StringBuilder responseBuilder = new StringBuilder();
            try (BufferedReader bRead = new BufferedReader(new InputStreamReader(searchConnection.getInputStream()))) {
                String line;
                while ((line = bRead.readLine()) != null) {
                    responseBuilder.append(line);
                }
            } finally {
                queryConnection.disconnect();
            }

            return ResponseEntity.ok().body(responseBuilder.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Trouble Parsing Query or Retrieving Results");
        }
    }
}
