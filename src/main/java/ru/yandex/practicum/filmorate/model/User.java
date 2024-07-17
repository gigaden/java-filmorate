package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Set;

@Data
public class User {

    private Long id;
    @Email
    @NotEmpty
    private String email;
    @NonNull
    @NotBlank
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    private Set<Long> friends;


}
