package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MpaDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;
    private String name;
}