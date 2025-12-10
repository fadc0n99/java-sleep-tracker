package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.util.List;
import java.util.function.Function;

public class SleepSessionCounter implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Количество сессий сна";

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        return new SleepAnalysisResult(sleepSessions.size(), DESCRIPTION);
    }
}
