/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler.models.eaters;

import id.pascal.bookscrawler.models.Book;
import id.pascal.bookscrawler.models.BookEater;

/**
 *
 * @author pascal
 */
public class ConsoleBookEater implements BookEater {

    @Override
    public void eat(Book book) {
        System.out.println(book);
    }
    
}
