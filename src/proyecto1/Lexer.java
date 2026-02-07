/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para tokenizar cada línea
*/

package proyecto1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lexer {

    // Conjunto de palabras reservadas (normalizadas en mayúscula)
    public static final Set<String> PALABRAS_RESERVADAS = Set.of(
        "MODULE",//
        "SUB",//
        "DIM",//
        "AS",//
        "IF",//
        "THEN",//
        "ELSEIF",//
        "ELSE",//
        "FUNCTION",//
        "RETURN",//
        "WHILE",//
         "END",//
         
        "IMPORTS",
        
        "INTEGER",
        "STRING",
        "BOOLEAN",
        "BYTE",
        
        "WRITELINE",
        "CONSOLE"
    );

    public Lexer() {}

    public List<Token> tokenizar(String linea) {
        List<Token> tokens = new ArrayList<>();

        // Dividir por espacios sin eliminar contenido interno
        String[] partes = linea.trim().split("\\s+");

        for (String parte : partes) {

            if (parte.isEmpty()) continue;

            String normalizado = parte.toUpperCase();

            // 1. Palabras reservadas
            if (PALABRAS_RESERVADAS.contains(normalizado)) {
                tokens.add(new Token(parte, TokenType.RESERVED_WORD));
                continue;
            }

            // 2. Identificadores válidos
            if (parte.matches("[A-Za-z][A-Za-z0-9_]*")) {
                tokens.add(new Token(parte, TokenType.IDENTIFIER));
                continue;
            }

            // 3. Números
            if (parte.matches("\\d+")) {
                tokens.add(new Token(parte, TokenType.NUMBER));
                continue;
            }

            // 4. Cadenas entre comillas
            if (parte.startsWith("\"") && parte.endsWith("\"")) {
                tokens.add(new Token(parte, TokenType.STRING_LITERAL));
                continue;
            }

            // 5. Operadores simples
            if (parte.matches("[=+\\-*/]")) {
                tokens.add(new Token(parte, TokenType.OPERATOR));
                continue;
            }

            // 6. Símbolos
            if (parte.matches("[()\",]")) {
                tokens.add(new Token(parte, TokenType.SYMBOL));
                continue;
            }

            // 7. Cualquier otra cosa
            tokens.add(new Token(parte, TokenType.UNKNOWN));
        }
        //devuelve lista de tokens 
        return tokens;
    }

    public List<Token> lexerizar(String linea) {
        return tokenizar(linea);
    }
}
