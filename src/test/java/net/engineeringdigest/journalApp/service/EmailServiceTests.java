package net.engineeringdigest.journalApp.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;

    @Disabled
    @Test
    void testSendMail() {
        // email will be sent to adhayayna@gmail.com from anshayadhayayn@gmail.com
        emailService.sendEmail("adhayayna@gmail.com",
                "Testing Java mail sender",
                "Hola, How do you do ?");
    }
}
