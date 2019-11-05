/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler;

import id.pascal.bookscrawler.models.Crawler;
import id.pascal.bookscrawler.models.crawlers.GramediaCrawler;
import id.pascal.bookscrawler.models.eaters.CSVBookEater;
import java.io.IOException;

/**
 *
 * @author pascal
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Crawler[] crawlers = {
            new GramediaCrawler()
        };
        CSVBookEater eater;
        eater = new CSVBookEater("output.csv");
        for (Crawler crawler: crawlers) {
            try {
                crawler.crawl(eater);
            } catch (IOException e) {
                System.err.println("WARNING: exception on " + crawler.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
    }
}
