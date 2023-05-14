package pl.inzynierka.schronisko.news;

import lombok.Data;

@Data
public class NewsResponse {
    private long id;
    private String addedBy;
    private String shelter;
    private String addedTime;
    private String title;
    private String message;
}
