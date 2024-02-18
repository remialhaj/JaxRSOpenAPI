package fr.istic.taa.jaxrs.domain;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
