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
public class Report {
    private Student student;
    private Material material;
    private String reason, description, status;
    
    
    
    public Report(Student student, Material material, String reason, String description) {
        this.student = student; this.material = material; this.reason = reason; this.description = description; this.status = "pending";
    }
    
    
    @Override
    public String toString() { 
        return "[LAPORAN " + status.toUpperCase() + "] " + reason + " oleh " + student.getName() + " di materi: " + material.getTitle();
    }
}