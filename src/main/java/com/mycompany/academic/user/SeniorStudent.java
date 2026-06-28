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
public class SeniorStudent extends User {
    private int uploadCount;
    
    public SeniorStudent(String name, String email, String major) {
        super(name, email, "senior_student", major); this.uploadCount = 0;
    }
    
    @Override
    public String getDashboardInfo() {
        return "Mentor: " + name + "\nJurusan: " + major + " (" + uploadCount + " Unggahan)";
    }
}