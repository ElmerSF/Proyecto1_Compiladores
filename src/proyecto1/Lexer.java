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
  
     // <editor-fold defaultstate="collapsed" desc="primer intento división por split">
    /*
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
     //  if (parte.matches("[=+\\-*]")) {
        //    tokens.add(new Token(parte, TokenType.OPERATOR));
          //  continue;
        }

        // 6. Símbolos
      //  if (parte.matches("[()\",]")) {
        //    tokens.add(new Token(parte, TokenType.SYMBOL));
          //  continue;
        //}


        // 7. Cualquier otra cosa
       // tokens.add(new Token(parte, TokenType.UNKNOWN));
   // }

    //return tokens;
//}
*/
  
    // </editor-fold>
    

    public List<Token> tokenizar(String linea) {
        List<Token> tokens = new ArrayList<>();
        if (linea == null || linea.isEmpty()) {
            return tokens;
        }

        int i = 0;
        int n = linea.length();

        while (i < n) {
            char c = linea.charAt(i);

            // Espacios
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Comentarios: todo lo que sigue a '
            if (c == '\'') {
                break;
            }

            // Strings entre comillas, con espacios internos
            if (c == '"') {
                int inicio = i;
                i++; // saltar la primera comilla
                StringBuilder sb = new StringBuilder();
                sb.append('"');
                boolean cerrado = false;

                while (i < n) {
                    char d = linea.charAt(i);
                    sb.append(d);
                    if (d == '"') {
                        cerrado = true;
                        i++; // incluir comilla de cierre
                        break;
                    }
                    i++;
                }

                String lexema = sb.toString();
                if (cerrado) {
                    tokens.add(new Token(lexema, TokenType.STRING_LITERAL));
                } else {
                    // comilla sin cerrar, igual lo tomamos como STRING_LITERAL
                    tokens.add(new Token(lexema, TokenType.STRING_LITERAL));
                }
                continue;
            }

            // Números (enteros o decimales)
            if (Character.isDigit(c)) {
                int inicio = i;
                boolean tienePunto = false;
                while (i < n && (Character.isDigit(linea.charAt(i)) || linea.charAt(i) == '.')) {
                    if (linea.charAt(i) == '.') {
                        if (tienePunto) break; // segundo punto, se corta
                        tienePunto = true;
                    }
                    i++;
                }
                String lexema = linea.substring(inicio, i);
                tokens.add(new Token(lexema, TokenType.NUMBER));
                continue;
            }

            // Identificadores o palabras reservadas
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

            // Operadores simples
            if (c == '=' || c == '+' || c == '-' || c == '*' || c == '/' || c == '&') {
                tokens.add(new Token(String.valueOf(c), TokenType.OPERATOR));
                i++;
                continue;
            }

            // Símbolos
            if (c == '(' || c == ')' || c == ',' ) {
                tokens.add(new Token(String.valueOf(c), TokenType.SYMBOL));
                i++;
                continue;
            }

            // Cualquier otro carácter
            tokens.add(new Token(String.valueOf(c), TokenType.UNKNOWN));
            i++;
        }

        return tokens;
    }

    public List<Token> lexerizar(String linea) {
        return tokenizar(linea);
    }
}
