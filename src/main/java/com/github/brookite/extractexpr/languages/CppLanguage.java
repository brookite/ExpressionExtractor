package com.github.brookite.extractexpr.languages;

import com.github.brookite.extractexpr.LanguageInfo;
import com.github.brookite.extractexpr.SourceCodeParser;
import com.github.brookite.extractexpr.patches.CppPointerFixPatch;
import org.treesitter.TreeSitterCpp;
import org.vstu.meaningtree.MeaningTree;
import org.vstu.meaningtree.SupportedLanguage;
import org.vstu.meaningtree.nodes.statements.assignments.AssignmentStatement;

import java.lang.reflect.InvocationTargetException;

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
    public MeaningTree createExpressionMeaningTree(SourceCodeParser.Node node) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        node.code = CppPointerFixPatch.fixString(node.tsNode, node.code);
        MeaningTree mt = super.createExpressionMeaningTree(node);
        if (mt.getRootNode() instanceof AssignmentStatement assign) {
            mt.changeRoot(assign.getRValue());
        }
        return mt;
    }

        @Override
    public String getName() {
        return "cpp";
    }
}
