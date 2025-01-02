package com.github.brookite.extractexpr;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;

public class FileUtils {
    public static String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    public static String readUTF8(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }

    public static String readUTF16(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_16);
    }

    public static String sanitizeFileName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        String sanitized = Normalizer.normalize(name, Normalizer.Form.NFKC);

        sanitized = sanitized.replaceAll("[^a-zA-Z0-9]", "_");

        sanitized = sanitized.replaceAll(" ", "_");

        sanitized = sanitized.replaceAll("[\\p{Cntrl}]", "").trim();

        int maxLength = 96;
        if (sanitized.length() > maxLength) {
            sanitized = sanitized.substring(0, maxLength);
        }

        if (sanitized.isEmpty()) {
            sanitized = Double.toString(Math.random());
        }

        return sanitized;
    }
}
