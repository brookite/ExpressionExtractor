package com.github.brookite.extractexpr;

import org.treesitter.TSLanguage;
import org.vstu.meaningtree.MeaningTree;
import org.vstu.meaningtree.SupportedLanguage;
import org.vstu.meaningtree.languages.LanguageTranslator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public abstract class LanguageInfo {
    protected TSLanguage grammar;
    protected SupportedLanguage mtLang;

    public abstract String[] targetNodeNames();
    public abstract String[] ignoredOperators();
    public abstract String getName();

    public TSLanguage getGrammar() {
        return grammar;
    }

    public MeaningTree createExpressionMeaningTree(SourceCodeParser.Node node) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Only expression support
        LanguageTranslator translator = mtLang.createTranslator(new HashMap<>() {{
            put("skipErrors", "true");
            put("translationUnitMode", "false");
            put("expressionMode", "true");
            put("disableCompoundComparisonConversion", "true");
        }});
        return translator.getMeaningTree(node.code);
    }
}
