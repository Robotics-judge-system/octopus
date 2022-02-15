package ru.anarcom.octopus.utilus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceReader {
    public static String getResource(String path) throws IOException {
        if (path.indexOf('/') == 0) {
            path = path.substring(1);
        }
        InputStream is = ClassLoader.getSystemResourceAsStream(path);
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
}
