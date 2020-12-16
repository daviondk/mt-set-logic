package com.daviondk.mt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Parser {
    LexicalAnalyzer lexicalAnalyzer;

    private Tree or() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.OR),
                Collections.emptyList(), "OR", true);
    }

    private Tree xor() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.XOR),
                Collections.emptyList(), "XOR", true);
    }

    private Tree and() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.AND),
                Collections.emptyList(), "AND", true);
    }

    private Tree not() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.NOT),
                Collections.emptyList(), "NOT", true);
    }

    private Tree in() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.IN),
                Collections.emptyList(), "IN", true);
    }

    private Tree variable() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.VARIABLE),
                Collections.emptyList(),
                "VARIABLE_" + lexicalAnalyzer.currentTokenString(), true);
    }

    private Tree lParen() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.LPAREN),
                Collections.emptyList(), "LPAREN", true);
    }

    private Tree rParen() {
        return buildSimpleTree(
                Collections.emptyList(),
                Collections.singletonList(Token.RPAREN),
                Collections.emptyList(), "RPAREN", true);
    }

    private Tree S() {
        return buildSimpleTree(
                List.of(Token.VARIABLE, Token.LPAREN, Token.NOT),
                Collections.emptyList(),
                List.of(this::T, this::SPrime), "S", false);
    }

    private Tree SPrime() {
        return buildSimpleTree(
                Collections.singletonList(Token.OR),
                List.of(Token.END, Token.RPAREN),
                List.of(this::or, this::T, this::SPrime), "SPrime", false);
    }

    private Tree T() {
        return buildSimpleTree(
                List.of(Token.VARIABLE, Token.LPAREN, Token.NOT),
                Collections.emptyList(),
                List.of(this::U, this::TPrime), "T", false);
    }

    private Tree TPrime() {
        return buildSimpleTree(
                Collections.singletonList(Token.XOR),
                List.of(Token.END, Token.RPAREN, Token.OR),
                List.of(this::xor, this::U, this::TPrime), "TPrime", false);
    }

    private Tree U() {
        return buildSimpleTree(
                List.of(Token.VARIABLE, Token.LPAREN, Token.NOT),
                Collections.emptyList(),
                List.of(this::V, this::UPrime), "U", false);
    }

    private Tree UPrime() {
        return buildSimpleTree(
                Collections.singletonList(Token.AND),
                List.of(Token.END, Token.RPAREN, Token.OR, Token.XOR),
                List.of(this::and, this::V, this::UPrime), "UPrime", false);
    }

    private Tree V() {
        return buildSimpleTree(
                List.of(Token.VARIABLE, Token.LPAREN, Token.NOT),
                Collections.emptyList(),
                List.of(this::W, this::R), "V", false);
    }

    private Tree VPrime() {
        return buildSimpleTree(
                Collections.singletonList(Token.IN),
                List.of(Token.END, Token.RPAREN, Token.OR, Token.XOR, Token.AND),
                List.of(this::in, this::W, this::VPrime), "VPrime", false);
    }

    private Tree VDoublePrime() {
        return buildSimpleTree(
                Collections.singletonList(Token.NOT),
                Collections.emptyList(),
                List.of(this::not, this::VTriplePrime), "VDoublePrime", false);
    }

    private Tree VTriplePrime() {
        return buildSimpleTree(
                Collections.singletonList(Token.IN),
                Collections.emptyList(),
                List.of(this::in, this::W, this::VPrime), "VTriplePrime", false);
    }

    private Tree W() {
        switch (lexicalAnalyzer.currentToken()) {
            case NOT:
                return new Tree("W", List.of(not(), X()));
            case VARIABLE:
            case LPAREN:
                return new Tree("W", Collections.singletonList(X()));
            default:
                throw new ParseException();
        }
    }

    private Tree X() {
        switch (lexicalAnalyzer.currentToken()) {
            case VARIABLE:
                return new Tree("X", Collections.singletonList(variable()));
            case LPAREN:
                return new Tree("X", List.of(lParen(), S(), rParen()));
            default:
                throw new ParseException();
        }
    }

    private Tree R() {
        switch (lexicalAnalyzer.currentToken()) {
            case AND:
            case XOR:
            case OR:
            case END:
            case RPAREN:
            case IN:
                return new Tree("R", List.of(VPrime()));
            case NOT:
                return new Tree("R", List.of(VDoublePrime()));
            default:
                throw new ParseException();
        }
    }

    private Tree buildSimpleTree(List<Token> first, List<Token> first_after_eps,
                                 List<Supplier<Tree>> nodes, String nodeName, boolean nextToken) {
        Token currentToken = lexicalAnalyzer.currentToken();
        if (nextToken) {
            lexicalAnalyzer.nextToken();
        }
        if (first.contains(currentToken)) {
            List<Tree> children = nodes.stream().map(Supplier::get).collect(Collectors.toList());
            return new Tree(nodeName, children);
        } else if (first_after_eps.contains(currentToken)) {
            return new Tree(nodeName);
        } else {
            throw new ParseException();
        }
    }

    public Tree parse(InputStream is) {
        lexicalAnalyzer = new LexicalAnalyzer(is);
        return S();
    }

    public Tree parse(String str) {
        return parse(new ByteArrayInputStream(str.getBytes()));
    }
}
