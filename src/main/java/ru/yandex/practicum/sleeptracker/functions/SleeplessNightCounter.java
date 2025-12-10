package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.time.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleeplessNightCounter implements Function<List<SleepSession>, SleepAnalysisResult> {

    private static final String DESCRIPTION = "Количество бессонных ночей";
    private static final LocalTime START_NIGHT_TIME = LocalTime.of(0, 0);
    private static final LocalTime END_NIGHT_TIME = LocalTime.of(6, 0);
    private static final LocalTime MIDDAY_TIME = LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult apply(List<SleepSession> sleepSessions) {
        LocalDateTime startTimeFirst = sleepSessions.getFirst().getStartTime();
        LocalDateTime endTimeLast = sleepSessions.getLast().getEndTime();

        List<LocalDate> allNightsInPeriod = getDatesBetweenPeriod(startTimeFirst, endTimeLast);

        long sleeplessNights = allNightsInPeriod
                .stream()
                .filter(night -> isSleepless(night, sleepSessions))
                .count();

        return new SleepAnalysisResult(sleeplessNights, DESCRIPTION);
    }

    private List<LocalDate> getDatesBetweenPeriod(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        // Пропускаем ночь первого дня если сессия сна началась после 12:00
        if (startDateTime.toLocalTime().isAfter(MIDDAY_TIME)) {
            startDate = startDate.plusDays(1);
        }

        return startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());
    }

    private boolean isSleepless(LocalDate night, List<SleepSession> sleepSessions) {
        LocalDateTime nightStart = LocalDateTime.of(night, START_NIGHT_TIME);
        LocalDateTime nightEnd = LocalDateTime.of(night, END_NIGHT_TIME);

        return sleepSessions.stream()
                .filter(session -> isSessionInNightDay(session, night))
                .noneMatch(session -> isSessionOverlapNightTime(session, nightStart, nightEnd));
    }

    private boolean isSessionInNightDay(SleepSession session, LocalDate night) {
        return session.getStartTime().toLocalDate().equals(night) || session.getEndTime().toLocalDate().equals(night);
    }

    private boolean isSessionOverlapNightTime(SleepSession session, LocalDateTime nightStart, LocalDateTime nightEnd) {
        return session.getStartTime().isBefore(nightEnd) && session.getEndTime().isAfter(nightStart);
    }
}
