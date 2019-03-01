package org.rublin.to;

import lombok.Data;

@Data
public class HeatingResponseDto {
    private boolean globalStatus;
    private float tempUp;
    private float tempDown;

    public String status() {
        return globalStatus ? "Pump is ON" : "Pump is OFF";
    }
}
