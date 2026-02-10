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
        "MODULE",
        "SUB",
        "DIM",
        "AS",
        "IF",
        "THEN",
        "ELSEIF",
        "ELSE",
        "FUNCTION",
        "RETURN",
        "WHILE",
        "END",
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
        if (linea == null || linea.isEmpty()) {
            return tokens;
        }

        int i = 0;
        int n = linea.length();

        while (i < n) {
            char c = linea.charAt(i);

            // ============================================================
            // ESPACIOS EN BLANCO
            // ============================================================
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // ============================================================
            // COMENTARIOS (todo lo que sigue a ')
            // ============================================================
            if (c == '\'') {
                break;
            }

            // ============================================================
            // STRINGS ENTRE COMILLAS
            // ============================================================
            if (c == '"') {
                // cadena de texto vamos recorriendo cada carácter
                int inicio = i;
                i++; 
                
                //lo vamos guardando temporalmente
                StringBuilder sb = new StringBuilder();
                sb.append('"');
                boolean cerrado = false;
                
                //mientras no se haya cerrado las comillas
                while (i < n) {
                    char d = linea.charAt(i);
                    sb.append(d);
                    if (d == '"') {
                        cerrado = true;
                        i++;
                        break;
                    }
                    i++;
                }
                    //lo identificamos como una cadena de texto
                tokens.add(new Token(sb.toString(), TokenType.STRING_LITERAL));
                continue;
            }

            // ============================================================
            // IDENTIFICADORES INVÁLIDOS
            // ============================================================

            //Ojo aquí puse ❌ CÓDIGO NUEVO: identificadores que empiezan con "_"
            // Antes no se detectaban explícitamente
            if (c == '_') {
                int inicio = i;
                while (i < n && (Character.isLetterOrDigit(linea.charAt(i)) || linea.charAt(i) == '_')) {
                    i++;
                }
                String lexema = linea.substring(inicio, i);

                // Se marca como UNKNOWN para que el Validador determine el error exacto
                tokens.add(new Token(lexema, TokenType.UNKNOWN));
                continue;
            }

            // ❌ CÓDIGO NUEVO: identificadores inválidos que empiezan con número
            // Antes se mezclaban con números válidos
            if (Character.isDigit(c)) {

                int inicio = i;
                boolean tieneLetras = false;
                boolean tienePunto = false;

                while (i < n && (Character.isLetterOrDigit(linea.charAt(i)) || linea.charAt(i) == '_' || linea.charAt(i) == '.')) {

                    char d = linea.charAt(i);

                    if (Character.isLetter(d) || d == '_') {
                        tieneLetras = true; // 4var, 3x3, 9_numero
                    }

                    if (d == '.') {
                        if (tienePunto) break; // segundo punto → inválido
                        tienePunto = true;
                    }

                    i++;
                }

                String lexema = linea.substring(inicio, i);

                if (tieneLetras) {
                    // Caso: 4var → UNKNOWN
                    tokens.add(new Token(lexema, TokenType.UNKNOWN));
                } else {
                    // Caso: 12 o 12.5 → NUMBER
                    tokens.add(new Token(lexema, TokenType.NUMBER));
                }

                continue;
            }

            // ============================================================
            // IDENTIFICADORES VÁLIDOS O PALABRAS RESERVADAS
            // ============================================================

            // comparamos con las palabras reservadas
            if (Character.isLetter(c)) {
                int inicio = i;

                while (i < n && (Character.isLetterOrDigit(linea.charAt(i)) || linea.charAt(i) == '_')) {
                    i++;
                }

                String lexema = linea.substring(inicio, i);
                String normalizado = lexema.toUpperCase();

                if (PALABRAS_RESERVADAS.contains(normalizado)) {
                    tokens.add(new Token(lexema, TokenType.RESERVED_WORD));
                } else {
                    tokens.add(new Token(lexema, TokenType.IDENTIFIER));
                }
                continue;
            }

            // ============================================================
            // OPERADORES
            // ============================================================
            if (c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '&') {
                tokens.add(new Token(String.valueOf(c), TokenType.OPERATOR));
                i++;
                continue;
            }

            // ============================================================
            // SÍMBOLOS
            // ============================================================
            if (c == '(' || c == ')' || c == ',' ) {
                tokens.add(new Token(String.valueOf(c), TokenType.SYMBOL));
                i++;
                continue;
            }

            // ============================================================
            // CUALQUIER OTRO CARÁCTER → UNKNOWN
            // ============================================================
            tokens.add(new Token(String.valueOf(c), TokenType.UNKNOWN));
            i++;
        }

        return tokens;
    }

    public List<Token> lexerizar(String linea) {
        return tokenizar(linea);
    }
}
