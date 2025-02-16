package net.engineeringdigest.journalApp.repository;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

// this MongoRepository is provided by Spring Data MongoDB.
// MongoRepository is an interface that provides abstraction from complex functions of database interaction (all it does is standard CRUD operations)

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {
    // now to achieve ORM for mongo, we need to make our POJO class i.e JournalEntry as Document(row) in order to map it to a collection (table)
}
