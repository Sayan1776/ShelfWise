/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.service;

import com.sayan.shelfwise.model.Loan;
import com.sayan.shelfwise.repository.LoanRepository;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author sayan
 */
public class LoanService {
    
    private final LoanRepository loanRepository;
    public LoanService(LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }
    public void borrowBook(Loan loan) throws SQLException{
        if (loan == null){
            throw new IllegalArgumentException("Loan details are required");
        }
        
        if (loan.getBookId()<=0 || loan.getMemberId()<=0){
            throw new IllegalArgumentException("Both book ID and Member ID must be positive");
        }
        
        if (loan.getDueDate()==null){
            throw new IllegalArgumentException("A due date is required");
        }
        
        if (loan.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }
        
        loanRepository.borrowBook(loan);
    }
    
    public void returnBook(int loanId) throws SQLException{
        if (loanId<=0){
            throw new IllegalArgumentException("Invalid loan ID");
        }
        
        loanRepository.returnBook(loanId);
    }
}
