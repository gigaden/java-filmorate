package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    @NonNull
    @NotBlank
    @Email
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;


}
