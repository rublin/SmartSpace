package org.rublin.events;

import com.google.common.primitives.Ints;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
public class OnHeatingEvent extends ApplicationEvent implements Delayed {

    private final long startTime;
    private final boolean pumpStatus;

    public OnHeatingEvent(long delaySeconds, boolean pumpStatus) {
        super(delaySeconds);
        this.startTime = System.currentTimeMillis() + delaySeconds * 1000;
        this.pumpStatus = pumpStatus;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(startTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return Ints.saturatedCast(this.startTime - ((OnHeatingEvent) o).startTime);
    }
}
