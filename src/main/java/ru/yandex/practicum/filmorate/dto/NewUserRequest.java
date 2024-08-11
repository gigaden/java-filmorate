package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class NewUserRequest {
    @Email
    @NotEmpty
    private String email;
    @NotNull
    @NotBlank
    private String login;
    private String name;
    @NotNull
    private LocalDate birthday;

    private Set<Long> friends;
}
