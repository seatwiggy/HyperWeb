package sjoquist.mathew.capstone.webcrawler;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import crawlercommons.filters.basic.BasicURLNormalizer;
import de.hshn.mi.crawler4j.frontier.HSQLDBFrontierConfiguration;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.frontier.FrontierConfiguration;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@SpringBootApplication
public class WebcrawlerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebcrawlerApplication.class, args);
        startCrawlerController();
    }

    private static void startCrawlerController() {
        final int numberOfCrawlers = 5;
        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls
        // that are extracted from previously
        // fetched pages and need to be crawled later).
        config.setCrawlStorageFolder("/tmp/crawler4j/");

        // Be polite: Make sure that we don't send more than 1 request per second (1000
        // milliseconds between requests).
        // Otherwise, it may overload the target servers.
        config.setPolitenessDelay(1000);

        // You can set the maximum crawl depth here. The default value is -1 for
        // unlimited depth.
        config.setMaxDepthOfCrawling(-1);

        // You can set the maximum number of pages to crawl. The default value is -1 for
        // unlimited number of pages.
        /* Neo4j AuraDB Free tier has max 200k nodes */
        config.setMaxPagesToFetch(200000);

        // Should binary data should also be crawled? example: the contents of pdf, or
        // the metadata of images etc
        config.setIncludeBinaryContentInCrawling(false);

        // Do you need to set a proxy? If so, you can use:
        // config.setProxyHost("proxyserver.example.com");
        // config.setProxyPort(8080);

        // If your proxy also needs authentication:
        // config.setProxyUsername(username); config.getProxyPassword(password);

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

        // Instantiate the controller for this crawl.
        BasicURLNormalizer normalizer = BasicURLNormalizer.newBuilder()
                .idnNormalization(BasicURLNormalizer.IdnNormalization.NONE).build();
        PageFetcher pageFetcher;
        try {
            pageFetcher = new PageFetcher(config, normalizer);
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
            return;
        }
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        FrontierConfiguration frontierConfiguration = new HSQLDBFrontierConfiguration(config, 10);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher,
                frontierConfiguration.getWebURLFactory());
        CrawlController controller;
        try {
            controller = new CrawlController(config, normalizer, pageFetcher, robotstxtServer, frontierConfiguration);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages
        try {
            controller.addSeed("https://en.wikipedia.org/wiki/Main_Page");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        // The factory which creates instances of crawlers.
        CrawlController.WebCrawlerFactory<MyCrawler> factory = MyCrawler::new;

        // Start the crawl. This is a blocking operation, meaning that your code
        // will reach the line after this only when crawling is finished.
        controller.start(factory, numberOfCrawlers);
    }
}
