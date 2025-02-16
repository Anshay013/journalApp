package net.engineeringdigest.journalApp.Cache;

import lombok.Getter;
import net.engineeringdigest.journalApp.entity.ConfigJournalAppEntity;
import net.engineeringdigest.journalApp.repository.ConfigJournalAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component // since this bean will be loaded one time
@Getter
public class AppCache {


    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    private Map<String, String> APP_CACHE;

    @PostConstruct    // this init will also be invoked one time, hence the Map appCache will also be loaded onr time one time.
    public void init(){
        APP_CACHE = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for(ConfigJournalAppEntity configJournalAppEntity : all){
            APP_CACHE.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
    }

    // this appCache will work as in memory cache, so instead to ask from mongo for API, ask this cache

}
