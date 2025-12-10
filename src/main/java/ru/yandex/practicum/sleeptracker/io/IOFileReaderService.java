package ru.yandex.practicum.sleeptracker.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class IOFileReaderService implements FileReaderService {

    @Override
    public List<String> readLines(String filename) throws IOException {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Путь не может быть null или пустым");
        }

        if (!Files.exists(Paths.get(filename))) {
            throw new FileNotFoundException(String.format("Файл \"%s\" не найден", filename));
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            return bufferedReader
                    .lines()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Ошибка чтения файла" + filename, e);
        }
    }
}
