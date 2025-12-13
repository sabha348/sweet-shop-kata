package com.incubyte.sweetshop;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SweetshopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SweetshopApplication.class, args);
	}
	@RestController
    @CrossOrigin(origins = "http://localhost:5173") // <--- ADD THIS ANNOTATION
    class TestController {
        @GetMapping("/api/health")
        public Map<String, String> health() {
            return Map.of("status", "Backend is Active!");
        }
    }

}
