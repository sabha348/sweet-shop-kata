package com.incubyte.sweetshop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(SweetRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Sweet(null, "Kaju Katli", 250.0, "https://upload.wikimedia.org/wikipedia/commons/3/3c/Kaju_Katli.jpg"));
                repository.save(new Sweet(null, "Gulab Jamun", 150.0, "https://upload.wikimedia.org/wikipedia/commons/8/88/Gulab_Jamun_1.jpg"));
                repository.save(new Sweet(null, "Jalebi", 100.0, "https://upload.wikimedia.org/wikipedia/commons/1/1c/Jalebi_1.jpg"));
            }
        };
    }
}