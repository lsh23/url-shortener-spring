package com.example.urlshortener.domain.auth.domain;

import jakarta.persistence.*;

@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "uuid", unique = true)
    private String uuid;

    public String getUUID() {
        return uuid;
    }

    public void assignUUID(String uuid) {
        this.uuid = uuid;
    }
}
