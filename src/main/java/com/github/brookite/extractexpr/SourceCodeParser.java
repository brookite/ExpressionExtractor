package com.github.brookite.extractexpr;

import org.treesitter.TSLanguage;
import org.treesitter.TSNode;
import org.treesitter.TSParser;
import org.treesitter.TSTree;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SourceCodeParser {
    public static class AnalyzeResult {
        boolean isSupported = true;
        int depth;
    }

    public record Node(TSNode tsNode, String code, String fullCode, LanguageInfo lang) {};

    public static AnalyzeResult analyzeNode(LanguageInfo lang, TSNode node) {
        AnalyzeResult result = new AnalyzeResult();
        performNodeAnalysis(node, lang, result, 0);
        return result;
    }

    private static void performNodeAnalysis(TSNode node, LanguageInfo info, AnalyzeResult result, int depth) {
        if (List.of(info.ignoredOperators()).contains(node.getType())) {
            result.isSupported = false;
        }

        if (List.of(info.targetNodeNames()).contains(node.getType())) {
            depth++;
        }

        result.depth = Math.max(depth, result.depth);

        for (int i = 0; i < node.getNamedChildCount(); i++) {
            performNodeAnalysis(node.getNamedChild(i), info, result, depth);
        }
    }

    public static Node[] collectExpressions(LanguageInfo lang, String source) {
        TSLanguage grammar = lang.getGrammar();
        TSParser parser = new TSParser();
        parser.setLanguage(grammar);
        TSTree tree = parser.parseString(null, source);
        List<Node> nodes = new ArrayList<>();
        if (!tree.getRootNode().hasError()) {
            collectNodesByType(tree.getRootNode(), nodes, lang, source);
        } else {
            System.err.println("Parsing failed. Found syntax errors");
        }
        return nodes.toArray(new Node[0]);
    }

    public static String getCodePiece(String sourceCode, TSNode node) {
        byte[] code = sourceCode.getBytes(StandardCharsets.UTF_8);
        int start = node.getStartByte();
        int end = node.getEndByte();
        return new String(code, start, end - start);
    }

    private static void collectNodesByType(TSNode node, List<Node> result, LanguageInfo lang, String source) {
        if (List.of(lang.targetNodeNames()).contains(node.getType()) && !List.of(lang.ignoredOperators()).contains(node.getType())) {
            result.add(new Node(node, getCodePiece(source, node), source, lang));
        }

        for (int i = 0; i < node.getNamedChildCount(); i++) {
            collectNodesByType(node.getNamedChild(i), result, lang, source);
        }
    }
}