package sjoquist.mathew.capstone.websitepersistence.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import sjoquist.mathew.capstone.websitepersistence.models.Webpage;
import sjoquist.mathew.capstone.websitepersistence.repositories.IWebpageRepository;

@Service
public class WebpageConsumer {
    private IWebpageRepository webpageRepository;

    public WebpageConsumer(IWebpageRepository webpageRepository) {
        this.webpageRepository = webpageRepository;
    }

    @KafkaListener(topics = "Webpage", groupId = "websitepersistence")
    public void consume(Webpage message) {
        webpageRepository.save(message);
    }
}
