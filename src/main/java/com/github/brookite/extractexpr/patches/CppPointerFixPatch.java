package com.github.brookite.extractexpr.patches;

import org.treesitter.TSNode;

public class CppPointerFixPatch {
    public static String fixString(TSNode node, String s) {
        if (node.getType().equals("binary_expression") && node.getChildByFieldName("operator").getType().equals("*")) {
            return "_patch = " + s;
        }
        return s;
    }
}
