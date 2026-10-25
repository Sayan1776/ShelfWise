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
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main{
    
    public static void main(String[] args) throws SQLException{
        Scanner sc = new Scanner(System.in);
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
        
        
        try {
            
            LocalDate defaultDueDate = LocalDate.now().plusDays(14);
            System.out.println("Default due date: "+ defaultDueDate);
            System.out.print("Enter a different due date "+"or press Enter to accept the default: ");
            
            String requestedDueDate = sc.nextLine().trim();
            LocalDate dueDate = requestedDueDate.isEmpty()
                    ? defaultDueDate
                    :LocalDate.parse(requestedDueDate);
            Loan loan = new Loan(
                1,
                1,
                dueDate
            );

            System.out.println("LoanID: " + loanService.borrowBook(loan));
            System.out.println("Borrow successful");
            
            System.out.println("Due date: "+ dueDate);
        }catch(DateTimeParseException e){
            System.out.println("Invalid date format. Use YYYY-MM-DD");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        
        
    }
}