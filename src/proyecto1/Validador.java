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
    private final SymbolTable symbolTable;
    private boolean moduleEncontrado = false;

    public Validador(ErrorManager errorManager, SymbolTable symbolTable) {
        this.errorManager = errorManager;
        this.symbolTable = symbolTable;
    }

    public void validarLinea(List<Token> tokens, String linea, int numeroLinea) {

        if (tokens == null || tokens.isEmpty()) {
            return;
        }

        Token primero = tokens.get(0);

        // Detectar Module
        if (primero.es("RESERVED_WORD", "Module")) {
            moduleEncontrado = true;
            return;
        }

        // Validación de declaraciones Dim
        if (primero.es("RESERVED_WORD", "Dim")) {

            // Validar que Dim NO aparezca antes del Module
            if (!moduleEncontrado) {
                errorManager.agregarError(ErrorCode.DIM_ANTES_DE_MODULE, linea, numeroLinea);
                return;
            }

            validarDeclaracionDim(tokens, linea, numeroLinea);
            return;
        }
    }

    /**
     * Validación del formato de declaración de variables (Punto 5).
     */
    private void validarDeclaracionDim(List<Token> tokens, String linea, int numeroLinea) {

        if (tokens.size() < 4) {
            errorManager.agregarError(ErrorCode.DECLARACION_INCOMPLETA, linea, numeroLinea);
            return;
        }

        // 1. Identificador
        Token identificador = tokens.get(1);
        if (identificador.type != TokenType.IDENTIFIER) {
            errorManager.agregarError(ErrorCode.IDENTIFICADOR_INVALIDO, linea, numeroLinea);
        }

        // 2. Palabra reservada As
        Token asToken = tokens.get(2);
        if (!asToken.es("RESERVED_WORD", "As")) {
            errorManager.agregarError(ErrorCode.FALTA_AS, linea, numeroLinea);
        }

        // 3. Tipo
        Token tipo = tokens.get(3);
        if (!esTipoValido(tipo.lexema)) {
            errorManager.agregarError(ErrorCode.TIPO_INVALIDO, linea, numeroLinea);
        }

        // Registrar variable en tabla de símbolos
        symbolTable.registrar(identificador.lexema, tipo.lexema);

        // 4. Validación opcional de asignación
        if (tokens.size() > 4) {
            validarAsignacion(tokens, linea, numeroLinea, tipo);
        }

        // 5. Tokens extra (solo si NO hay operación matemática)
        boolean hayOperacion = false;
        for (int i = 5; i < tokens.size(); i++) {
            Token t = tokens.get(i);
            if (t.type == TokenType.OPERATOR && t.lexema.matches("[+\\-*/]")) {
                hayOperacion = true;
                break;
            }
        }

        if (!hayOperacion && tokens.size() > 6) {
            errorManager.agregarError(ErrorCode.TOKENS_EXTRA, linea, numeroLinea);
        }
    }

    private boolean esTipoValido(String tipo) {
        return tipo.equalsIgnoreCase("Integer") ||
               tipo.equalsIgnoreCase("Double") ||
               tipo.equalsIgnoreCase("String") ||
               tipo.equalsIgnoreCase("Boolean");
    }

    /**
     * Validación completa de asignación:
     * - Valor literal
     * - Operación matemática
     */
    private void validarAsignacion(List<Token> tokens, String linea, int numeroLinea, Token tipoDeclarado) {

        if (!tokens.get(4).es("OPERATOR", "=")) {
            errorManager.agregarError(ErrorCode.FALTA_IGUAL, linea, numeroLinea);
            return;
        }

        if (tokens.size() < 6) {
            errorManager.agregarError(ErrorCode.FALTA_VALOR, linea, numeroLinea);
            return;
        }

        // Caso 1: Asignación simple (literal)
        if (tokens.size() == 6) {
            validarCompatibilidad(tipoDeclarado, tokens.get(5), linea, numeroLinea);
            return;
        }

        // Caso 2: Operación matemática
        validarOperacionMatematica(tokens, linea, numeroLinea, tipoDeclarado);
    }

    /**
     * Validación de compatibilidad tipo–valor literal.
     */
    private void validarCompatibilidad(Token tipo, Token valor, String linea, int numeroLinea) {

        String t = tipo.lexema.toLowerCase();

        switch (t) {

            case "integer":
                if (valor.type != TokenType.NUMBER || valor.lexema.contains(".")) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            case "double":
                if (valor.type != TokenType.NUMBER) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            case "string":
                if (valor.type != TokenType.STRING_LITERAL) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            case "boolean":
                if (!valor.lexema.equalsIgnoreCase("True") &&
                    !valor.lexema.equalsIgnoreCase("False")) {
                    errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
                }
                break;

            default:
                errorManager.agregarError(ErrorCode.TIPO_INVALIDO, linea, numeroLinea);
        }
    }

    /**
     * Validación de operaciones matemáticas:
     * - operandos numéricos
     * - variables declaradas
     * - operadores válidos
     */
    private void validarOperacionMatematica(List<Token> tokens, String linea, int numeroLinea, Token tipoDeclarado) {

        for (int i = 5; i < tokens.size(); i++) {

            Token t = tokens.get(i);

            // Operadores válidos
            if (t.type == TokenType.OPERATOR) {
                if (!t.lexema.matches("[+\\-*/]")) {
                    errorManager.agregarError(ErrorCode.OPERADOR_INVALIDO, linea, numeroLinea);
                }
                continue;
            }

            // Números
            if (t.type == TokenType.NUMBER) {
                continue;
            }

            // Identificadores → deben existir y ser numéricos
            if (t.type == TokenType.IDENTIFIER) {

                if (!symbolTable.existe(t.lexema)) {
                    errorManager.agregarError(ErrorCode.VARIABLE_NO_DECLARADA, linea, numeroLinea);
                    continue;
                }

                String tipoVar = symbolTable.tipoDe(t.lexema);

                if (!tipoVar.equals("integer") && !tipoVar.equals("double")) {
                    errorManager.agregarError(ErrorCode.OPERANDO_NO_NUMERICO, linea, numeroLinea);
                }

                continue;
            }

            // Cualquier otro token es inválido
            errorManager.agregarError(ErrorCode.OPERANDO_INVALIDO, linea, numeroLinea);
        }

        // Validar compatibilidad del resultado
        if (tipoDeclarado.lexema.equalsIgnoreCase("String") ||
            tipoDeclarado.lexema.equalsIgnoreCase("Boolean")) {
            errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
        }
    }
}
