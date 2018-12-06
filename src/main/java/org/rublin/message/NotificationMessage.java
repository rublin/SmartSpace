package org.rublin.message;

import com.google.common.primitives.Ints;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class NotificationMessage implements Delayed {

    // File (path to file) or text to say
    private String data;

    // Timestamp when to say
    private long startTime;

    public NotificationMessage(String data, long delaySeconds) {
        this.data = data;
        this.startTime = System.currentTimeMillis() + delaySeconds * 1000;
        log.info("Notification message created with delay {} sec", getDelay(TimeUnit.SECONDS));
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(startTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Ints.saturatedCast(this.startTime - ((NotificationMessage) o).startTime);
    }
}
