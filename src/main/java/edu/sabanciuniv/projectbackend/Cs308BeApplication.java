package edu.sabanciuniv.projectbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EntityScan(basePackages = "edu.sabanciuniv.projectbackend.models")
public class Cs308BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Cs308BeApplication.class, args);
    }

}
