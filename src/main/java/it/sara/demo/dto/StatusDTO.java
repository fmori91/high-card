package it.sara.demo.dto;

import lombok.Data;


@Data
public class StatusDTO {
    private int code;
    private String message;
    private String traceId;

    public StatusDTO() {
    }

    public StatusDTO(String invalidCredentials) {
    }
}
