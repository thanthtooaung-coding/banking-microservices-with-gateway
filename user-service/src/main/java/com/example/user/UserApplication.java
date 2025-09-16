package com.example.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class)
public class UserApplication {
  public static void main(String[] args) { SpringApplication.run(UserApplication.class, args); }
}
