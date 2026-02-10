/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clasificación de los tipos de token
*/

package proyecto1;

//<editor-fold defaultstate="collapsed" desc="Explicación del uso de Enum">
/*
 * TokenType (Enum)
 *
 * Este enum define todas las categorías posibles de tokens que el Lexer puede producir.
 * Se usa para clasificar cada fragmento del código fuente en un tipo bien definido:
 * identificadores, números, operadores, cadenas, símbolos, etc.
 *
 * ¿Por qué usar un enum?
 * ----------------------
 * Según la documentación oficial de Oracle:
 * “An enum type is a special data type that enables for a variable to be a set of predefined constants.”
 * (Oracle Java Documentation)
 *
 * Esto significa que un enum permite representar un conjunto fijo de valores constantes
 * de forma segura, clara y eficiente. En un analizador léxico, esto es fundamental porque:
 *
 * - Evita errores de escritura (no dependemos de comparar Strings).
 * - Hace el código más claro y mantenible.
 * - Permite que el Validador trabaje con categorías, no con texto crudo.
 * - Es más eficiente que comparar cadenas.
 * - Facilita agregar nuevas reglas sin romper el diseño.
 *
 * En este proyecto, TokenType es esencial porque:
 * - El Lexer clasifica cada token según su tipo.
 * - El Validador aplica reglas del lenguaje basándose en estos tipos.
 * - La tabla de símbolos usa estos tipos para validar operaciones y asignaciones.
 *
 * Referencia:
 * Oracle. (n.d.). *Enum Types*. Oracle Java Documentation.
 * https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html
 */
// </editor-fold>


public enum TokenType {
    RESERVED_WORD,     // Palabras reservadas (Module, Dim, End, etc.)
    IDENTIFIER,        // Identificadores válidos
    NUMBER,            // Números enteros
    STRING_LITERAL,    // Cadenas entre comillas
    OPERATOR,          // Operadores (+, -, *, /, =)
    SYMBOL,            // Símbolos como paréntesis, comas, etc.
    UNKNOWN            // Cualquier cosa no reconocida
}

