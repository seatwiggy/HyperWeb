package sjoquist.mathew.capstone.webcrawler;

import java.util.HashMap;
import java.util.Set;

// https://github.com/yasserg/crawler4j
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import sjoquist.mathew.capstone.webcrawler.kafka.WebpageProducer;
import sjoquist.mathew.capstone.webcrawler.models.Webpage;

public class BasicCrawler extends WebCrawler {
    WebpageProducer webpageProducer;

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = referringPage.getWebURL().getURL();
        // Only visit pages linked to from the English Wikipedia
        return href.startsWith("https://en.wikipedia.org/wiki/");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            // Format webpage data
            HashMap<String, Integer> termMatrix = getTermMatrix(text);
            Webpage webpage = new Webpage(url, termMatrix, links.toArray(new String[links.size()]));

            // Send the webpage to kafka
            webpageProducer.send(webpage);
        }
    }

    private HashMap<String, Integer> getTermMatrix(String text) {
        HashMap<String, Integer> termMatrix = new HashMap<String, Integer>();
        String[] words = text.split(" ");
        for (String word : words) {
            if (termMatrix.containsKey(word)) {
                termMatrix.put(word, termMatrix.get(word) + 1);
            } else {
                termMatrix.put(word, 1);
            }
        }
        return termMatrix;
    }
}
