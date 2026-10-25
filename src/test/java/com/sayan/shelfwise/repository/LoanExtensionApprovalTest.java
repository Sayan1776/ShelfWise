/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.repository;



import com.sayan.shelfwise.database.DatabaseConnection;
import com.sayan.shelfwise.model.LoanExtensionRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author sayan
 */
public class LoanExtensionApprovalTest {

    private final LoanExtensionRepository repository =
            new LoanExtensionRepository();

    @Test
    void approvePendingRequestUpdatesLoanAndRequest() throws SQLException {
        TestFixture fixture = createTestFixture(false);

        try {
            LocalDate requestedDueDate =
                    fixture.originalDueDate.plusDays(7);

            int requestId = repository.createRequest(
                    new LoanExtensionRequest(
                            fixture.loanId,
                            requestedDueDate,
                            "JUnit approval test"
                    )
            );

            repository.approveRequest(requestId);

            assertAll(
                    () -> assertEquals(
                            requestedDueDate,
                            findLoanDueDate(fixture.loanId)
                    ),
                    () -> assertEquals(
                            "APPROVED",
                            findRequestStatus(requestId)
                    ),
                    () -> assertNotNull(
                            findRequestReviewedAt(requestId)
                    )
            );

        } finally {
            cleanupTestFixture(fixture);
        }
    }

    @Test
    void approvingSameRequestTwiceShouldFail() throws SQLException {
        TestFixture fixture = createTestFixture(false);

        try {
            LocalDate requestedDueDate =
                    fixture.originalDueDate.plusDays(7);

            int requestId = repository.createRequest(
                    new LoanExtensionRequest(
                            fixture.loanId,
                            requestedDueDate,
                            "JUnit duplicate approval test"
                    )
            );

            // First approval should succeed.
            repository.approveRequest(requestId);

            // Second approval should fail.
            assertThrows(
                    SQLException.class,
                    () -> repository.approveRequest(requestId)
            );

            // The successful approval must remain unchanged.
            assertAll(
                    () -> assertEquals(
                            requestedDueDate,
                            findLoanDueDate(fixture.loanId)
                    ),
                    () -> assertEquals(
                            "APPROVED",
                            findRequestStatus(requestId)
                    ),
                    () -> assertNotNull(
                            findRequestReviewedAt(requestId)
                    )
            );

        } finally {
            cleanupTestFixture(fixture);
        }
    }

    @Test
    void approvingExtensionForReturnedLoanShouldRollback()
            throws SQLException {

        TestFixture fixture = createTestFixture(true);

        try {
            LocalDate requestedDueDate =
                    fixture.originalDueDate.plusDays(7);

            int requestId = repository.createRequest(
                    new LoanExtensionRequest(
                            fixture.loanId,
                            requestedDueDate,
                            "JUnit returned loan test"
                    )
            );

            assertThrows(
                    SQLException.class,
                    () -> repository.approveRequest(requestId)
            );

            // The due date must remain unchanged.
            assertEquals(
                    fixture.originalDueDate,
                    findLoanDueDate(fixture.loanId)
            );

            // The request must remain pending after rollback.
            assertAll(
                    () -> assertEquals(
                            "PENDING",
                            findRequestStatus(requestId)
                    ),
                    () -> assertNull(
                            findRequestReviewedAt(requestId)
                    )
            );

        } finally {
            cleanupTestFixture(fixture);
        }
    }

    @Test
    void approvingNonexistentRequestShouldFail() {
        assertThrows(
                SQLException.class,
                () -> repository.approveRequest(-1)
        );
    }

    // --------------------------------------------------
    // Test database setup
    // --------------------------------------------------

