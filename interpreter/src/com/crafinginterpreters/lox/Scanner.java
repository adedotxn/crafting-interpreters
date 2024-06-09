package com.crafinginterpreters.lox;

import static com.crafinginterpreters.lox.TokenType.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; // Maybe find alternative to this static import
/**
 * We can use REGEX to recognize all the different lexems for Lox 
 * but we will be handcrafting our way of doing that since our aim is to understand how it works
 */


 class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>(); //stores the tokens we will be generating
    private int start = 0; // points to the first character of the lexeme being scanned
    private int current = 0; // points to the character currently being considered;
    private int line = 1; // tracks the line on the source the 'current' is currently on;

   private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    Scanner(String source) {
        this.source = source;
    }

    // Scans the source code, adding tokens till it runs out of characters.
    // In each loop, we scan a single token.
    List<Token> scanTokens() {
        while(!isAtEnd()) {
            // We are at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(': 
            addToken(LEFT_PAREN);
                break;

            case ')' :
            addToken(RIGHT_PAREN);
                break;

            case '{' :
            addToken(LEFT_BRACE);
                break;

            case '}' :
            addToken(RIGHT_BRACE);
                break;

            case ',' :
            addToken(COMMA);
                break;

            case '.' :
            addToken(DOT);
                break;

            case '-' :
            addToken(MINUS);
                break;

            case '+' :
            addToken(PLUS);
                break;

            case ';' :
            addToken(SEMICOLON);
                break;
            
            case '*' :
            addToken(STAR);
                break;

            // Operators
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;

            case '>':
                addToken(match('=') ? GREATER_EQUAL : EQUAL);
                break;
            
            //Longer Lexemes
            case '/':
                if(match('/')) {
                    while(peek() != '\n' && !isAtEnd()) advance(); // A Comment goes until end of line
                } else if(match('*')) {
                    // handling block comments => /* ...  */ style.
                    while(!isAtEnd()) {
                        if(peek() == '*' && peekNext() == '/') {
                            advance(); // consume *
                            advance(); // consume /   
                            break;
                        }
                        advance();
                    }

                    if(isAtEnd()) {
                        Lox.error(line, "Unterminated block comment");
                    }
                 } else {
                    addToken(SLASH); }
                break;
            
            case ' ':
            case '\r':
            case '\t':
                // Ignore Whitespace
                break;

            // String Literals
            case '"':
                string();
                break;
            
            default:
                if(isDigit(c)) {
                    number();
                } else if(isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Unexpected Character"); }
                break;
                    
        }
    }

    private void identifier() {
        while(isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        
        if(type == null) type = IDENTIFIER;
        
        addToken(type);
    }

    private void number() {
        while(isDigit(peek())) advance();

        // Look for fractional part
        if(peek() == '.' && isDigit(peekNext())) { // we  do not want to consume "." until we're sure there's a digit ahead;
            // consume the "."
            advance();

            while(isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        // Lox supports multi-line strings;
        while(peek() != '"' && !isAtEnd()) { // If we're not at the closing quote and not at the source end;
            if(peek() == '\n') line++; 
            advance();
        }

        if(isAtEnd()) { // If we're not at the closing end too but at end
            Lox.error(line, "Unterminated String");
            return;
        }

        // Closing "
        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    private boolean match(char expected) {
        if(isAtEnd()) return false;
        if(source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if(isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if(current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c  >= 'a' && c <= 'z') || 
                (c >= 'A' && c <= 'Z') || 
                    c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) { // We use this overload to handle tokens with literal values;
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
