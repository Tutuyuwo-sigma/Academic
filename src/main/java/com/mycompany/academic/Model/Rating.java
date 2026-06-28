/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.academic.Model;

import com.mycompany.academic.Model.Material;
import com.mycompany.academic.user.Student;

/**
 *
 * @author user
 */
public class Rating {
    private Student student;
    private Material material;
    private int ratingValue;
    private String review;
    
    
    public Rating(Student student, Material material, int ratingValue, String review) {
        this.student = student; this.material = material; this.ratingValue = ratingValue; this.review = review;
    }
    
    @Override
    public String toString() { 
        return "★ " + ratingValue + " oleh " + student.getName() + ": \"" + review + "\"";
    }
}