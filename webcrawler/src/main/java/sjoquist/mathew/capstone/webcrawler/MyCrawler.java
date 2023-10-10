package sjoquist.mathew.capstone.webcrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import sjoquist.mathew.capstone.webcrawler.kafka.WebpageProducer;
import sjoquist.mathew.capstone.webcrawler.models.Webpage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

public class MyCrawler extends WebCrawler {
    @Autowired
    WebpageProducer producer;

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "https://en.wikipedia.org/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches();
    }

    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            Map<String, Integer> termMatrix = getTermMatrix(text);
            Set<Webpage> webpages = new HashSet<Webpage>();
            for (WebURL linkUrl : links) {
                webpages.add(new Webpage(linkUrl.getURL()));
            }

            producer.send(new Webpage(url, termMatrix, webpages));
        }
    }

    /**
     * Determine whether links found at the given URL should be added to the queue
     * for crawling.
     */
    @Override
    protected boolean shouldFollowLinksIn(WebURL url) {
        return url.toString().toLowerCase().startsWith("https://en.wikipedia.org/wiki");
    }

    private static Map<String, Integer> getTermMatrix(String text) {
        Map<String, Integer> termMatrix = new HashMap<>();
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