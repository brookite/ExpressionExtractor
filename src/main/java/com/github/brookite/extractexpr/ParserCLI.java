package com.github.brookite.extractexpr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParserCLI {
    public static final int minExprDepth = 3;
    private static final Logger log = LogManager.getLogger(ParserCLI.class);

    public static void main(String[] args) throws IOException {
        List<String> filePaths = new ArrayList<>();
        String domain = null;
        String repoInfo = null;
        String outputDir = null;
        boolean parsingFiles = true;

        for (String arg : args) {
            if (arg.equals("--")) {
                parsingFiles = false;
                continue;
            }

            if (parsingFiles) {
                filePaths.add(arg);
            } else if (repoInfo == null) {
                repoInfo = arg;
            } else if (domain == null) {
                domain = arg;
            } else if (outputDir == null) {
                outputDir = arg;
            } else {
                log.error("Unexpected argument: " + arg);
                System.exit(1);
            }
        }

        if (filePaths.isEmpty()) {
            log.error("Error: No input files provided.");
            System.exit(1);
        }

        if (domain == null) {
            log.error("Error: No domain provided.");
            System.exit(1);
        }

        if (outputDir == null) {
            log.error("Error: No output directory provided.");
            System.exit(1);
        }

        SourceCodeRepositoryInfo repositoryInfo = null;
        if (new File(repoInfo).exists()) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create();
            repositoryInfo = gson.fromJson(Files.readString(Path.of(repoInfo)), SourceCodeRepositoryInfo.class);
        }

        List<SourceCodeParser.Node> expressions = parseFiles(filePaths);
        File outputDirFile = new File(outputDir);
        if (!outputDirFile.isDirectory()) {
            boolean mkdirRes = outputDirFile.mkdirs();
            if (!mkdirRes) {
                log.error("Error: Failed to create output directory.");
                System.exit(1);
            }
        }
        for (SourceCodeParser.Node expr : expressions) {
            File file = new File(outputDirFile, FileUtils.sanitizeFileName(expr.code) + "_" + expr.lang.getName() + ".mt.ttl");
            try {
                FileOutputStream ostream = new FileOutputStream(file);

                log.info("Creating from expression: " + expr.code);
                boolean res = ASTSerializer.meaningTreeTtl(expr, expr.lang, repositoryInfo, ostream);
                ostream.close();
                if (!res) {
                    file.delete();
                    log.warn("Creating meaning tree failed. Unsupported conversion");
                } else {
                    log.info("Created question: " + file);
                }
            } catch (Exception e) {
                log.error("Parser exception {} with msg: {}", e.getClass().getName(), e.getMessage());
                log.error("{} parse failed in {}", expr.code, expr.fileName());
                file.delete();
                throw new RuntimeException(e);
            }
        }
    }

    private static List<SourceCodeParser.Node> parseFiles(List<String> filePaths) {
        List<SourceCodeParser.Node> nodes = new ArrayList<>();
        for (String filePath : filePaths) {
            if (!new File(filePath).exists()) {
                log.error("File not found: " + filePath);
                continue;
            }
            log.error("Processing: " + filePath);
            Optional<LanguageInfo> lang = LanguageSupport.getParserByFileType(FileUtils.getExtension(filePath));
            if (lang.isPresent()) {
                try {
                    SourceCodeParser.Node[] parsed = SourceCodeParser.collectExpressions(lang.get(), FileUtils.readUTF8(filePath));
                    for (SourceCodeParser.Node expr : parsed) {
                        SourceCodeParser.AnalyzeResult analyzeResult = SourceCodeParser.analyzeNode(lang.get(), expr.tsNode);
                        expr.setFilename(filePath);
                        if (analyzeResult.depth >= minExprDepth && analyzeResult.isSupported) {
                            nodes.add(expr);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return nodes;
    }
}
