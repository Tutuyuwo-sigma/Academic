package com.mycompany.academic.user;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public abstract class User {
    protected String name, email, role, major;
    public User(String name, String email, String role, String major) {
        this.name = name; this.email = email; this.role = role; this.major = major;
    }
    public String getName() { 
        return name; 
    }
    
    public String getEmail() { 
        return email; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public String getMajor() { 
        return major; 
    }
    
    public abstract String getDashboardInfo(); 
}