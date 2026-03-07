package com.maplewood.model;

import jakarta.persistence.*;

@Entity
@Table(name = "semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="is_active")
    private Boolean isActive;

    public Semester() {}

    public Long getId() { return id; }

    @Column(name = "name")
    private String name;

    public String getName() { return name; }
    
    public Boolean getIsActive() { return isActive; }
    
}