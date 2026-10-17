/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.database;

/**
 *
 * @author sayan
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class DatabaseConnection {
    public static Connection getConnection() throws SQLException{
        Properties config = new Properties();
        
        try(InputStream input = Files.newInputStream(Path.of("shelfwise.properties"))){
            config.load(input);
        }catch(IOException e){
            throw new SQLException(
            "Could not read shelfwise.properties" + "Check if that file is in the project root", e
            );
        }
        
        String url = config.getProperty("db.url");
        String password = config.getProperty("db.password");
        String user = config.getProperty("db.user");
        
        if (url==null || url.isBlank() || user==null || user.isBlank() || password==null || password.isBlank()){
            throw new SQLException("Database configuration is incomplete");
        }
        
        Properties credentials = new Properties();
        credentials.setProperty("user",user);
        credentials.setProperty("password",password);
        
        
        return DriverManager.getConnection(url, credentials);
    }
    
}

