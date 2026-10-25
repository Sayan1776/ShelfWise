/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.repository;

import com.sayan.shelfwise.database.DatabaseConnection;
import com.sayan.shelfwise.model.ExtensionStatus;
import com.sayan.shelfwise.model.LoanExtensionRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sayan
 */
public class LoanExtensionRepository  {
    public int createRequest(LoanExtensionRequest loanExtensionRequest) throws SQLException{
        int id;
        String sql = """
                     INSERT INTO loan_extension_requests(loan_id, requested_due_date, reason) 
                     VALUES (?, ?, ?)
                     RETURNING id
                     """;
        
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
            ){
           statement.setInt(1, loanExtensionRequest.getLoanId());
           statement.setObject(2, loanExtensionRequest.getRequestedDueDate());
           statement.setString(3, loanExtensionRequest.getReason());
           
           try(ResultSet resultSet = statement.executeQuery()){
               if (resultSet.next()){
                   id = resultSet.getInt("id");
               }else {
                   throw new SQLException("Failed to create loan extension request: no ID was returned.");
               }
           }
        }
        return id;
    }
    
    public List<LoanExtensionRequest> findPendingRequests() throws SQLException{
        String sql = """
                     SELECT
                         id,
                         loan_id,
                         requested_due_date,
                         requested_at,
                         status,
                         reviewed_at,
                         withdrawn_at,
                         reason
                     FROM loan_extension_requests
                     WHERE status = 'PENDING'
                     ORDER BY requested_at ASC;
                     """;
        List<LoanExtensionRequest> loanExtensionRequests = new ArrayList<>();
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                // Read the current row's columns.
                int id = resultSet.getInt("id");
                int loanId = resultSet.getInt("loan_id");
                LocalDate requestedDueDate = resultSet.getObject("requested_due_date", LocalDate.class);
                OffsetDateTime requestedAt = resultSet.getObject("requested_at", OffsetDateTime.class);
                ExtensionStatus status = ExtensionStatus.valueOf(resultSet.getString("status"));
                OffsetDateTime reviewedAt = resultSet.getObject("reviewed_at", OffsetDateTime.class);
                OffsetDateTime withdrawnAt = resultSet.getObject("withdrawn_at", OffsetDateTime.class);
                String reason = resultSet.getString("reason");
                // Construct a LoanExtensionRequest.
                LoanExtensionRequest loanExtensionRequest = new LoanExtensionRequest(id, loanId, requestedDueDate, requestedAt, status, reviewedAt, withdrawnAt, reason);
                // Add it to loanExtensionRequests.
                loanExtensionRequests.add(loanExtensionRequest);
            }
        }

        return loanExtensionRequests;
        
    }
}
