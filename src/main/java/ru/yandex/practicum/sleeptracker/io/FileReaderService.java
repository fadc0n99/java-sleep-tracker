package ru.yandex.practicum.sleeptracker.io;

import java.io.IOException;
import java.util.List;

public interface FileReaderService {
    List<String> readLines(String filename) throws IOException;
}
