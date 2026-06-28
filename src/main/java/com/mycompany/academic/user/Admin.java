/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.academic.user;

import com.mycompany.academic.user.User;

/**
 *
 * @author user
 */
public class Admin extends User {
    public Admin(String name, String email) { 
        
        super(name, email, "admin", "Sistem"); 
    
    }
    
    @Override
    public String getDashboardInfo() {
        return "Administrator Terautentikasi.\nSelamat bekerja mengelola konten ruang belajar.";
    }
}