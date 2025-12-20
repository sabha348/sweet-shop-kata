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
                // ID, Name, Price (Cents), Quantity, URL
                repository.save(new Sweet(null, "Kaju Katli", 25000L, 10, "https://tse2.mm.bing.net/th/id/OIP.dsg137QG7ZdCkd5Xm0QEZwHaHa?cb=ucfimgc2&w=1200&h=1200&rs=1&pid=ImgDetMain&o=7&rm=3"));
                repository.save(new Sweet(null, "Gulab Jamun", 15000L, 20, "https://upload.wikimedia.org/wikipedia/commons/8/88/Gulab_Jamun_1.jpg"));
                repository.save(new Sweet(null, "Jalebi", 10000L, 15, "https://upload.wikimedia.org/wikipedia/commons/1/1c/Jalebi_1.jpg"));
            }
        };
    }
}