package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Also note REST API's methods should be public in order for them to be accessible from spring framework or external http request.

@RestController // special type of @Component handling http requests
@RequestMapping("/_journal") // what it does we need to put -> /journal before every end point access. (basically the redundant
// endpoint path which occur with every rest API is written here.
public class JournalEntryController {

    private Map<ObjectId, JournalEntry> journalEntries = new HashMap<>();

    @GetMapping("/abc") // due to @RequestMapping the new route becomes -> localhost:8080/journal/abc
    public List<JournalEntry> getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping // write @PostMappint("/") or leave it as @PostMapping both are same
    public boolean createEntry(@RequestBody JournalEntry myEntry){

        // @RequestBody maps to the body of postman or the external source via which we are making the post call
        // myEntry maps to the body content we wrote in postman (in JSON, and that JSON gets mapped to the POJO class JournalEntry
        journalEntries.put(myEntry.getId(), myEntry);
        return true;

    }
    @GetMapping("id/{myId}") // {myId} --> a variable passed via external source while making request.
    public JournalEntry getById(@PathVariable String myId){ // we catch the var. via @Pathvariable in our GetMapping fun.
        return journalEntries.get(myId);
    }

    @DeleteMapping("id/{myId}")
    public JournalEntry deleteById(@PathVariable String myId){
        return journalEntries.remove(myId);
    }


    @PutMapping("id/{myId}")
    public JournalEntry updateById(@PathVariable ObjectId myId, @RequestBody JournalEntry myEntry ){
        // all arguments are needed which one to update and what to update is basically the body we send from postman

       // return journalEntries.put(myId, myEntry); --> display old values
        journalEntries.put(myId, myEntry);
        return myEntry; // --> new values displayed
           }


}
