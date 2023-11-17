package sjoquist.mathew.capstone.websiteretrieval.rest_controllers;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sjoquist.mathew.capstone.websiteretrieval.models.Webpage;
import sjoquist.mathew.capstone.websiteretrieval.repositories.IWebpageRepository;

@RestController
@RequestMapping("/webpages")
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
    public ResponseEntity<List<Webpage>> searchWebpages(@RequestParam String text) {
        List<String> queryList = Stream.of(text.split("\\+")).map(s -> " " + s + " ").toList();
        return ResponseEntity.ok().body(webpageRepository.findByTextContainingAllIgnoreCaseList(queryList));
    }

    @GetMapping("/search/url")
    public ResponseEntity<List<Webpage>> searchWebpagesByUrl(@RequestParam String url) {
        return ResponseEntity.ok().body(webpageRepository.findByUrlContainingIgnoreCase(url));
    }
}