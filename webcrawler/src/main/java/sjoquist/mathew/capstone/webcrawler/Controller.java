package sjoquist.mathew.capstone.webcrawler;


import crawlercommons.filters.basic.BasicURLNormalizer;
import de.hshn.mi.crawler4j.frontier.HSQLDBFrontierConfiguration;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.FrontierConfiguration;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import sjoquist.mathew.capstone.webcrawler.kafka.WebpageProducer;

@SpringBootApplication
public class Controller {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Controller.class, args);
        final WebpageProducer producer = context.getBean(WebpageProducer.class);

        final int numberOfCrawlers = 5;
        CrawlConfig config = getCrawlConfig();

        // Instantiate the controller for this crawl.
        BasicURLNormalizer normalizer = BasicURLNormalizer.newBuilder().idnNormalization(BasicURLNormalizer.IdnNormalization.NONE).build();
        CrawlController controller = getCrawlController(config, normalizer);

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<MyCrawler> factory = () -> new MyCrawler(producer);

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        controller.start(factory, numberOfCrawlers);
    }

    private static CrawlController getCrawlController(CrawlConfig config, BasicURLNormalizer normalizer) throws Exception {
        CrawlController controller = getController(config, normalizer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        controller.addSeed("https://en.wikipedia.org/wiki/Main_Page");
        return controller;
    }

    private static CrawlController getController(CrawlConfig config, BasicURLNormalizer normalizer) throws Exception {
        PageFetcher pageFetcher = new PageFetcher(config, normalizer);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        FrontierConfiguration frontierConfiguration = new HSQLDBFrontierConfiguration(config, 10);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher, frontierConfiguration.getWebURLFactory());
        return new CrawlController(config, normalizer, pageFetcher, robotstxtServer, frontierConfiguration);
    }

    private static CrawlConfig getCrawlConfig() {
        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        config.setCrawlStorageFolder("/tmp/crawler4j/");

        // Be polite: Make sure that we don't send more than 1 request per second (1000 milliseconds between requests).
        // Otherwise, it may overload the target servers.
        config.setPolitenessDelay(1000);

        // You can set the maximum crawl depth here. The default value is -1 for unlimited depth.
        config.setMaxDepthOfCrawling(-1);

        // You can set the maximum number of pages to crawl. The default value is -1 for unlimited number of pages.
        // Neo4j AuraDB Free tier allows 200,000 nodes
        config.setMaxPagesToFetch(200000);

        // Should binary data should also be crawled? example: the contents of pdf, or the metadata of images etc
        config.setIncludeBinaryContentInCrawling(false);

        // Do you need to set a proxy? If so, you can use:
        //config.setProxyHost("proxyserver.example.com");
        //config.setProxyPort(8080);

        // If your proxy also needs authentication:
        //config.setProxyUsername(username); config.getProxyPassword(password);

        // This config parameter can be used to set your crawl to be resumable
        // (meaning that you can resume the crawl from a previously
        // interrupted/crashed crawl). Note: if you enable resuming feature and
        // want to start a fresh crawl, you need to delete the contents of
        // rootFolder manually.
        config.setResumableCrawling(true);

        // Set this to true if you want crawling to stop whenever an unexpected error
        // occurs. You'll probably want this set to true when you first start testing
        // your crawler, and then set to false once you're ready to let the crawler run
        // for a long time.
        config.setHaltOnError(true);
        return config;
    }
}
