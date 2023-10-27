package sjoquist.mathew.capstone.websitepersistence.kafka;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import sjoquist.mathew.capstone.websitepersistence.models.Webpage;
import sjoquist.mathew.capstone.websitepersistence.repositories.IWebpageRepository;

@Component
public class WebpageConsumer {
    private IWebpageRepository webpageRepository;

    public WebpageConsumer(IWebpageRepository webpageRepository) {
        this.webpageRepository = webpageRepository;
    }

    @KafkaListener(topics = "webpages", groupId = "websitepersistence")
    public void consume(Webpage message) {
        // find the node in the database, if it exists
        webpageRepository.findById(message.getUrl()).ifPresentOrElse(
                startNode -> {
                    // if the node exists, update the text and links
                    startNode.setText(message.getText());

                    // clear the links to prevent duplcated urls
                    startNode.setLinksTo(new HashSet<>());

                    // get the new links as strings for the next step
                    Set<String> linksToStrings = message.getLinksTo().stream().map(Webpage::getUrl)
                            .collect(Collectors.toSet());

                    // find all the links that are already in the database and add them to the node
                    webpageRepository.findAllById(linksToStrings)
                            .forEach(endNode -> startNode.getLinksTo().add(endNode));

                    // add the new links that weren't in the database
                    for (Webpage page : message.getLinksTo()) {
                        if (!linksToStrings.contains(page.getUrl())) {
                            startNode.getLinksTo().add(page);
                        }
                    }

                    webpageRepository.save(startNode);
                },
                // if the node doesn't exist, save the new node
                () -> webpageRepository.save(message));
    }
}
