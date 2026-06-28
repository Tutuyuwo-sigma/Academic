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
public class Student extends User {
    private int semester;
    public Student(String name, String email, String major, int semester) {
        super(name, email, "student", major); this.semester = semester;
    }
    public int getSemester() { 
        
        return semester; 
    
    }
    
    @Override
    public String getDashboardInfo() {
        return "Siswa: " + name + "\nProdi: " + major + " (Semester " + semester + ")";
    }
}