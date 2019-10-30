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
import java.util.List;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author pascal
 */
public class GramediaCrawler implements Crawler {

    @Override
    public void crawl(BookEater eater) throws IOException {
        Connection connection = Jsoup.connect("https://www.gramedia.com/api/products/?page=1&per_page=10&category=buku&based_on=new-arrival");
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
            JSONArray moreFormats = moreProduct.getJSONArray("formats");
            for (int j = 0; j < moreFormats.length(); j++) {
                JSONObject format = moreFormats.getJSONObject(j);
                try {
                    Book newBook = (Book)bookBase.clone();
                    newBook.setPrice(format.getInt("basePrice"));
                    newBook.setNumOfPages(Math.round(Float.parseFloat(format.getString("pages"))));
                    newBook.setIsbn(format.getString("isbn"));
                    newBook.setLanguage(format.getString("language"));
                    eater.eat(newBook);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException("Internal error: cannot clone object");
                }
            }
        }
    }

}
