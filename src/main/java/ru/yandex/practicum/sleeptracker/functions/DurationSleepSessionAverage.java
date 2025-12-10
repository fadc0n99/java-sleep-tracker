package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class DurationSleepSessionAverage implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Cредняя продолжительность сна (в минутах)";

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        double avgDurationSeconds = sleepSessions
                .stream()
                .map(SleepSession::getDuration)
                .mapToDouble(Duration::toSeconds)
                .average().orElse(0);

        return new SleepAnalysisResult(
                avgDurationSeconds / 60, DESCRIPTION);
    }
}
