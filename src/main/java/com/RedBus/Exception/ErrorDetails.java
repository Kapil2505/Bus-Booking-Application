package com.RedBus.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data

public class ErrorDetails {
    private Date date;
    private String message;
    private String description;

    public ErrorDetails(Date date, String message, String description) {
        this.date = date;
        this.message = message;
        this.description = description;
    }
}
