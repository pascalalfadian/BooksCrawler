/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler.models.eaters;

import id.pascal.bookscrawler.models.Book;
import id.pascal.bookscrawler.models.BookEater;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author pascal
 */
public class CSVBookEater implements BookEater {

    private final CSVPrinter csvPrinter;

    public CSVBookEater(String filename) throws IOException {
        Files.deleteIfExists(Paths.get(filename));
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
                "Additional Description",
                "Extra_Image",
                "Extra_Remarks"
        );
    }

    @Override
    public void eat(Book book) {
        try {
            System.out.println("Processing: " + book + "...");
            String filename = this.saveImage(book.getImageURL());
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
                    book.getAdditionalDescription(),
                    filename,
                    book.getRemarks()
            );
            csvPrinter.flush();
        } catch (IOException ex) {
            throw new RuntimeException("Failed to write CSV output: " + ex);
        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        try {
            csvPrinter.close();
        } catch (IOException e) {
            // It's okay if it fails
        }
    }

    /**
     * Saves image from a URL to local file. Based on {@linkplain https://www.avajava.com/tutorials/lessons/how-do-i-save-an-image-from-a-url-to-a-file.html}
     * @param url URL of the image
     * @return the file name
     * @throws IOException 
     */
    private String saveImage(URL url) throws IOException {
        Path file = Paths.get(url.getFile()).getFileName();
        if (Files.exists(file, LinkOption.NOFOLLOW_LINKS)) {
            System.out.println(file + " exists, download skipped");
        } else {
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(file.toFile());

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        }
        
        return file.toString();
    }
}
