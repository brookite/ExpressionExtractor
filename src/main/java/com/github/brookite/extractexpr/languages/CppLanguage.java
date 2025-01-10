package com.github.brookite.extractexpr.languages;

import com.github.brookite.extractexpr.LanguageInfo;
import org.treesitter.TreeSitterCpp;
import org.vstu.meaningtree.SupportedLanguage;

public class CppLanguage extends LanguageInfo {
    public CppLanguage() {
        this.grammar = new TreeSitterCpp();
        this.mtLang = SupportedLanguage.CPP;
    }

    @Override
    public String[] targetNodeNames() {
        return new String[] {
                "binary_expression", "unary_expression", "call_expression",
                "assignment_expression", "subscript_expression", "field_expression",
                "new_expression", "conditional_expression", "pointer_expression",
                "cast_expression", "qualified_identifier", "sizeof_expression", "offsetof_expression", "comma_expression"
        };
    }

    @Override
    public String[] ignoredOperators() {
        return new String[] {"lambda_expression", "co_await_expression", "delete_expression", "comment"};
    }

    @Override
    public String getName() {
        return "cpp";
    }
}
