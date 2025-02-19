package com.example.worker;

import com.example.worker.models.Vote;
import com.example.worker.services.DatabaseService;
import com.example.worker.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class WorkerApplication {


    public static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
    }

}
