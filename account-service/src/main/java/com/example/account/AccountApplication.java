package com.example.account;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration.class)
public class AccountApplication {
  public static void main(String[] args){ SpringApplication.run(AccountApplication.class,args); }
}
