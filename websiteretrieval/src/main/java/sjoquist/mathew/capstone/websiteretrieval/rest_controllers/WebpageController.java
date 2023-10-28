package sjoquist.mathew.capstone.websiteretrieval.rest_controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import sjoquist.mathew.capstone.websiteretrieval.models.Webpage;
import sjoquist.mathew.capstone.websiteretrieval.repositories.IWebpageRepository;

@RestController("/webpages")
class WebpageController {
    IWebpageRepository webpageRepository;

    WebpageController(IWebpageRepository webpageRepository) {
        this.webpageRepository = webpageRepository;
    }

    @GetMapping
    public ResponseEntity<List<Webpage>> getWebpages() {
        return ResponseEntity.ok().body(webpageRepository.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Webpage>> searchWebpages(String text) {
        return ResponseEntity.ok().body(webpageRepository.findByTextContainingAllIgnoreCase(text));
    }

    @GetMapping("/search/url")
    public ResponseEntity<List<Webpage>> searchWebpagesByUrl(String url) {
        return ResponseEntity.ok().body(webpageRepository.findByUrlContainingIgnoreCase(url));
    }
}