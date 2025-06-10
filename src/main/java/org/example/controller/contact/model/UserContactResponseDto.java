package org.example.controller.contact.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.model.contact.ContactType;

@Data
public class UserContactResponseDto {

    @JsonProperty("user_id")
    public Long userID;

    public String value;

    public ContactType type;
}
