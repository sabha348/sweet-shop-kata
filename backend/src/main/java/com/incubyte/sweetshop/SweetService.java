package com.incubyte.sweetshop;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SweetService {
    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    public Sweet addSweet(Sweet sweet) {
        return sweetRepository.save(sweet);
    }

    public Sweet updateSweet(Long id, Sweet updatedSweet) {
        Sweet existingSweet = sweetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sweet not found with id: " + id));

        // Update all fields except ID
        existingSweet.setName(updatedSweet.getName());
        existingSweet.setPrice(updatedSweet.getPrice());       // Updates Long
        existingSweet.setQuantity(updatedSweet.getQuantity()); // Updates Integer
        existingSweet.setImageUrl(updatedSweet.getImageUrl());
        existingSweet.setCategory(updatedSweet.getCategory());

        return sweetRepository.save(existingSweet);
    }

    public void deleteSweet(Long id) {
        if (!sweetRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Sweet not found with id: " + id);
        }
        sweetRepository.deleteById(id);
    }
}