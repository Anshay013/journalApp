package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.scheduler.UserScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserSchedulersTest {

    @Autowired
    UserScheduler userScheduler;

    @Test
    public void testFetchUsersAndSendEmail(){
        userScheduler.fetchUsersAndSendSaMail();
    }
}
