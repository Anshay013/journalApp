package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.Cache.AppCache;
import net.engineeringdigest.journalApp.api.response.JsonPlaceHolderResponse;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class JsonPlaceHolderService {
    private static String API;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JsonPlaceHolderResponse post;

    private final AppCache appCache;

    @Autowired
    public JsonPlaceHolderService(AppCache appCache){
        this.appCache = appCache;
    }


    public ResponseEntity<?> createPost(JsonPlaceHolderResponse response) {
        // Create request payload
        API = appCache.getAPP_CACHE().get("jsonPlaceHolder_api");
        post.setTitle(response.getTitle());
        post.setBody(response.getBody());
        post.setUserId(response.getUserId());
        post.setId(response.getId());

        // we don't set ID the API we are hitting, its server will set it automatically

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // we set an header by telling that the media type we are posting is a JSON

        // Create request entity
        HttpEntity<JsonPlaceHolderResponse> request = new HttpEntity<>(post, headers);

        // Make the POST request, all GET, POST, UPDATE, DELETE are done by restTemplate -
        // Here like we did in GET, HttpMethod.GET we don't need to set HttpMethod.POST because we are using .postForEntity
        ResponseEntity<JsonPlaceHolderResponse> res = restTemplate.postForEntity(API, request, JsonPlaceHolderResponse.class);

        // sending JsonPlaceHolderResponse.class in post call means we are serializing it into JSON.
        return res;
    }


}
