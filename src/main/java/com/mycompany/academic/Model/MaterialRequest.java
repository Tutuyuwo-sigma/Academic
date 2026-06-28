package com.mycompany.academic.Model;


import com.mycompany.academic.Model.Material;
import com.mycompany.academic.user.Student;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author user
 */
public class MaterialRequest {
    private int id;
    private Student student;
    private Material material;
    private String status; // 

    public MaterialRequest(int id, Student student, Material material, String status) {
        this.id = id;
        this.student = student;
        this.material = material;
        this.status = status;
    }

    public int getId() { 
        return id; 
    }
    
    public Student getStudent() { 
        return student; 
    }
    
    public Material getMaterial() { 
        return material; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
}