package com.github.brookite.extractexpr.languages;

import com.github.brookite.extractexpr.LanguageInfo;
import org.treesitter.TreeSitterPython;
import org.vstu.meaningtree.SupportedLanguage;

public class PythonLanguage extends LanguageInfo {

    public PythonLanguage() {
        this.grammar = new TreeSitterPython();
        this.mtLang = SupportedLanguage.PYTHON;
    }

    @Override
    public String[] targetNodeNames() {
        return new String[] {
                "binary_operator", "comparison_operator", "unary_operator", "call", "not_operator",
                "attribute", "subscript", "boolean_operator",
                "parenthesized_expression", "conditional_expression", "named_expression",
        };
    }

    @Override
    public String[] ignoredOperators() {
        return new String[] {"yield", "lambda"};
    }
}
