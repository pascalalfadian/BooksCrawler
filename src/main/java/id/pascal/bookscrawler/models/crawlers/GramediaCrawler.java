/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler.models.crawlers;

import id.pascal.bookscrawler.models.Crawler;
import id.pascal.bookscrawler.models.Book;
import id.pascal.bookscrawler.models.BookEater;
import java.io.IOException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 *
 * @author pascal
 */
public class GramediaCrawler extends Crawler {

    @Override
    public void crawl(BookEater eater) throws IOException {
        for (int page = pageFrom; page <= pageTo; page++) {
            System.err.printf("Crawling page %d out of (%d..%d)\n", page, pageFrom, pageTo);
            Connection connection = Jsoup.connect(
                    String.format(
                            "https://www.gramedia.com/api/products/?page=%d&per_page=%d&category=buku&based_on=new-arrival",
                            pageFrom,
                            itemsPerPage
                    )
            );
            connection.ignoreContentType(true);
            Response response = connection.execute();
            JSONArray products = new JSONArray(response.body());
            for (int i = 0; i < products.length(); i++) {
                // Basic book information
                JSONObject product = products.getJSONObject(i);
                String nameString = product.getString("name");
                StringBuilder authorsString = new StringBuilder();
                JSONArray authors = product.getJSONArray("authors");
                for (int j = 0; j < authors.length(); j++) {
                    JSONObject author = authors.getJSONObject(j);
                    if (j > 0) {
                        authorsString.append(", ");
                    }
                    authorsString.append(author.getString("title"));
                }
                Book bookBase = new Book(nameString, authorsString.toString());
                // More book information
                String href = product.getString("href");
                Connection moreConnection = Jsoup.connect(href);
                moreConnection.ignoreContentType(true);
                Response moreResponse = moreConnection.execute();
                JSONObject moreProduct = new JSONObject(moreResponse.body());
                // Parse image
                bookBase.setImageURL(new URL(moreProduct.getString("thumbnail")));
                // Get description
                bookBase.setAdditionalDescription(moreProduct.getString("description"));
                // Parse categories
                JSONArray moreCategories = moreProduct.getJSONArray("categories");
                StringBuilder bookRemarks = new StringBuilder("Kategori: ");
                for (int j = 0; j < moreCategories.length(); j++) {
                    JSONObject category = moreCategories.getJSONObject(j);
                    if (j > 0) {
                        bookRemarks.append(", ");
                    }
                    bookRemarks.append(category.getString("title"));
                }
                // Parse formats
                JSONArray moreFormats = moreProduct.getJSONArray("formats");
                for (int j = 0; j < moreFormats.length(); j++) {
                    JSONObject format = moreFormats.getJSONObject(j);
                    try {
                        Book newBook = (Book) bookBase.clone();
                        newBook.setPrice(format.getInt("basePrice"));
                        newBook.setNumOfPages(Math.round(Float.parseFloat(format.getString("pages"))));
                        newBook.setIsbn(format.getString("isbn"));
                        newBook.setLanguage(format.getString("language"));
                        newBook.setRemarks(bookRemarks.toString());
                        eater.eat(newBook);
                    } catch (CloneNotSupportedException e) {
                        throw new RuntimeException("Internal error: cannot clone object");
                    }
                }
            }
        }
    }

}
