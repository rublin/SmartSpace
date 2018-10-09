package org.rublin.to;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Builder
@Getter
@Setter
public class TelegramResponseDto {
    private List<String> messages;
    private List<File> files;
    private String id;
}