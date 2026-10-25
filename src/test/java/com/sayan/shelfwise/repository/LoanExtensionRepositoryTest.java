/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.repository;
import com.sayan.shelfwise.database.DatabaseConnection;
import com.sayan.shelfwise.model.LoanExtensionRequest;
import com.sun.net.httpserver.Request;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




/**
 *
 * @author sayan
 */
public class LoanExtensionRepositoryTest {
    @Test
    void createRequestReturnsGeneratedId() throws SQLException {
        LoanExtensionRepository repository =
                new LoanExtensionRepository();
        
        LoanExtensionRequest request = new LoanExtensionRequest(
                7,
                LocalDate.of(2026, 11, 15),
                "Testing extension request creation"
        );

        int requestId = repository.createRequest(request);

        try {
            assertTrue(requestId > 0,
                    "The database should return a generated request ID.");

            System.out.println("Created extension request ID: " + requestId);
        } finally {
            deleteTestRequest(requestId);
        }
    }
    
    private void deleteTestRequest(int requestId) throws SQLException{
        String sql = "DELETE FROM loan_extension_requests WHERE id = ?";
        try(
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ){
            statement.setInt(1, requestId);
            statement.executeUpdate();
        }
    }
    
    @Test
    void rejectsSecondPendingRequestForSameLoan() throws SQLException {
        LoanExtensionRepository repository =
                new LoanExtensionRepository();

        LoanExtensionRequest firstRequest =
                new LoanExtensionRequest(
                        7,
                        LocalDate.of(2026, 11, 15),
                        "First duplicate-constraint test request"
                );

        LoanExtensionRequest duplicateRequest =
                new LoanExtensionRequest(
                        7,
                        LocalDate.of(2026, 11, 20),
                        "Second duplicate-constraint test request"
                );

        int firstId = repository.createRequest(firstRequest);
        int[] duplicateId = {0};

        try {
            SQLException exception = assertThrows(
                    SQLException.class,
                    () -> duplicateId[0] =
                            repository.createRequest(duplicateRequest)
            );

            assertEquals("23505", exception.getSQLState(),
                    "The duplicate should violate the unique constraint.");
        } finally {
            if (duplicateId[0] > 0) {
                deleteTestRequest(duplicateId[0]);
            }
            deleteTestRequest(firstId);
        }
    }
    
    @Test
    void findPendingRequestsReturnsCreatedRequest() throws SQLException {
        LoanExtensionRepository repository =
                new LoanExtensionRepository();

        int requestId = 0;

        try {
            LoanExtensionRequest request = new LoanExtensionRequest(
                    7,
                    LocalDate.of(2026, 11, 15),
                    "Testing extension request retrieval"
            );

            requestId = repository.createRequest(request);
            
            final int createdRequestId = requestId;

            List<LoanExtensionRequest> requests =
                    repository.findPendingRequests();

            boolean found = requests.stream()
                    .anyMatch(item -> item.getId() == createdRequestId);

            assertTrue(found,
                    "The pending requests should contain the newly created request.");

        } finally {
            if (requestId > 0) {
                deleteTestRequest(requestId);
            }
        }
    }
}
