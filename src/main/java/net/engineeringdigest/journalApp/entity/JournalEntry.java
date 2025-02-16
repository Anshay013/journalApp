package net.engineeringdigest.journalApp.entity;

// A simple POJO (plain old java object) class

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.enums.Sentiment;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journal_entries") // it says that an instance of JournalEntry is equal to a document
// collection = "journal_entries" -->, naming of collection. If we don't write the name then mongoRepository will have JournalEntry i.e the class name
// in database else it will search for journal_entries.

// Lombok annotation to generate all the below at compile time
// (i.e the maven dependency of lombok checks at compile time that the annotation is there, and for that generates what the annotation does)
// Lombok generates bytecode like getters, setters, equals, Hashcode().. etc as specified by type annotation. This generated code is added to the compiled .class files

//@Getter
//@Setter
//@RequiredArgsConstructor
//@ToString
//@EqualsAndHashCo de
//@Value

// instead of the all above put -
@Data // -> note this annotation contains @RequiredArgsConstructor, that is we need to create an constructor but we don't want to do so as of now therefore we suppy
// an annotation of @NoArgsConstructor to handle it.
@NoArgsConstructor
// note for de-serialization of JSON to the below POJO class (JournalEntry) the constructor of the class needs to be called but here we don't have any constructor defined
// Hence we give an annotation of @NoArgsConstructor
public class JournalEntry {


    @Id
    private ObjectId id; //  assign our unique key by ObjectId,
    // we don't need to send Id from post call anymore (it will automatically be allocated) -> and in case if we do, we get a 404 error response
    // also once id is created can can never be updated, yes the entry can be deleted and other contents can be updated leaving date  (because we are setting it)
    @NonNull
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiment sentiment;

    // note since id is the unique key here, we try to save am entry with existing id, it won't be saved in collection

}
