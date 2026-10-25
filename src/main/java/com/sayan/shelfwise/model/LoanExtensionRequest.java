/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 *
 * @author sayan
 */
public class LoanExtensionRequest {
    private int id;
    private int loanId;
    private LocalDate requestedDueDate;
    private ExtensionStatus status;
    private OffsetDateTime requestedAt;
    private OffsetDateTime reviewedAt;
    private OffsetDateTime withdrawnAt;
    private String reason;
    
    public LoanExtensionRequest(int loanId, LocalDate requestedDueDate, String reason){
        this.loanId = loanId;
        this.requestedDueDate = requestedDueDate;
        this.reason = reason;
        this.status = ExtensionStatus.PENDING;
    }
    
    public LoanExtensionRequest (int id, int loanId, LocalDate requestedDueDate,OffsetDateTime requestedAt, ExtensionStatus status ,OffsetDateTime reviewedAt, OffsetDateTime withdrawnAt, String reason){
        this.id = id;
        this.loanId = loanId;
        this.requestedDueDate = requestedDueDate;
        this.status = status;
        this.requestedAt = requestedAt;
        this.reviewedAt = reviewedAt;
        this.withdrawnAt = withdrawnAt;
        this.reason = reason;
    }
    
    public int getId(){
        return id;
    }
    
    public int getLoanId(){
        return loanId;
    }
    
    public LocalDate getRequestedDueDate(){
        return requestedDueDate;
    }
    
    public ExtensionStatus getStatus(){
        return status;
    }
    
    public OffsetDateTime getRequestedAt(){
        return requestedAt;
    }
    
    public OffsetDateTime getReviewedAt(){
        return reviewedAt;
    }
    
    public OffsetDateTime getWithdrawnAt(){
        return withdrawnAt;
    }
    
    public String getReason(){
        return reason;
    }
}
