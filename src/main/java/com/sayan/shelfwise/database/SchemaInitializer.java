/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.database;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;

/**
 *
 * @author sayan
 */
public class SchemaInitializer {
    public static void initialize() throws SQLException{
        String sql = null;
        try(InputStream input = SchemaInitializer.class.getResourceAsStream("/loans.sql")) {
            if (input == null){
                throw new SQLException("Could not find schema.sql on the classpath");
            }
            sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new SQLException("Could not read schema.sql. ", e);
        }
        
        if (sql.isBlank()){
            throw new SQLException("Schema.sql is empty");
        }
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            
        ){
           statement.executeUpdate(sql);
           System.out.print("Schema executed succesfully");
        }
        
    }
}
