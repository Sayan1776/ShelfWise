/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.repository;

import com.sayan.shelfwise.database.DatabaseConnection;
import java.sql.SQLException;
import com.sayan.shelfwise.model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sayan
 */
public class BookRepository {
    
    public void addBook(Book book)throws SQLException{
     String sql = "INSERT INTO books(title,author,isbn,total_copies,available_copies)"+ "VALUES (?, ?, ?, ?, ?)";
     
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ){
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getTotalCopies());
            statement.setInt(5, book.getAvailableCopies());
            
            statement.executeUpdate();
        }
    }
    
    public List<Book> findAll() throws SQLException{
        String sql = """
                     SELECT id, title, author, isbn, total_copies, available_copies
                     FROM books
                     ORDER BY id ASC;""";
        List<Book> books = new ArrayList<>();
        
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
            ){
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String isbn = resultSet.getString("isbn");
                int totalCopies = resultSet.getInt("total_copies");
                int availableCopies = resultSet.getInt("available_copies");
                
                Book book = new Book(id, title, author, isbn, totalCopies, availableCopies);
                books.add(book);
            }
        }
        return books;
    }
    
    public void updateBookCopies(int bookId, int totalCopies, int availableCopies) throws SQLException{
        String sql = "UPDATE books SET total_copies = ?, available_copies = ? WHERE id = ?";
        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
            ){
            statement.setInt(1, totalCopies);
            statement.setInt(2, availableCopies);
            statement.setInt(3, bookId);
            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated==0){
                throw new SQLException("No book was found for the supplied ID: " + bookId);
            }
        }
    }
    
    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books where id=?";
        
        try(
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ){
            statement.setInt(1, bookId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted==0){
                throw new SQLException("No book was found for the supplied ID: " + bookId);
            }
        }
        
    }
}
