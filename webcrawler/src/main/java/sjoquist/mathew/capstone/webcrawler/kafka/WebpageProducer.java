package sjoquist.mathew.capstone.webcrawler.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import sjoquist.mathew.capstone.webcrawler.models.Webpage;

@Service
public class WebpageProducer {
    private KafkaTemplate<String, Webpage> kafkaTemplate;

    public WebpageProducer(KafkaTemplate<String, Webpage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Webpage message) {
        kafkaTemplate.send("Webpage", message);
    }
}
