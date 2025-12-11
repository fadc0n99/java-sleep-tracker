package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.io.FileReaderService;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.SleepSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SleepSessionLoader {

    private final DateTimeFormatter dateTimeFormatter;
    private final FileReaderService fileReaderService;

    public SleepSessionLoader(DateTimeFormatter dateTimeFormatter, FileReaderService fileReaderService) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.fileReaderService = fileReaderService;
    }

    public List<SleepSession> loadSessions(String filepath) throws IOException {
        List<String> sessionsFromFile = fileReaderService.readLines(filepath);

        return sessionsFromFile.stream()
                .map(this::convertToSession)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<SleepSession> convertToSession(String sessionFromFile) {
        String[] elements = sessionFromFile.split(";");

        if (elements.length != 3) {
            System.out.printf("Некорректный формат строки: %s. Сессия сна не будет создана%n", sessionFromFile);
            return Optional.empty();
        }

        LocalDateTime startSleepTime;
        LocalDateTime endSleepTime;
        SleepQuality quality;

        try {
            startSleepTime = LocalDateTime.parse(elements[0], dateTimeFormatter);
            endSleepTime = LocalDateTime.parse(elements[1], dateTimeFormatter);
            quality = SleepQuality.valueOf(elements[2]);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            System.out.printf("Не удалось извлечь данные. Сессия сна не будет создана. %s%n", e.getCause());
            return Optional.empty();
        }

        return Optional.of(new SleepSession(startSleepTime, endSleepTime, quality));
    }
}
