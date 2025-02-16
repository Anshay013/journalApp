package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

/*    @Autowired
    private EmailService emailService;

    // the method annotated with kafka runs all time to listen to the data the producer produced, As soon as it gets data within the topic the fun runs and send email
    // this is consumer fetching flow.
    @KafkaListener(topics = "foods", groupId = "foods-group")
    public void consume(SentimentData sentimentData) { // this sentimentData in the argument is de-serialized by JsonSerializer by broker before the consumer fetches it.
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData) {
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
        //   emailService.sendEmail("adhayayna@gmail.com, "Sentiment for previous week", sentimentData.getSentiment());
    }*/
}