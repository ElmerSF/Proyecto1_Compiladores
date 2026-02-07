/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para validación de los token
Valida reglas del lenguaje como palabras reservadas
*/
package proyecto1;

import java.util.List;

public class Validador {

    private final ErrorManager errorManager;

    public Validador(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * Método principal de validación por línea.
     * Aquí se decide qué tipo de validación aplicar según los tokens.
     */
    public void validarLinea(List<Token> tokens, String linea, int numeroLinea) {

        if (tokens == null || tokens.isEmpty()) {
            return; // línea vacía
        }

        Token primero = tokens.get(0);

        // Validación de declaraciones Dim
        if (primero.es("RESERVED_WORD", "Dim")) {
            validarDeclaracionDim(tokens, linea, numeroLinea);
            return;
        }

        // Aquí luego agregaremos validaciones de:
        // - Console.WriteLine
        // - If / ElseIf / Else / End If
        // - Module / End Module
        // - Function / End Function
    }

    /**
     * Validación del formato de declaración de variables (Punto 5 del proyecto).
     * Estructura correcta:
     * Dim <identificador> As <tipo> [= <valor>]
     */
    private void validarDeclaracionDim(List<Token> tokens, String linea, int numeroLinea) {

        // Validación mínima: Dim id As tipo
        if (tokens.size() < 4) {
            errorManager.agregarError("Declaración Dim incompleta.", linea, numeroLinea);
            return;
        }

        // 1. Validar identificador
        Token identificador = tokens.get(1);
        if (identificador.type != TokenType.IDENTIFIER) {
            errorManager.agregarError("Se esperaba un identificador después de 'Dim'.", linea, numeroLinea);
        }

        // 2. Validar palabra reservada As
        Token asToken = tokens.get(2);
        if (!asToken.es("RESERVED_WORD", "As")) {
            errorManager.agregarError("Se esperaba la palabra reservada 'As'.", linea, numeroLinea);
        }

        // 3. Validar tipo
        Token tipo = tokens.get(3);
        if (!esTipoValido(tipo.lexema)) {
            errorManager.agregarError("Tipo de dato inválido en declaración Dim.", linea, numeroLinea);
        }

        // 4. Validación opcional de asignación (= valor)
        if (tokens.size() > 4) {
            validarAsignacionOpcional(tokens, linea, numeroLinea, tipo);
        }

        // 5. Validar que no haya tokens extra
        if (tokens.size() > 6) {
            errorManager.agregarError("Tokens extra después de la declaración de variable.", linea, numeroLinea);
        }
    }

    /**
     * Verifica si el tipo es válido según el proyecto.
     */
    private boolean esTipoValido(String tipo) {
        return tipo.equalsIgnoreCase("Integer") ||
               tipo.equalsIgnoreCase("Double") ||
               tipo.equalsIgnoreCase("String") ||
               tipo.equalsIgnoreCase("Boolean");
    }

    /**
     * Validación de la parte opcional: = valor
     */
    private void validarAsignacionOpcional(List<Token> tokens, String linea, int numeroLinea, Token tipo) {

        if (!tokens.get(4).es("OPERATOR", "=")) {
            errorManager.agregarError("Se esperaba '=' para asignación.", linea, numeroLinea);
            return;
        }

        if (tokens.size() < 6) {
            errorManager.agregarError("Falta el valor después de '='.", linea, numeroLinea);
            return;
        }

        Token valor = tokens.get(5);

        // Validar compatibilidad de tipos
        validarCompatibilidad(tipo, valor, linea, numeroLinea);
    }

    /**
     * Validación de compatibilidad entre tipo declarado y valor asignado.
     */
    private void validarCompatibilidad(Token tipo, Token valor, String linea, int numeroLinea) {

        String t = tipo.lexema.toLowerCase();

        switch (t) {

            case "integer":
                if (valor.type != TokenType.NUMBER || valor.lexema.contains(".")) {
                    errorManager.agregarError("El valor asignado no es un Integer válido.", linea, numeroLinea);
                }
                break;

            case "double":
                if (valor.type != TokenType.NUMBER) {
                    errorManager.agregarError("El valor asignado no es un Double válido.", linea, numeroLinea);
                }
                break;

            case "string":
                if (valor.type != TokenType.STRING_LITERAL) {
                    errorManager.agregarError("El valor asignado no es un String válido.", linea, numeroLinea);
                }
                break;

            case "boolean":
                if (!valor.lexema.equalsIgnoreCase("True") &&
                    !valor.lexema.equalsIgnoreCase("False")) {
                    errorManager.agregarError("El valor asignado no es un Boolean válido.", linea, numeroLinea);
                }
                break;

            default:
                errorManager.agregarError("Tipo desconocido en declaración Dim.", linea, numeroLinea);
        }
    }
}
