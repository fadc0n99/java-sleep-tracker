package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.util.List;
import java.util.function.Function;

public class BadSleepSessionCounter implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Количество сессий с плохим качество сна";

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        long countBadSessions = sleepSessions
                .stream()
                .filter(session -> session.getQuality().equals(SleepQuality.BAD))
                .count();

        return new SleepAnalysisResult(countBadSessions, DESCRIPTION);
    }
}
