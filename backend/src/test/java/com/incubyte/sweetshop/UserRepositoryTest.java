package com.incubyte.sweetshop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void should_save_and_find_user_by_email() {
        User user = new User("save_me@incubyte.co", "secret","CUSTOMER");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("save_me@incubyte.co");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("save_me@incubyte.co");
        assertThat(foundUser.get().getRole()).isEqualTo("CUSTOMER");
    }
}