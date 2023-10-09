package sjoquist.mathew.capstone.websitepersistence.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import sjoquist.mathew.capstone.websitepersistence.models.Webpage;
import sjoquist.mathew.capstone.websitepersistence.repositories.IWebpageRepository;

@Service
public class WebpageConsumer {
    @Autowired
    private IWebpageRepository webpageRepository;

    @KafkaListener(topics = "Webpage")
    public void consume(Webpage message) {
        webpageRepository.save(message);
    }
}
