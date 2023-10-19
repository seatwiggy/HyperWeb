package sjoquist.mathew.capstone.webcrawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.springframework.stereotype.Component;
import sjoquist.mathew.capstone.webcrawler.kafka.WebpageProducer;
import sjoquist.mathew.capstone.webcrawler.models.Webpage;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class MyCrawler extends WebCrawler {
    private static final Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp4|zip|gz))$");

    private final WebpageProducer producer;

    public MyCrawler(WebpageProducer producer) {
        this.producer = producer;
    }

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions.
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

        if (page.getParseData() instanceof HtmlParseData htmlParseData) {
            String text = htmlParseData.getText();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            Set<Webpage> linksTo = new HashSet<>();
            for (WebURL link : links) {
                linksTo.add(new Webpage(link.getURL(), "", null));
            }

            Webpage webpage = new Webpage(url, text, linksTo);
            producer.send(webpage);
        }
    }

    /**
     * Determine whether links found at the given URL should be added to the queue
     * for crawling.
     */
    @Override
    protected boolean shouldFollowLinksIn(WebURL url) {
        return url.getURL().toLowerCase().startsWith("https://en.wikipedia.org/wiki/");
    }
}