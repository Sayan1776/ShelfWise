/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.repository;

import com.sayan.shelfwise.database.DatabaseConnection;
import com.sayan.shelfwise.model.Loan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author sayan
 */
public class LoanRepository {
    public void borrowBook(Loan loan) throws SQLException{
        String sql1 ="""
                     UPDATE books
                     SET available_copies = available_copies - 1
                     WHERE id = ? AND available_copies > 0
                     """;
        
        String sql2="""
                   INSERT INTO loans (book_id, member_id, due_date)
                   VALUES (?, ?, ?)
                   """;
        
        try (
                Connection connection = DatabaseConnection.getConnection();
            ){
            connection.setAutoCommit(false);
            try(
                    PreparedStatement statement1 = connection.prepareStatement(sql1);
                    PreparedStatement statement2 = connection.prepareStatement(sql2);
                ){
                statement1.setInt(1, loan.getBookId());
                
                int rowsUpdated1 = statement1.executeUpdate();
                if (rowsUpdated1==0){
                    throw new SQLException("Book not found or no copies available. ID: "+ loan.getBookId());
                }
                
                statement2.setInt(1, loan.getBookId());
                statement2.setInt(2, loan.getMemberId());
                statement2.setObject(3, loan.getDueDate());
                
                int rowsUpdated2 = statement2.executeUpdate();
                if (rowsUpdated2==0){
                    throw new SQLException("loan table update failed");
                }
                
                connection.commit();
            }catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    if (rollbackException != e) {
                        e.addSuppressed(rollbackException);
                    }
                }

                throw e;
            }
        } 
    }
    
    public void returnBook(int loanId) throws SQLException{
        String sql1 ="""
                     UPDATE loans
                     SET returned_at = CURRENT_TIMESTAMP
                     WHERE id = ? AND returned_at IS NULL
                     RETURNING book_id
                     """;
        
        String sql2="""
                    UPDATE books
                    SET available_copies = available_copies + 1
                    WHERE id = ?
                    """;
        
        try (
                Connection connection = DatabaseConnection.getConnection();
                
            ){
            connection.setAutoCommit(false);
            try(PreparedStatement Statement1 = connection.prepareStatement(sql1);
                PreparedStatement Statement2 = connection.prepareStatement(sql2)){
                
                Statement1.setInt(1, loanId);

                int bookId;
                try(ResultSet resultSet = Statement1.executeQuery();){
                    
                    if (resultSet.next()){
                        bookId = resultSet.getInt("book_id");
                    }else{
                        throw new SQLException("Loan not found or already returned");
                    }
                }

                Statement2.setInt(1, bookId);
                int rowsUpdated = Statement2.executeUpdate();
                if (rowsUpdated != 1){
                    throw new SQLException("update of table book failed");
                }
                connection.commit();
            }catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    if (rollbackException != e) {
                        e.addSuppressed(rollbackException);
                    }
                }

                throw e;
            }
        }
    }
}
