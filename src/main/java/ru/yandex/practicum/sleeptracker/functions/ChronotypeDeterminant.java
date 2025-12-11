package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.yandex.practicum.sleeptracker.model.Chronotype.*;

public class ChronotypeDeterminant implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Хронотип пользователя";

    public static final LocalTime OWL_MAX_NIGHT_HOUR = LocalTime.of(23, 0);
    public static final LocalTime OWL_MIN_MORNING_HOUR = LocalTime.of(9, 0);
    public static final LocalTime LARK_MAX_EVENING_HOUR = LocalTime.of(22, 0);
    public static final LocalTime LARK_MIN_MORNING_HOUR = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        Chronotype chronotypeWithMaxCount = sleepSessions
                .stream()
                .collect(Collectors.groupingBy(this::getChronotypeFromSleepSession, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow().getKey();

        return new SleepAnalysisResult(chronotypeWithMaxCount, DESCRIPTION);
    }

    private Chronotype getChronotypeFromSleepSession(SleepSession session) {
        LocalTime startSleep = session.getStartTime().toLocalTime();
        LocalTime endSleep = session.getEndTime().toLocalTime();

        if (startSleep.isAfter(OWL_MAX_NIGHT_HOUR)
                && endSleep.isAfter(OWL_MIN_MORNING_HOUR)) {
            return OWL;
        } else if (startSleep.isBefore(LARK_MAX_EVENING_HOUR)
                && endSleep.isBefore(LARK_MIN_MORNING_HOUR)) {
            return LARK;
        }

        return PIGEON;
    }
}
