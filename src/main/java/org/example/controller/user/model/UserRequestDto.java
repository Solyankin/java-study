package org.example.controller.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "DTO для создания пользователя")
@Data
public class UserRequestDto {

    @JsonProperty("first_name")
    @NotNull
    @Schema(
            description = "Имя пользователя",
            example = "Иван",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1,
            maxLength = 50)
    public String firstName;

    @JsonProperty("last_name")
    @NotNull
    @Schema(description = "Фамилия пользователя",
            example = "Иванов",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 1,
            maxLength = 50)
    public String lastName;
}
