package sjoquist.mathew.capstone.webcrawler.kafka;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import sjoquist.mathew.capstone.webcrawler.models.Webpage;


@Component
public class WebpageProducer {
    private final KafkaTemplate<String, Webpage> kafkaTemplate;

    public WebpageProducer(KafkaTemplate<String, Webpage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Webpage webpage) {
        kafkaTemplate.send("webpages", webpage);
    }
}
