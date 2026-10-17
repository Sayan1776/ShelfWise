/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.sayan.shelfwise;


import com.sayan.shelfwise.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnectionCheck {

    public static void DbConnectionCheck() {

        String sql = "SELECT current_database(), current_user";

        try (
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)
        ) {
            if (resultSet.next()) {
                System.out.println("Connection successful!");
                System.out.println(
                    "Database: " +
                    resultSet.getString("current_database")
                );
                System.out.println(
                    "Connected user: " +
                    resultSet.getString("current_user")
                );
            }

        } catch (SQLException e) {
            System.err.println("Connection test failed:");
            System.err.println(e.getMessage());
        }
    }
}