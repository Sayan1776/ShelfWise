/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sayan.shelfwise.service;

import com.sayan.shelfwise.model.Member;
import com.sayan.shelfwise.repository.MemberRepository;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author sayan
 */
public class MemberService {
    private final MemberRepository memberRepository;
    
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    
    public void addMember(Member member) throws SQLException{
        
        if (member==null){
            throw new IllegalArgumentException("Member cannot be null");
        }
        
        if (member.getName()==null || member.getName().isBlank()){
            throw new IllegalArgumentException("Member name cannot be Empty");
        }
        
        if (member.getEmail() == null || member.getEmail().isBlank()){
            throw new IllegalArgumentException("Member Email cannot be Empty");
        }
        if (!member.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        memberRepository.addMember(member);
    }
    
    public List<Member> findAll() throws SQLException{
        return memberRepository.findAll();
    }
}
