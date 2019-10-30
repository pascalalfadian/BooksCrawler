/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler.models;

import java.io.IOException;

public interface Crawler {
    public void crawl(BookEater eater) throws IOException;
}
