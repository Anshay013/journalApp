package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigJournalAppService {

    @Autowired
    ConfigJournalAppRepository appRepository;


    public ResponseEntity<?> saveConfig(ConfigJournalAppEntity appEntity){
        try {
            appRepository.save(appEntity);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(appEntity, HttpStatus.OK);
    }

    public ResponseEntity<?> getAllApiDetail(){
        try{
            List<ConfigJournalAppEntity> allEntries = appRepository.findAll();
            return new ResponseEntity<>(allEntries, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
