package io.golayer.sharing.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateSharingRequestDto {
    private List<String> emails;
    private List<String> selections;
}
