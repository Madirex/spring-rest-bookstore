package com.nullers.restbookstore.rest.users.repositories;

import com.nullers.restbookstore.rest.user.model.User;
import com.nullers.restbookstore.rest.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final User user = User.builder()
            .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
            .name("user")
            .surnames("test")
            .password("tests")
            .username("test")
            .email("user@user.com")
            .isDeleted(false)
            .build();
    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        userRepository.save(user);
    }
    @Test
    void findByUsername(){
        Optional<User> findByUsername = userRepository.findByUsername(user.getUsername());

        assertEquals(findByUsername.get().getUsername(), user.getUsername());
    }

    @Test
    void findByEmail(){
        Optional<User> findByEmail = userRepository.findByEmail(user.getEmail());

        assertEquals(findByEmail.get().getEmail(), user.getEmail());
    }

   @Test
   void findByUsernameEqualsIgnoreCase(){
         Optional<User> findByUsernameEqualsIgnoreCase = userRepository.findByUsernameEqualsIgnoreCase(user.getUsername());

         assertEquals(findByUsernameEqualsIgnoreCase.get().getUsername(), user.getUsername());
   }

   @Test
    void findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(){
        Optional<User> findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase = userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(user.getUsername(), user.getEmail());

        assertEquals(findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase.get().getUsername(), user.getUsername());
    }

    @Test
    void findAllByUsernameContainingIgnoreCase(){
        var findAllByUsernameContainingIgnoreCase = userRepository.findAllByUsernameContainingIgnoreCase(user.getUsername());

        assertEquals(findAllByUsernameContainingIgnoreCase.get(0).getUsername(), user.getUsername());
    }

}
