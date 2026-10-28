package com.example.Aivra_Backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class AdminDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String status;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }
}