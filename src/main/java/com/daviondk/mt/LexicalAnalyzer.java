package com.daviondk.mt;

import java.io.InputStream;
import java.util.Scanner;

public class LexicalAnalyzer {

    private static final String DELIMITER_REGEX = "((?<=[()])\\s*(?=\\S))|((?<=\\S)\\s*(?=[()])|\\s+)";
    private final Scanner scanner;

    private Token currentToken;
    private String currentTokenString;

    public LexicalAnalyzer(InputStream is) {
        scanner = new Scanner(is);
        scanner.useDelimiter(DELIMITER_REGEX);
        currentToken = nextToken();
    }

    public Token currentToken() {
        return currentToken;
    }

    public String currentTokenString() {
        return currentTokenString;
    }

    public Token nextToken() {
        if (!scanner.hasNext()) {
            currentTokenString = "";
            return currentToken = Token.END;
        }
        currentTokenString = scanner.next();
        switch (currentTokenString) {
            case "or":
                return currentToken = Token.OR;
            case "xor":
                return currentToken = Token.XOR;
            case "and":
                return currentToken = Token.AND;
            case "not":
                return currentToken = Token.NOT;
            case "in":
                return currentToken = Token.IN;
            case "(":
                return currentToken = Token.LPAREN;
            case ")":
                return currentToken = Token.RPAREN;
        }
        if (currentTokenString.length() == 1 &&
                Character.isAlphabetic(currentTokenString.codePointAt(0))) {
            return currentToken = Token.VARIABLE;
        }
        return currentToken = Token.UNDEFINED;
    }
}
