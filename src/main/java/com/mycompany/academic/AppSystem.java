package com.mycompany.academic;

import com.mycompany.academic.Model.Report;
import com.mycompany.academic.Model.Rating;
import com.mycompany.academic.security.AccessControl;
import com.mycompany.academic.exception.AuthenticationException;
import com.mycompany.academic.Model.CustomRequest;
import com.mycompany.academic.Model.MaterialRequest;
import com.mycompany.academic.Model.Material;
import com.mycompany.academic.Model.Course;
import com.mycompany.academic.user.SeniorStudent;
import com.mycompany.academic.user.Admin;
import com.mycompany.academic.user.Student;
import com.mycompany.academic.user.User;
import java.util.ArrayList;
import java.util.HashMap;

public class AppSystem implements AccessControl {
    private HashMap<String, User> userDatabase = new HashMap<>();
    private HashMap<String, String> passwordDatabase = new HashMap<>();
    private ArrayList<Material> materials = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<CustomRequest> customRequests = new ArrayList<>();
    private ArrayList<MaterialRequest> premiumRequests = new ArrayList<>(); 
    private ArrayList<Rating> ratings = new ArrayList<>(); 
    private ArrayList<Report> reports = new ArrayList<>(); 

    public AppSystem() {
        seedData();
    }

    private void seedData() {
        userDatabase.put("admin@gmail.com", new Admin("Admin Utama", "admin@gmail.com"));
        passwordDatabase.put("admin@gmail.com", "123);
        
        Student s = new Student("asep67 ", "student@gmail.com", "RPL", 1);
        userDatabase.put("student@gmail.com", s);
        passwordDatabase.put("student@gmail.com", "123");
        
        SeniorStudent senior = new SeniorStudent("Senior Budi", "senior@gmail.com", "RPL");
        userDatabase.put("senior@gmail.com", senior);
        passwordDatabase.put("senior@gmail.com", "123");

        Course rpl1 = new Course(1, "Dasar Pemrograman", "RPL", 1);
        Course rpl3 = new Course(2, "OOP Java", "RPL", 3);
        courses.add(rpl1);
        courses.add(rpl3);

        materials.add(new Material(1, rpl1, "Alpro", "materials/flowchart.pdf", "Free", "published"));
        materials.add(new Material(2, rpl3, "Polimorfrisme oop", "materials/oop_presentation.pptx", "Premium", "published"));
        materials.add(new Material(3, rpl1, "Arsitektur organiasi komputer", "materials/project_structure.png", "Free", "published"));

        customRequests.add(new CustomRequest(1, s, "Butuh Modul Belajar Alpro", "Panduan instalasi SDK ", 15000));
    }

    public User login(String email, String password) throws AuthenticationException {
        if (!userDatabase.containsKey(email)) {
            throw new AuthenticationException("Email tidak terdaftar!");
        }
        String correctPassword = passwordDatabase.get(email);
        if (correctPassword == null || !correctPassword.equals(password)) {
            throw new AuthenticationException("Kata sandi salah!");
        }
        return userDatabase.get(email);
    }

    public void register(String name, String email, String password, String role, String major, int semester) throws Exception {
        if (userDatabase.containsKey(email)) {
            throw new Exception("Email sudah terdaftar!");
        }
        User newUser = role.equals("Siswa (Student)") ? new Student(name, email, major, semester) : new SeniorStudent(name, email, major);
        userDatabase.put(email, newUser);
        passwordDatabase.put(email, password);
    }

    public void requestPremium(Student s, Material m) {
        premiumRequests.add(new MaterialRequest(premiumRequests.size() + 1, s, m, "unpaid"));
    }

    public void payPremium(Student s, Material m) {
        for (MaterialRequest req : premiumRequests) {
            if (req.getStudent().getEmail().equals(s.getEmail()) && req.getMaterial().getId() == m.getId()) {
                req.setStatus("approved");
                break;
            }
        }
    }

    public String getPremiumRequestStatus(User user, Material material) {
        for (MaterialRequest req : premiumRequests) {
            if (req.getStudent().getEmail().equals(user.getEmail()) && req.getMaterial().getId() == material.getId()) {
                return req.getStatus();
            }
        }
        return null;
    }

    // Simpan Rating & Report
    public void submitRating(Student s, Material m, int val, String rev) {
        ratings.removeIf(r -> r.toString().contains(s.getName()) && r.toString().contains(m.getTitle())); 
        ratings.add(new Rating(s, m, val, rev));
    }

    public void submitReport(Student s, Material m, String reason, String desc) {
        reports.add(new Report(s, m, reason, desc));
    }

    public void addMaterial(Course course, String title, String tier, String fileType) {
        int nextId = materials.size() + 1;
        String ext = fileType.equals("PDF") ? ".pdf" : fileType.equals("PowerPoint") ? ".pptx" : ".png";
        materials.add(new Material(nextId, course, title, "materials/new_file" + ext, tier, "pending"));
    }

    public void publishMaterial(int id, String tier) {
        for (Material m : materials) {
            if (m.getId() == id) {
                m.setStatus("published");
                m.setTier(tier);
                break;
            }
        }
    }

    public void deleteMaterial(int id) {
        materials.removeIf(m -> m.getId() == id);
    }

    public void createCustomRequest(Student student, String title, String description, int budget) {
        customRequests.add(new CustomRequest(customRequests.size() + 1, student, title, description, budget));
    }

    public void claimRequest(int id, SeniorStudent mentor) {
        for (CustomRequest req : customRequests) {
            if (req.getId() == id) {
                req.setMentor(mentor);
                req.setStatus("unpaid");
                break;
            }
        }
    }

    public void payRequest(int id) {
        for (CustomRequest req : customRequests) {
            if (req.getId() == id) {
                req.setStatus("paid");
                break;
            }
        }
    }

    public ArrayList<CustomRequest> getCustomRequests() { return customRequests; }
    public ArrayList<Rating> getRatings() { 
        return ratings; 
    }
    
    public ArrayList<Report> getReports() { 
        return reports; 
    }
    
    public ArrayList<Course> getCourses() { 
        return courses; 
    }

    @Override
    public boolean hasAccess(User user, Material material) {
        if (material.getTier().equals("Free")) return true;
        if (user.getRole().equals("admin") || user.getRole().equals("senior_student")) return true;
        
        for (MaterialRequest req : premiumRequests) {
            if (req.getStudent().getEmail().equals(user.getEmail()) && 
                req.getMaterial().getId() == material.getId() && 
                req.getStatus().equals("approved")) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Material> getPublishedMaterials() {
        ArrayList<Material> published = new ArrayList<>();
        for (Material m : materials) {
            if (m.getStatus().equals("published")) published.add(m);
        }
        return published;
    }

    public ArrayList<Material> getPendingMaterials() {
        ArrayList<Material> pending = new ArrayList<>();
        for (Material m : materials) {
            if (m.getStatus().equals("pending")) pending.add(m);
        }
        return pending;
    }

    void checkAccess(User user, Material item) {
        throw new UnsupportedOperationException("Not supported yet."); 
}
}
