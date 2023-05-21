package pl.inzynierka.schronisko.advices;

import lombok.Data;

@Data
public class AdviceResponse {
    private long id;
    private String addedBy;
    private String shelter;
    private String addedTime;
    private String title;
    private String message;
}
