/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.academic.Model;

/**
 *
 * @author user
 */
public class Course {
    private int id, semester;
    private String title, major;
    
    
    public Course(int id, String title, String major, int semester) {
        this.id = id; this.title = title; this.major = major; this.semester = semester;
    }
    public String getTitle() { 
        
        return title; 
    
    }
}