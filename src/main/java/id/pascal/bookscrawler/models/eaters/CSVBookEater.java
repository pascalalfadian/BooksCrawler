/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler.models.eaters;

import id.pascal.bookscrawler.models.Book;
import id.pascal.bookscrawler.models.BookEater;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author pascal
 */
public class CSVBookEater implements BookEater {

    private final CSVPrinter csvPrinter;
    
    public CSVBookEater(String filename) throws IOException {
        new File(filename).delete();
        csvPrinter = new CSVPrinter(new FileWriter(filename), CSVFormat.EXCEL);
        csvPrinter.printRecord(
                "Title",
                "Publisher",
                "Author",
                "Year",
                "Subject(LC Classification Code)",
                "ISBN",
                "Language",
                "Num Of Pages",
                "Price",
                "Additional Description"
        );
    }
    
    @Override
    public void eat(Book book) {
        try {
            System.out.println("Writing to csv: " + book + "...");
            csvPrinter.printRecord(
                    book.getTitle(),
                    book.getPublisher(),
                    book.getAuthor(),
                    book.getYear(),
                    book.getLcClassificationCode(),
                    book.getIsbn(),
                    book.getLanguage(),
                    book.getNumOfPages(),
                    book.getPrice(),
                    book.getAdditionalDescription()
            );
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write CSV output");
        }
    }
    
    public void close() throws IOException {
        csvPrinter.close();
    }
}
