package com.poc3;
import org.springframework.boot.*; import org.springframework.boot.autoconfigure.*; import org.springframework.web.bind.annotation.*;
@SpringBootApplication @RestController public class App {
 public static void main(String[] a){SpringApplication.run(App.class,a);}
 @GetMapping("/health") public String health(){return "POC-3 healthy";}
}