package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({ "userId", "id", "title", "body" })  // Ensures JSON order --> we will send post response in this order
public class JsonPlaceHolderResponse {

    private int userId;
    private int id;
    private String title;
    private String body;



    @Override
    public String toString() {
        return "PostResponse{" +
                "userId=" + userId +
                ", id=" + id +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
