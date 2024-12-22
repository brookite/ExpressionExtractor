package com.github.brookite.extractexpr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SourceFileAnalyzer {
    public static final List<String> supportedFileExtensions = LanguageSupport.allExtensions();

    public static String[] collectSupportedFiles(String directoryPath) {
        List<String> collectedFiles = new ArrayList<>();
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("Invalid directory path: " + directoryPath);
        }

        collectFilesRecursively(directory, collectedFiles, SourceFileAnalyzer.supportedFileExtensions);

        return collectedFiles.toArray(new String[0]);
    }

    private static void collectFilesRecursively(File directory, List<String> collectedFiles, List<String> supportedExtensions) {
        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                collectFilesRecursively(file, collectedFiles, supportedExtensions);
            } else {
                String fileName = file.getName();
                if (supportedExtensions.contains(FileUtils.getExtension(fileName))) {
                    collectedFiles.add(file.getAbsolutePath());
                }
            }
        }
    }
}
