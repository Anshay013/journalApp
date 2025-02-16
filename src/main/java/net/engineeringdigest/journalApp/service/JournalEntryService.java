package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

//    @Autowired
//    PlatformTransactionManager mongoTransactionManager;

   // private TransactionTemplate transactionTemplate;

    // constructor automatically gets invoked without calling it since class has @Service annotation on it (spring bean lifecycle)
//    public JournalEntryService(){
//        transactionTemplate = new TransactionTemplate(mongoTransactionManager);
//    }

    // note even if IOC C doesn't contain JournalEnryRepository, i.e that class isn't a bean, i.e not with a @Component or @RestController
    // we can still  write it with @Autowired annotation because at runtime spring will make an implementation for this class as a bean for us and inject it.
    // (it doesn't mean tha this class will be the part of IOC C, but spring is handling it to act like that)

//   public ResponseEntity<Object> saveEntry(JournalEntry journalEntry, String userName){
//        transactionTemplate.execute(status -> {
//            try {
//                User user = userService.findByUserName(userName);
//                journalEntry.setDate(LocalDateTime.now());
//                JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
//                user.getJournalEntries().add(savedEntry);
//
//                userService.saveUpdatedUser(user);
//                return new ResponseEntity<>(HttpStatus.CREATED); // commit by returning this if there is no problem.
//            } catch (Exception ignored){
//                status.setRollbackOnly(); // Mark for role back.
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//        });
//              return null;
//    }

    // note even if IOC C doesn't contain JournalEntryRepository, i.e that class isn't a bean, i.e not with a @Component or @RestController
    // we can still  write it with @Autowired annotation because at runtime spring will make an implementation for this class as a bean for us and inject it.
    // (it doesn't mean tha this class will be the part of IOC C, but spring is handling it to act like that)

 // @Transactional
    public ResponseEntity<Object> saveEntry(JournalEntry journalEntry, String userName){
            try {
                User user = userService.findByUserName(userName);
                journalEntry.setDate(LocalDateTime.now());
                JournalEntry savedEntry = journalEntryRepository.save(journalEntry); // also used to update the journalEntry suppose we have same id, but diff contents
                // now since @Transactional is applied the journalEntry is temporarily saved not commited to db.
                user.getJournalEntries().add(savedEntry);
                // now save user with updated Journal Entry

                // what will happen say due to some reason the journalEntry gets saved in journal_entries but the updated user doesn't get saved ?
                // we get an exception before saving it to user collection like -
                //   user.setUserName(null); --> or any logic that may set it null. Note that we have set username to NULL but we annotated it with @NotNull --> exception

                // so to avoid it we use @Transactional Annotation

                userService.saveUpdatedUser(user); // if this operation passes then both the above journalEntry and the updated user is commited to db, if this operations fails
                // the above saving of journalEntry rolls back
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (Exception ignored){
                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
    }

    // note -> since we have applied transactional and  not using replication with mongodb os we get an exception that transaction members are only allowed on a replica
    // set member -> in our local PC only a single instance is running but for transaction to happen we need multiple instance (i.e replication)
    // therefore here we will use mongo ATLAS i.e instead ogf running mongodb on local we will run it here.
    // Mongodb ATLAS makes our db managed. i.e our mongodb instance will run on aws

    public ResponseEntity<List<JournalEntry>> getAll(User user){
          List<JournalEntry> all = user.getJournalEntries();
          if(all != null && !all.isEmpty()){
              return new ResponseEntity<>(all, HttpStatus.OK);
          }
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity< Optional<JournalEntry>> getEntryById(ObjectId id){
        Optional<JournalEntry> entry = journalEntryRepository.findById(id);
        // is there something fetched from db or is it null

        if(entry.isPresent()){ // is there something fetched from db or is it null
            return new ResponseEntity<>(entry, HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity<?> deleteEntryById(ObjectId id, String userName){ // instead of Object we can write ? as well
        User user = userService.findByUserName(userName);
        boolean removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
        if(removed){
            userService.saveUpdatedUser(user);
            try {
                journalEntryRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(e.getMessage() ,HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }


    public ResponseEntity<JournalEntry> updateEntry(ObjectId id, JournalEntry entry, String userName){
       // id and date won't change changes can be title and content
        JournalEntry oldJournalEntry = journalEntryRepository.findById(id).orElse(null);
      //  the reason we don't pass in journalEnryRepository.findById(entry.getId()) because its null, and we send this null directly to our
        // mongodb collection, since we don't apply prior checks, mongo sends a fatal.
        // why null -> because we pass nothing to entry.id, only content and description is passed. (vai put API)


        if(oldJournalEntry != null) {
            User user = userService.findByUserName(userName); // first remove the old JournalEntry form JournalEntry list of associated user.
            if (user != null) {
                List<JournalEntry> entries = user.getJournalEntries();
                entries.remove(oldJournalEntry);

                // we can save saveEntry(entry) directly but what if its title or content is empty or null. (we don't want to save those the reason why we check the below)
                oldJournalEntry.setTitle(!entry.getTitle().isEmpty() ? entry.getTitle() : oldJournalEntry.getTitle());
                oldJournalEntry.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ? entry.getContent() : oldJournalEntry.getContent());
                oldJournalEntry.setDate(LocalDateTime.now());
                try {
                    JournalEntry savedEntry = journalEntryRepository.save(oldJournalEntry); // also used to update the journalEntry suppose we have same id, but diff contents
                    user.getJournalEntries().add(savedEntry);
                    userService.saveUpdatedUser(user);  // now save user with updated Journal Entry
                    return new ResponseEntity<>(oldJournalEntry, HttpStatus.OK);
                } catch (Exception e) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);

                }
            }
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    public ResponseEntity<Object> deleteAllEntries(){
        List<JournalEntry> myJournals = journalEntryRepository.findAll();
        if(!myJournals.isEmpty()) {
            journalEntryRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
