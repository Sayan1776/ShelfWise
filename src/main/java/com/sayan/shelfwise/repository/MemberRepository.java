/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.repository;

import com.sayan.shelfwise.database.DatabaseConnection;
import com.sayan.shelfwise.model.Member;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sayan
 */
public class MemberRepository {
    
    public void addMember(Member member)throws SQLException{
     String sql = "INSERT INTO members(name,email) "+ "VALUES (?, ?)";
     
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
            ){
            statement.setString(1, member.getName());
            statement.setString(2, member.getEmail());
            
            statement.executeUpdate();
        }
    }
    
     public List<Member> findAll() throws SQLException{
        String sql = """
                     SELECT id, name, email
                     FROM members
                     ORDER BY id ASC;""";
        List<Member> members = new ArrayList<>();
        
        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
            ){
            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                
                Member member = new Member(id, name, email);
                members.add(member);
            }
        }
        return members;
    }
}
