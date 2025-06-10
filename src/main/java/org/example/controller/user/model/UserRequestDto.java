package org.example.controller.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Schema(description = "Create user DTO")
@Data
public class UserRequestDto {

    @JsonProperty("first_name")
    @NotNull(message = "The user name is required")
    @NotBlank(message = "The user name is required")
    @Size(min = 2, max = 50, message = "The user name must be between 1 and 50 characters long")
    @Schema(
            description = "User name",
            example = "Ivan",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 50)
    public String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "The user last name is required")
    @NotBlank(message = "The user last name is required")
    @Size(min = 2, max = 50, message = "The user last name must be between 1 and 50 characters long")
    @Schema(description = "User last name",
            example = "Ivanov",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 2,
            maxLength = 50)
    public String lastName;
}
