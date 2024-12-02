package com.bugvictims.demo11;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "http://localhost:5175")
public class Demo11Application
{

    public static void main(String[] args)
    {
        SpringApplication.run(Demo11Application.class, args);
    }
}
