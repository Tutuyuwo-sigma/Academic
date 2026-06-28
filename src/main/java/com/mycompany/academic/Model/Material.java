package com.mycompany.academic.Model;

import com.mycompany.academic.Model.Course;

public class Material {
    private int id;
    private Course course;
    private String title;
    private String filePath;
    private String tier;   
    private String status; 

 
    public Material(int id, Course course, String title, String filePath, String tier, String status) {
        this.id = id;
        this.course = course;
        this.title = title;
        this.filePath = filePath;
        this.tier = tier;
        this.status = status;
    }


    public int getId() { 
        return id; 
    }
    
    public Course getCourse() { 
        return course; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public String getFilePath() { 
        return filePath; 
    } 
    
    public String getTier() { 
        return tier; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setTier(String tier) { 
        this.tier = tier; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }

    @Override
    public String toString() {
        return "Materi: " + title + " [" + tier + "] - Status: " + status + " - Kelas: " + course.getTitle();
    }
}