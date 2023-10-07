package ru.phestrix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StatDto {
    private String fullName;
    private String productName;
    private Integer expenses;
}
