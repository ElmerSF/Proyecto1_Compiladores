/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clasificación de los tipos de token
*/

package proyecto1;

public enum TokenType {
    RESERVED_WORD,     // Palabras reservadas (Module, Dim, End, etc.)
    IDENTIFIER,        // Identificadores válidos
    NUMBER,            // Números enteros
    STRING_LITERAL,    // Cadenas entre comillas
    OPERATOR,          // Operadores (+, -, *, /, =)
    SYMBOL,            // Símbolos como paréntesis, comas, etc.
    UNKNOWN            // Cualquier cosa no reconocida
}

