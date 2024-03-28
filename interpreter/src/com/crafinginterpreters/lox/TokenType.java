package com.crafinginterpreters.lox;

enum TokenType { // Keywords of the language grammar; reserved words.
    //Single-character tokens
    LEFT_PAREN, RIGHT_PAREN, 
    LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, SEMICOLON, SLASH,
    MINUS, PLUS,
    STAR,

    // One or two character tokens
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // Literals
    IDENTIFIER,STRING, NUMBER,

    // Keywords
    AND, CLASS, ELSE, FALSE, 
    FUN, FOR, IF, NIL, OR, 
    PRINT, RETURN, SUPER, THIS, 
    TRUE, VAR, WHILE, 
    
    EOF 
}
