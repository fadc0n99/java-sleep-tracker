package ru.yandex.practicum.sleeptracker.model;

public record SleepAnalysisResult(Object result, String description) {
    @Override
    public String toString() {
        return description + ": " + result;
    }
}
