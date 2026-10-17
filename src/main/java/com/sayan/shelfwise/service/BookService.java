/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.service;

import com.sayan.shelfwise.repository.BookRepository;
import java.sql.SQLException;
import com.sayan.shelfwise.model.Book;
import java.util.List;

/**
 *
 * @author sayan
 */
public class BookService {
    private final BookRepository bookRepository;
    
    public BookService(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }
    
    public void updateBookCopies(
        int bookId,
        int totalCopies,
        int availableCopies
    ) throws SQLException {
        
        if (bookId<=0){
            throw new IllegalArgumentException("Book ID must be greater than 0");
        }
        
        if (totalCopies<0){
            throw new IllegalArgumentException("Total copies can not be negative");
        }
        
        if (availableCopies < 0){
            throw new IllegalArgumentException("Available copies cannot be negative");
        }
        
        if (availableCopies > totalCopies){
            throw new IllegalArgumentException("Available copies must be equal or less than total copies");
        }
        
        bookRepository.updateBookCopies(bookId, totalCopies, availableCopies);
    }
    
    public void addBook(Book book) throws SQLException{
        if (book==null){
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (book.getTotalCopies()<0){
            throw new IllegalArgumentException("Total copies can not be negative");
        }
        
        if (book.getAvailableCopies() < 0){
            throw new IllegalArgumentException("Available copies cannot be negative");
        }
        
        if (book.getAvailableCopies() > book.getTotalCopies()){
            throw new IllegalArgumentException("Available copies must be equal or less than total copies");
        }
        
        if (book.getTitle()==null || book.getTitle().isBlank()){
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor()==null || book.getAuthor().isBlank()){
            throw new IllegalArgumentException("Book author cannot be empty");
        }
        if (book.getIsbn()==null || book.getIsbn().isBlank()){
            throw new IllegalArgumentException("Book ISBN cannot be empty");
        }
        
        
        bookRepository.addBook(book);
    }
    
    public List<Book> findAllBook() throws SQLException{
        return bookRepository.findAll();
    }
    
    public void deleteBook(int bookId) throws SQLException {
        if (bookId<=0){
            throw new IllegalArgumentException("Book ID must be greater than zero(ex: 1,2,3,..)");
        }
        
        bookRepository.deleteBook(bookId);
    }
}
