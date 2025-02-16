package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")

// This class is a controller which calls the services below described as @Mapping functions
// controller makes the end point and calls the service.
public class JournalEntityControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        return journalEntryService.getAll(user);
    }

    @PostMapping
    public ResponseEntity<?> createJournalForUser(@RequestBody JournalEntry journalEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return journalEntryService.saveEntry(journalEntry, userName);
    }

    @GetMapping("id/{myId}") // now id is an ObjectID, postman will show the id in timestamp and date format which we cannot call
    // via end point so, we need to check mongoshell, or fetch every id print or store them via java code here
    public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){  // list will have only one item
             Optional<JournalEntry> journalEntry = journalEntryService.getEntryById(myId).getBody();
            if(journalEntry.isPresent()){
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("delete/{myId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

         return journalEntryService.deleteEntryById(myId, userName);
    }

    @PutMapping("id/{myId}")
    public ResponseEntity<JournalEntry> updateJournalById(@PathVariable ObjectId myId,  @RequestBody JournalEntry myEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
       return journalEntryService.updateEntry(myId, myEntry, userName);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Object> deleteAll(){
        return journalEntryService.deleteAllEntries();
    }
}
//"timestamp": 1736527722,
//        "date": "2025-01-10T16:48:42.000+00:00"