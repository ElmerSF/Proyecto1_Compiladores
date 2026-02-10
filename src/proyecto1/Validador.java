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

        // ============================================================
        // PUNTO 6: Validación de Console.WriteLine(...)
        // ============================================================
        if (esConsoleWriteLine(tokens)) {
            validarConsoleWriteLine(tokens, linea, numeroLinea);
            return;
        }
    }

    // ============================================================
    // FUNCIÓN AUXILIAR: Detectar Console.WriteLine
    // ============================================================
    private boolean esConsoleWriteLine(List<Token> tokens) {

        if (tokens.size() < 3) {
            return false;
        }

        // Patrón base: Console . WriteLine
        if (tokens.get(0).type == TokenType.IDENTIFIER &&
            tokens.get(0).lexema.equalsIgnoreCase("Console") &&
            tokens.get(1).lexema.equals(".") &&
            tokens.get(2).type == TokenType.IDENTIFIER &&
            tokens.get(2).lexema.equalsIgnoreCase("WriteLine")) {

            return true;
        }

        return false;
    }

    // ============================================================
    // VALIDACIÓN COMPLETA DEL PUNTO 6
    // ============================================================
    private void validarConsoleWriteLine(List<Token> tokens, String linea, int numeroLinea) {

        // ============================================================
        // 1. PRIMERO: detectar STRING SIN CERRAR
        //    Esto corrige los casos de las líneas 40 y 43.
        // ============================================================
        for (int i = 3; i < tokens.size(); i++) {
            Token t = tokens.get(i);

            if (t.type == TokenType.STRING_LITERAL) {

                // Si empieza con " pero NO termina con "
                if (!t.lexema.startsWith("\"") || !t.lexema.endsWith("\"")) {
                    errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                    return;
                }
            }

            // Si encontramos comillas Unicode (“ ”) → también es string sin cerrar
            if (t.lexema.equals("“") || t.lexema.equals("”")) {
                errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                return;
            }
        }

        // ============================================================
        // 2. Validar paréntesis de cierre
        // ============================================================
        Token ultimo = tokens.get(tokens.size() - 1);

        if (!ultimo.es("SYMBOL", ")")) {
            errorManager.agregarError(ErrorCode.PARENTESIS_FALTANTE, linea, numeroLinea);
            return;
        }

        // ============================================================
        // 3. Validar paréntesis vacíos
        // ============================================================
        if (tokens.size() == 5) {
            errorManager.agregarError(ErrorCode.PARENTESIS_VACIOS, linea, numeroLinea);
            return;
        }

        // ============================================================
        // 4. Validar strings sin cerrar cuando sí hay paréntesis
        // ============================================================
        for (int i = 4; i < tokens.size() - 1; i++) {
            Token t = tokens.get(i);

            if (t.type == TokenType.STRING_LITERAL) {
                if (!t.lexema.startsWith("\"") || !t.lexema.endsWith("\"")) {
                    errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                    return;
                }
            }
        }
    }

    /**
     * Validación del formato de declaración de variables (Punto 5).
     */
    private void validarDeclaracionDim(List<Token> tokens, String linea, int numeroLinea) {
        
        //la extensión mínima es de 4 token
        if (tokens.size() < 4) {
            errorManager.agregarError(ErrorCode.DECLARACION_INCOMPLETA, linea, numeroLinea);
            return;
        }

        // ============================================================
        // 1. VALIDACIÓN DEL IDENTIFICADOR
        // ============================================================

        Token identificador = tokens.get(1);

        if (identificador.type == TokenType.RESERVED_WORD) {
            errorManager.agregarError(ErrorCode.USO_PALABRA_RESERVADA_COMO_IDENTIFICADOR, linea, numeroLinea);

        } else if (identificador.type == TokenType.UNKNOWN) {

            String lex = identificador.lexema;

            if (lex.startsWith("_")) {
                errorManager.agregarError(ErrorCode.IDENTIFICADOR_INICIA_CON_GUION_BAJO, linea, numeroLinea);

            } else if (lex.matches("^[0-9].*")) {
                errorManager.agregarError(ErrorCode.IDENTIFICADOR_INICIA_CON_NUMERO, linea, numeroLinea);

            } else {
                errorManager.agregarError(ErrorCode.IDENTIFICADOR_INVALIDO, linea, numeroLinea);
            }

        } else if (identificador.type != TokenType.IDENTIFIER) {
            errorManager.agregarError(ErrorCode.IDENTIFICADOR_INVALIDO, linea, numeroLinea);
        }

        // ============================================================
        // 2. VALIDACIÓN DE "As"
        // ============================================================

        Token asToken = tokens.get(2);

        if (asToken.type == TokenType.IDENTIFIER
                && tokens.size() > 3
                && tokens.get(3).es("RESERVED_WORD", "As")) {

            errorManager.agregarError(ErrorCode.IDENTIFICADOR_CON_ESPACIOS, linea, numeroLinea);
            return;
        }

        if (!asToken.es("RESERVED_WORD", "As")) {
            errorManager.agregarError(ErrorCode.FALTA_AS, linea, numeroLinea);
        }

        // ============================================================
        // 3. VALIDACIÓN DEL TIPO
        // ============================================================

        Token tipo = tokens.get(3);

        boolean tipoEsValido = esTipoValido(tipo.lexema);

        if (tipo.type == TokenType.RESERVED_WORD && !tipoEsValido) {
            errorManager.agregarError(ErrorCode.USO_PALABRA_RESERVADA_COMO_TIPO, linea, numeroLinea);
            return;
        }

        if (!tipoEsValido) {
            errorManager.agregarError(ErrorCode.TIPO_INVALIDO, linea, numeroLinea);
            return;
        }

        symbolTable.registrar(identificador.lexema, tipo.lexema);

        // ============================================================
        // 4. VALIDACIÓN DE ASIGNACIÓN (opcional)
        // ============================================================

        if (tokens.size() > 4) {
            validarAsignacion(tokens, linea, numeroLinea, tipo);
        }

        // ============================================================
        // 5. TOKENS EXTRA (solo si NO hay operación matemática)
        // ============================================================

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

        if (tokens.size() == 6) {
            validarCompatibilidad(tipoDeclarado, tokens.get(5), linea, numeroLinea);
            return;
        }

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

            if (t.type == TokenType.OPERATOR) {
                if (!t.lexema.matches("[+\\-*/]")) {
                    errorManager.agregarError(ErrorCode.OPERADOR_INVALIDO, linea, numeroLinea);
                }
                continue;
            }

            if (t.type == TokenType.NUMBER) {
                continue;
            }

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

            errorManager.agregarError(ErrorCode.OPERANDO_INVALIDO, linea, numeroLinea);
        }

        if (tipoDeclarado.lexema.equalsIgnoreCase("String") ||
            tipoDeclarado.lexema.equalsIgnoreCase("Boolean")) {
            errorManager.agregarError(ErrorCode.VALOR_NO_COMPATIBLE, linea, numeroLinea);
        }
    }
}
