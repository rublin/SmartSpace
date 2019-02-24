package org.rublin.to;

import lombok.Data;

@Data
public class HeatingResponseDto {
    private boolean globalStatus;
    private float tempUp;
    private float tempDown;
}
