/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.model;
import java.time.OffsetDateTime;
import java.time.LocalDate;
/**
 *
 * @author sayan
 */
public class Loan {
    private int id;
    private int bookId;
    private int memberId;
    private OffsetDateTime borrowedAt;
    private LocalDate dueDate;
    private OffsetDateTime returnedAt;
    
    public Loan(int bookId, int memberId, LocalDate dueDate){
        this.bookId = bookId;
        this.memberId = memberId;
        this.dueDate = dueDate;
    }
    
    public Loan(int id,int bookId, int memberId, OffsetDateTime borrowedAt, LocalDate dueDate, OffsetDateTime returnedAt){
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedAt = borrowedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
    }
    
    public int getId(){
        return id;
    }
    
    public int getBookId(){
        return bookId;
    }
    
    public int getMemberId(){
        return memberId;
    }
    
    public OffsetDateTime getBorrowedAt(){
        return borrowedAt;
    }
    
    public LocalDate getDueDate(){
        return dueDate;
    }
    
    public OffsetDateTime getReturnedAt(){
        return returnedAt;
    }
}
