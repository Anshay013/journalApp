package net.engineeringdigest.journalApp.scheduler;

import net.engineeringdigest.journalApp.Cache.AppCache;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.enums.Sentiment;
import net.engineeringdigest.journalApp.model.SentimentData;
import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import net.engineeringdigest.journalApp.service.EmailService;
import net.engineeringdigest.journalApp.service.SentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private SentimentAnalysisService analysisService;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate; // create a kafka template by autowired, having key, value pair

    // cron ? - > with the help of cron method -> we are told till when to run  the method under @Scheduler
    // cron -> chronos (Titan and god of time, greek mythology)
    // cron = "...." --> go to any cron generator site  and set date time to generate the string

    // The below methods needn't be called it will be scheduled via spring boot every 1 min.

    @Scheduled(cron = "0 0 6 * * ?") // every day at 6 am
    public void fetchUsersAndSendSaMail() {
/*        List<User> users = userRepository.getUserForSA("ansh_013");
        // right now there will only be a single user. But assume there are multiple user with same userName (which is not possible just assume)
        // so we run a loop

        for (User user : users) {
            List<JournalEntry> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream()
                    .filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))) // get entries with date not older than 100 days
                    .map(x -> x.getSentiment()) // for all the above dates get its content
                    .collect(Collectors.toList());  // since content is list save it as List

            Map<Sentiment, Integer> sentimentCounts = new HashMap<>();
            for (Sentiment sentiment : sentiments) {
                if (sentiment != null)
                    sentimentCounts.put(sentiment, sentimentCounts.getOrDefault(sentiment, 0) + 1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment, Integer> entry : sentimentCounts.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null) {
                // used builder pattern by lombok to set email and sentiment.
                SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + mostFrequentSentiment).build();
                try{
                    // work of producer to send data to broker
                    kafkaTemplate.send("foods", sentimentData.getEmail(), sentimentData);
                    // we are sending data to foods topic in kafka key -> email (string) and value -> sentimentData (Json)
                    // Since we are using a Key, then a partition in food is reserved for this email (key)
                }catch (Exception e){
                    emailService.sendEmail(sentimentData.getEmail(), "Sentiment for previous week", sentimentData.getSentiment());
                }
            }
        }*/
    }

    // updating cache every 10 mins
    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }

    }

