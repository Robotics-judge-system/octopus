package ru.anarcom.octopus.utilus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceReader {
    public static String getResource(String path) throws IOException {
        InputStream inputstream = ClassLoader.getSystemResourceAsStream(path);
        return new String(inputstream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
