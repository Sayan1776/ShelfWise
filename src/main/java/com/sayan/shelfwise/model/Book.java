/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.model;

/**
 *
 * @author sayan
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int totalCopies;
    private int availableCopies;
    
    public Book(String title, String author, String isbn, int totalCopies, int availableCopies){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getAuthor(){
        return author;
    }
    
    public String getIsbn(){
        return isbn;
    }
    
    public int getTotalCopies(){
        return totalCopies;
    }
    
    public int getAvailableCopies(){
        return availableCopies;
    }
    
    public int getId(){
        return id;
    }
    
    public Book(int id,String title, String author, String isbn, int totalCopies, int availableCopies){
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public void add(Book book) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
