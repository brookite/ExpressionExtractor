package com.github.brookite.extractexpr.languages;

import com.github.brookite.extractexpr.LanguageInfo;
import org.treesitter.TreeSitterJava;
import org.vstu.meaningtree.SupportedLanguage;

public class JavaLanguage extends LanguageInfo {
    public JavaLanguage() {
        this.grammar = new TreeSitterJava();
        this.mtLang = SupportedLanguage.JAVA;
    }

    @Override
    public String[] targetNodeNames() {
        return new String[] {"method_invocation", "binary_expression",
                "unary_expression", "parenthesized_expression",
                "instanceof_expression", "field_access", "ternary_expression",
                "update_expression", "array_access", "assignment_expression"
        };
    }

    @Override
    public String[] ignoredOperators() {
        return new String[] {"lambda_expression", "comment"};
    }

    @Override
    public String getName() {
        return "java";
    }
}
