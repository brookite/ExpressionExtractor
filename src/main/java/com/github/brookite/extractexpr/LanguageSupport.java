package com.github.brookite.extractexpr;

import com.github.brookite.extractexpr.languages.CppLanguage;
import com.github.brookite.extractexpr.languages.JavaLanguage;
import com.github.brookite.extractexpr.languages.PythonLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LanguageSupport {
    private static HashMap<String, LanguageInfo> languages = new HashMap<>() {{
        put("python", new PythonLanguage());
        put("java", new JavaLanguage());
        put("cpp", new CppLanguage());
    }};

    private static HashMap<String, List<String>> extensions = new HashMap<>() {{
        put("python", List.of("py"));
        put("java", List.of("java"));
        put("cpp", List.of("cpp", "c", "h", "cxx", "hpp", "cc"));
    }};

    public static Optional<LanguageInfo> getParser(String lang) {
        return Optional.ofNullable(languages.getOrDefault(lang, null));
    }

    public static Optional<LanguageInfo> getParserByFileType(String ext) {
        for (String lang : extensions.keySet()) {
            if (extensions.get(lang).contains(ext)) {
                return Optional.of(languages.get(lang));
            }
        }
        return Optional.empty();
    }

    public static List<String> allExtensions() {
        ArrayList<String> ext = new ArrayList<>();
        for (String key : extensions.keySet()) {
            ext.addAll(extensions.get(key));
        }
        return ext;
    }
}
