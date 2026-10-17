/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.model;

/**
 *
 * @author sayan
 */
public class Member {
    private int id;
    private String name;
    private String email;
    
    public Member(String name, String email){
        this.name = name;
        this.email = email;
    }
    
    public Member(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public String getEmail(){
        return email;
    }
}