    private TestFixture createTestFixture(boolean alreadyReturned)
            throws SQLException {

        String token = UUID.randomUUID().toString();
        LocalDate originalDueDate = LocalDate.now().plusDays(7);

        int bookId;
        int memberId;
        int loanId;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                String insertBookSql = """
                        INSERT INTO books
                            (title, author, isbn, total_copies, available_copies)
                        VALUES (?, ?, ?, 1, 0)
                        RETURNING id
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(insertBookSql)) {

                    statement.setString(1, "JUnit Test Book " + token);
                    statement.setString(2, "ShelfWise Test");
                    statement.setString(3, "T-" + token.substring(0, 18));

                    bookId = readGeneratedId(statement);
                }

                String insertMemberSql = """
                        INSERT INTO members (name, email)
                        VALUES (?, ?)
                        RETURNING id
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(insertMemberSql)) {

                    statement.setString(1, "JUnit Test Member");
                    statement.setString(
                            2,
                            "shelfwise-test-" + token + "@example.com"
                    );

                    memberId = readGeneratedId(statement);
                }

                String insertLoanSql = """
                        INSERT INTO loans
                            (book_id, member_id, borrowed_at, due_date, returned_at)
                        VALUES (
                            ?, ?,
                            CURRENT_TIMESTAMP,
                            ?,
                            CASE WHEN ? THEN CURRENT_TIMESTAMP ELSE NULL END
                        )
                        RETURNING id
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(insertLoanSql)) {

                    statement.setInt(1, bookId);
                    statement.setInt(2, memberId);
                    statement.setObject(3, originalDueDate);
                    statement.setBoolean(4, alreadyReturned);

                    loanId = readGeneratedId(statement);
                }

                connection.commit();

            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }

                throw e;
            }
        }

        return new TestFixture(
                bookId,
                memberId,
                loanId,
                originalDueDate
        );
    }

    private int readGeneratedId(PreparedStatement statement)
            throws SQLException {

        try (ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                throw new SQLException(
                        "Test setup failed: no generated ID was returned."
                );
            }

            return resultSet.getInt("id");
        }
    }

    // --------------------------------------------------
    // Database verification helpers
    // --------------------------------------------------

    private LocalDate findLoanDueDate(int loanId) throws SQLException {
        String sql = """
                SELECT due_date
                FROM loans
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, loanId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Test loan was not found.");
                }

                return resultSet.getObject("due_date", LocalDate.class);
            }
        }
    }

    private String findRequestStatus(int requestId) throws SQLException {
        String sql = """
                SELECT status
                FROM loan_extension_requests
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, requestId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Test request was not found.");
                }

                return resultSet.getString("status");
            }
        }
    }

    private OffsetDateTime findRequestReviewedAt(int requestId)
            throws SQLException {

        String sql = """
                SELECT reviewed_at
                FROM loan_extension_requests
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, requestId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new SQLException("Test request was not found.");
                }

                return resultSet.getObject(
                        "reviewed_at",
                        OffsetDateTime.class
                );
            }
        }
    }

    // --------------------------------------------------
    // Test cleanup
    // --------------------------------------------------

    private void cleanupTestFixture(TestFixture fixture)
            throws SQLException {

        try (Connection connection = DatabaseConnection.getConnection()) {

            // Delete extension requests before deleting their loan.
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM loan_extension_requests WHERE loan_id = ?")) {

                statement.setInt(1, fixture.loanId);
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM loans WHERE id = ?")) {

                statement.setInt(1, fixture.loanId);
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM members WHERE id = ?")) {

                statement.setInt(1, fixture.memberId);
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM books WHERE id = ?")) {

                statement.setInt(1, fixture.bookId);
                statement.executeUpdate();
            }
        }
    }

    private static class TestFixture {

        private final int bookId;
        private final int memberId;
        private final int loanId;
        private final LocalDate originalDueDate;

        private TestFixture(
                int bookId,
                int memberId,
                int loanId,
                LocalDate originalDueDate) {

            this.bookId = bookId;
            this.memberId = memberId;
            this.loanId = loanId;
            this.originalDueDate = originalDueDate;
        }
    }
}