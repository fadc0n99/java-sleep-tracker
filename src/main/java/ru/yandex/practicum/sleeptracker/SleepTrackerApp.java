package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.io.IOFileReaderService;
import ru.yandex.practicum.sleeptracker.functions.*;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static final List<Function<List<SleepSession>, SleepAnalysisResult>> ANALYSIS_FUNCTIONS = List.of(
            new SleepSessionCounter(),
            new MinDurationSleepSession(),
            new MaxDurationSleepSession(),
            new DurationSleepSessionAverage(),
            new BadSleepSessionCounter(),
            new SleeplessNightCounter(),
            new ChronotypeDeterminant()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Путь к файлу не был передан");
            return;
        }

        String filepath = args[0];
        SleepSessionLoader loader = new SleepSessionLoader(FORMATTER, new IOFileReaderService());
        List<SleepSession> sleepSessionList;

        try {
            sleepSessionList = loader.loadSessions(filepath);
            analyzeSleepSessions(sleepSessionList).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<SleepAnalysisResult> analyzeSleepSessions(List<SleepSession> sessions) {
        if (sessions.isEmpty()) {
            return List.of(new SleepAnalysisResult("Записи отсутствуют",
                            "Нет корректных записей, либо файл пуст"));
        }

        return ANALYSIS_FUNCTIONS.stream()
                .map(f -> f.apply(sessions)).collect(Collectors.toList());
    }
}