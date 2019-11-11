package id.pascal.bookscrawler;

import id.pascal.bookscrawler.models.Crawler;
import id.pascal.bookscrawler.models.eaters.CSVBookEater;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {

    private static final String DEFAULT_FROM = "1";
    private static final String DEFAULT_TO = "2";
    private static final String DEFAULT_PERPAGE = "100";
    
    public static void main(String[] args) throws IOException {
        Options options = new Options();
        options.addOption("f", "from", true, "page number to start from, defaults to " + DEFAULT_FROM);
        options.addOption("t", "to", true, "page number to end to, defaults to " + DEFAULT_TO);
        options.addOption("p", "perpage", true, "number of items per page, defaults to " + DEFAULT_PERPAGE);
        options.addRequiredOption("c", "crawler", true, "crawler class to use (e.g. GramediaCrawler)");

        CommandLineParser cliParser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = cliParser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar BooksCrawler.jar", options);
            System.exit(1);
        }
        Class crawlerClass;
        Crawler crawler = null;
        try {
            crawlerClass = Class.forName("id.pascal.bookscrawler.models.crawlers." + cmd.getOptionValue("crawler"));
            crawler = (Crawler) crawlerClass.newInstance();
            crawler.setPageFrom(Integer.parseInt(cmd.getOptionValue("from", DEFAULT_FROM)));
            crawler.setPageTo(Integer.parseInt(cmd.getOptionValue("to", DEFAULT_TO)));
            crawler.setItemsPerPage(Integer.parseInt(cmd.getOptionValue("perpage", DEFAULT_PERPAGE)));
        } catch (ClassNotFoundException ex) {
            System.err.println("Crawler not available: " + cmd.getOptionValue("crawler"));
            System.exit(1);
        } catch (InstantiationException ex) {
            System.err.println("Internal error: " + ex);
            System.exit(1);
        } catch (IllegalAccessException ex) {
            System.err.println("Internal error: " + ex);
            System.exit(1);
        } catch (NumberFormatException ex) {
            System.err.println("Please fill in numbers where required: " + ex);
            System.exit(1);            
        }
        CSVBookEater eater;
        eater = new CSVBookEater("output.csv");
        try {
            crawler.crawl(eater);
        } catch (IOException ex) {
            System.err.println("Error occured while crawling: " + ex);
            System.exit(1);            
        }
    }
}
