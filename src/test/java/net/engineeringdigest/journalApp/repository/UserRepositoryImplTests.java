package net.engineeringdigest.journalApp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImplTests {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private UserRepository repository;

    @Test
    public void testSaveNewUser(){
        userRepository.getUserForSA("Kamado");
    }

    @Test
    public void searchUsersByEmail() {
        repository.findByEmailPattern("put any desired regex pattern");
    }
}