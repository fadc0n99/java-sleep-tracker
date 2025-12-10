package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinDurationSleepSession implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Минимальная продолжительность сна (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        long minDurationMinutes = sleepSessions
                .stream()
                .map(SleepSession::getDuration)
                .min(Duration::compareTo)
                .orElse(Duration.ZERO)
                .toMinutes();

        return new SleepAnalysisResult(
                minDurationMinutes, DESCRIPTION);
    }
}
