package com.sayan.shelfwise;

import com.sayan.shelfwise.model.Book;
import com.sayan.shelfwise.model.Member;
import com.sayan.shelfwise.repository.BookRepository;
import com.sayan.shelfwise.repository.LoanRepository;
import com.sayan.shelfwise.repository.MemberRepository;
import java.sql.SQLException;
import com.sayan.shelfwise.service.BookService;
import com.sayan.shelfwise.service.LoanService;
import com.sayan.shelfwise.service.MemberService;
import java.util.List;
import com.sayan.shelfwise.model.Loan;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Main{
    public static void main(String[] args) throws SQLException{
        /*BookRepository bookRepo = new BookRepository();
        BookService bookService = new BookService(bookRepo);
        
        try {
            bookService.updateBookCopies(1, 5, 12);
            System.out.println("updateBookCopies test succeeded from service layer");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        
        List<Book> read= bookService.findAllBook();
        for (Book book : read) {
            System.out.println("ID: "+ book.getId());
            System.out.println("Title: " + book.getTitle());
            System.out.println("Total Copies: " + book.getTotalCopies());
            System.out.println("Available Copies: " + book.getAvailableCopies());
            System.out.println("====================================================");
            System.out.println("");
        }/*
        
        /*Book testBook = new Book("Service layer Test book", "Test author", "SW-SERVICE-001", 1, 1);
        try{
            bookService.addBook(testBook);
            System.out.println("Book added successfully");
        }catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }*/
        /*try {
            bookService.deleteBook(4);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }*/
        
        /*MemberRepository memberReopsitory = new MemberRepository();
        MemberService memberService = new MemberService(memberReopsitory);
        try {
            Member testMember = new Member("Aarav Sen","sayan.com");
            memberService.addMember(testMember);
            
            List<Member> read=memberService.findAll();
            for (Member member: read){
                System.out.println("Member ID: "+ member.getId());
                System.out.println("Member Name: "+ member.getName());
                System.out.println("Member Email: "+ member.getEmail());
                System.out.println("======================================");
                System.out.println("");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }*/
        
        
        LoanRepository loanRepository = new LoanRepository();
        LoanService loanService = new LoanService(loanRepository);
        
        /*Loan obj = new Loan(1, 1, LocalDate.now().plusDays(14));
        
        
        try {
            loanService.borrowBook(obj);
            System.out.println("Loan Succeeded");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }*/
        /*try{
            loanService.returnBook(1);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }*/
        
        
        try {
            Loan loan = new Loan(
                1,
                1,
                LocalDate.now().plusDays(14)
            );

            loanService.borrowBook(loan);
            System.out.println("Borrow successful");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        
        
    }
}