package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeDeterminant implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Хронотип пользователя";

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        Map<Chronotype, Long> map = sleepSessions
                .stream()
                .collect(Collectors.groupingBy(this::getChronotypeFromSleepSession, Collectors.counting()));

        Chronotype chronotypeWithMaxCount = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow().getKey();

        return new SleepAnalysisResult(chronotypeWithMaxCount, DESCRIPTION);
    }

    private Chronotype getChronotypeFromSleepSession(SleepSession session) {
        LocalTime startSleep = session.getStartTime().toLocalTime();
        LocalTime endSleep = session.getEndTime().toLocalTime();

        if (startSleep.isAfter(LocalTime.of(23, 0))
                && endSleep.isAfter(LocalTime.of(9, 0))) {
            return Chronotype.OWL;
        } else if (startSleep.isBefore(LocalTime.of(22, 0))
                && endSleep.isBefore(LocalTime.of(7, 0))) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }
}
