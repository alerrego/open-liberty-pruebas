package com.alito.rest.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true,nullable=false)
    private String email;

    @Column(nullable=false)
    @JsonbTransient
    private String password;

    @Column(nullable=false)
    private String role;

    public User(){}

    public User(String email,String password,String role){
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRol() { return role; }
    public void setRol(String role) { this.role = role; }
}
