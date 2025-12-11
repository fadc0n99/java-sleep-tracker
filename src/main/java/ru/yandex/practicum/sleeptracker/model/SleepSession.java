package ru.yandex.practicum.sleeptracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SleepSession {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final SleepQuality quality;

    public SleepSession(LocalDateTime startTime, LocalDateTime endTime, SleepQuality quality) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.quality = quality;
    }

    public Duration getDuration() {
        return Duration.between(startTime, endTime);
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "SleepSession{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", quality=" + quality +
                '}';
    }
}
