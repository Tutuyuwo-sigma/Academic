/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.academic.security;

import com.mycompany.academic.Model.Material;
import com.mycompany.academic.user.User;

/**
 *
 * @author user
 */
public interface AccessControl {
    boolean hasAccess(User user, Material material);
}