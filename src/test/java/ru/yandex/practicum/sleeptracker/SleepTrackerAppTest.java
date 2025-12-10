package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.*;
import ru.yandex.practicum.sleeptracker.model.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {

    private final BadSleepSessionCounter badSleepSessionCounter = new BadSleepSessionCounter();
    private final ChronotypeDeterminant chronotypeDeterminant = new ChronotypeDeterminant();
    private final DurationSleepSessionAverage durationSleepSessionAverage = new DurationSleepSessionAverage();
    private final MaxDurationSleepSession maxDurationSleepSession = new MaxDurationSleepSession();
    private final MinDurationSleepSession minDurationSleepSession = new MinDurationSleepSession();
    private final SleeplessNightCounter sleeplessNightCounter = new SleeplessNightCounter();

    @Test
    void testBadSleepSessionCounter_WhenNoBadSessionsShouldReturn0() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), SleepQuality.NORMAL)
        );

        SleepAnalysisResult result = badSleepSessionCounter.apply(sessions);

        assertEquals(0L, result.getResult());
    }

    @Test
    void testBadSleepSessionCounter_WhenAllBadSessionsShouldReturnAllCountSessions() {
        List<SleepSession> sessions = List.of(
                new SleepSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), SleepQuality.BAD),
                new SleepSession(LocalDateTime.now(), LocalDateTime.now().plusHours(5), SleepQuality.BAD)
        );

        BadSleepSessionCounter counter = new BadSleepSessionCounter();
        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals(2L, result.getResult());
    }

    @Test
    void testChronotypeDeterminant_WhenBefore22And07ShouldReturnLark() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2023, 10, 1, 21, 59),
                        LocalDateTime.of(2023, 10, 1, 6, 59),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = chronotypeDeterminant.apply(sessions);

        assertEquals(Chronotype.LARK, result.getResult());
    }

    @Test
    void testChronotypeDeterminant_WhenBeforeAfter23And09ShouldReturnOwl() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2023, 10, 1, 23, 1),
                        LocalDateTime.of(2023, 10, 2, 9, 1),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = chronotypeDeterminant.apply(sessions);

        assertEquals(Chronotype.OWL, result.getResult());
    }


    @Test
    void testDurationSleepSessionAverage_WhenSessionWithMinimalDurationShouldReturnsOneMinute() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 1, 22, 1),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = durationSleepSessionAverage.apply(sessions);

        assertEquals(1.0, result.getResult());
    }

    @Test
    void testDurationSleepSessionAverage_WhenSessionsWithFractionalMinutesShouldHandlesPrecisionCorrectly() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0, 0),
                        LocalDateTime.of(2025, 10, 1, 22, 10, 30),
                        SleepQuality.NORMAL
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0, 0),
                        LocalDateTime.of(2025, 10, 1, 23, 5, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = durationSleepSessionAverage.apply(sessions);

        assertEquals(7.75, result.getResult());
    }

    @Test
    void testMinDurationSleepSession_ShouldReturnsMinimum() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 0, 30),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 1, 0),
                        LocalDateTime.of(2025, 10, 2, 1, 45),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 2, 0),
                        LocalDateTime.of(2025, 10, 3, 4, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = minDurationSleepSession.apply(sessions);

        assertEquals(45L, result.getResult());
    }

    @Test
    void testMaxDurationSleepSession_ShouldReturnsMaximum() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 0, 30),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 1, 0),
                        LocalDateTime.of(2025, 10, 2, 1, 45),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 2, 0),
                        LocalDateTime.of(2025, 10, 3, 4, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = maxDurationSleepSession.apply(sessions);

        assertEquals(120L, result.getResult());
    }

    @Test
    void testSleeplessNightCounter_ShouldReturns3Nights() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 1, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 3, 0, 30),
                        LocalDateTime.of(2025, 10, 3, 5, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 6, 10, 0),
                        LocalDateTime.of(2025, 10, 6, 12, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = sleeplessNightCounter.apply(sessions);

        assertEquals(3L, result.getResult());
    }

    @Test
    void testSleeplessNightCounter_ShouldSkipFirstSessionAndReturns2Nights() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 13, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 2, 2, 0),
                        LocalDateTime.of(2025, 10, 2, 4, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 4, 10, 0),
                        LocalDateTime.of(2025, 10, 4, 12, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = sleeplessNightCounter.apply(sessions);

        assertEquals(2L, result.getResult());
    }

    @Test
    void testSleeplessNightCounter_WhenSleepDuration1MinuteAtNightReturnZeroSleeplessNights() {
        List<SleepSession> sessions = List.of(
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 5, 59),
                        LocalDateTime.of(2025, 10, 1, 8, 0),
                        SleepQuality.GOOD
                ),
                new SleepSession(
                        LocalDateTime.of(2025, 10, 1, 23, 59),
                        LocalDateTime.of(2025, 10, 2, 0, 1),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = sleeplessNightCounter.apply(sessions);

        assertEquals(0L, result.getResult());
    }

}