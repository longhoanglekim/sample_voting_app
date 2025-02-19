package com.example.worker.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "votes")
public class Vote {
    @Id
    private String voterId;
    private String choice;
}
