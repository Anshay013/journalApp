package net.engineeringdigest.journalApp.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@NoArgsConstructor
@Data
public class User {

    @Id
    private ObjectId id;

    @Indexed(unique = true) // making username unique, and by @Indexed the searching of userName becomes fast
    @NonNull // mustn't be null, we will get a warning during compilation time only, if there's  a possibility for it to be NULL (while we are setting it)
    private String userName;
    @NonNull
    private String password;

    private String email;
    private boolean sentimentAnalysis;
    private int age;

    @DBRef // it means we are creating a reference inside User for JournalEntries.
    // in simple words this arrayList will now keep the reference of the entries that are present in the collection = journal_entries.
    private List<JournalEntry>journalEntries = new ArrayList<>(); // don't need to pass this just like id via Post API call, just pass userName and password

    private List<String> roles; // when a user gets saved to collection what roles does it have ? -> admin, normal user etc.

    // main funda is to attach DBRef of JournalEntry to User
}
