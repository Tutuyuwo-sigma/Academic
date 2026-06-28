package com.mycompany.academic.Model;

import com.mycompany.academic.user.SeniorStudent;
import com.mycompany.academic.user.Student;

public class CustomRequest {
    private int id;
    private Student student;
    private SeniorStudent mentor; 
    private String title;
    private String description;
    private int budget;
    private String status; 

    public CustomRequest(int id, Student student, String title, String description, int budget) {
        this.id = id;
        this.student = student;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.status = "open";
        this.mentor = null;
    }

    public int getId() { 
        return id; 
    }
    
    public Student getStudent() { 
        return student; 
    }
    
    public SeniorStudent getMentor() { 
        return mentor; 
    }
    
    public void setMentor(SeniorStudent mentor) { 
        this.mentor = mentor; 
    }
    
    public String getTitle() { 
        return title; 
    }
    
    public String getDescription() { 
        return description; 
    }
    
    public int getBudget() { 
        return budget; 
    }
    
    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    @Override
    public String toString() {
        return "Request #" + id + ": " + title + " (Budget: Rp " + budget + ") [" + status.toUpperCase() + "]";
    }
}