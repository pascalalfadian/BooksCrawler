/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package id.pascal.bookscrawler.models;

/**
 *
 * @author pascal
 */
public class Book implements Cloneable {

    private String title;
    private String publisher;
    private String author;
    private Integer year;
    private String lcClassificationCode;
    private String isbn;
    private String language;
    private Integer numOfPages;
    private float price;
    private String additionalDescription;    

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getLcClassificationCode() {
        return lcClassificationCode;
    }

    public void setLcClassificationCode(String lcClassificationCode) {
        this.lcClassificationCode = lcClassificationCode;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getNumOfPages() {
        return numOfPages;
    }

    public void setNumOfPages(Integer numOfPages) {
        this.numOfPages = numOfPages;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAdditionalDescription() {
        return additionalDescription;
    }

    public void setAdditionalDescription(String additionalDescription) {
        this.additionalDescription = additionalDescription;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException  { 
        return super.clone(); 
    } 
    
    @Override
    public String toString() {
        return "Book{" + "title=" + title + ", publisher=" + publisher + ", author=" + author + ", year=" + year + ", lcClassificationCode=" + lcClassificationCode + ", isbn=" + isbn + ", language=" + language + ", numOfPages=" + numOfPages + ", price=" + price + ", additionalDescription=" + additionalDescription + '}';
    }    
}
