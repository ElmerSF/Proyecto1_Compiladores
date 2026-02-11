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

    // Control del orden lógico del archivo
    private boolean moduleEncontrado = false;

    // ⭐ PUNTO 7: Control de Imports y Module
    private boolean importsEncontrado = false;
    private boolean moduleValidado = false;

    // ⭐ PUNTO 8: Control de End Module
    // cantidadEndModule → cuántos End Module VÁLIDOS se han encontrado
    // lineaEndModule → línea del ÚLTIMO End Module válido
    private int cantidadEndModule = 0;
    private int lineaEndModule = -1;

    public Validador(ErrorManager errorManager, SymbolTable symbolTable) {
        this.errorManager = errorManager;
        this.symbolTable = symbolTable;
    }

    // ============================================================
    // MÉTODO PRINCIPAL DE VALIDACIÓN POR LÍNEA
    // ============================================================
    public void validarLinea(List<Token> tokens, String linea, int numeroLinea) {

        // Línea vacía o sin tokens útiles → no se valida nada
        if (tokens == null || tokens.isEmpty()) {
            return;
        }

        Token primero = tokens.get(0);

        // ⭐ PUNTO 8: Detectar End Module PRIMERO
        // Esto garantiza que End Module se procese antes que cualquier otra regla.
        if (esEndModule(tokens)) {
            validarEndModule(tokens, linea, numeroLinea);
            return;
        }

        // ⭐ PUNTO 9: Comentarios en Visual Basic
        // Regla: solo se considera comentario válido si la línea INICIA con '
        // Si la línea NO inicia con ' pero contiene ' en medio → comentario inválido para este proyecto.
        String lineaTrim = linea.trim();
        if (!lineaTrim.startsWith("'") && linea.contains("'")) {
            // Comentario inválido al final o en medio de la línea
            errorManager.agregarError(ErrorCode.COMENTARIO_INVALIDO, linea, numeroLinea);
            return;
        }

        // ⭐ PUNTO 8 – CÓDIGO DESPUÉS DE UN END MODULE VÁLIDO:
        // Si ya hubo un End Module válido y aparece cualquier otra instrucción,
        // eso significa que hay código después de End Module → debe marcar error.
        if (cantidadEndModule > 0) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_NO_ES_ULTIMA_LINEA,
                    linea,
                    numeroLinea
            );

            // Invalida el End Module anterior, porque ya no es la última línea de código
            cantidadEndModule = 0;
            lineaEndModule = -1;
        }

        // ⭐ PUNTO 7: Detectar Imports
        if (primero.es("RESERVED_WORD", "Imports")) {
            importsEncontrado = true;
            return;
        }

        // ⭐ PUNTO 7: Validación completa del Module
        if (primero.es("RESERVED_WORD", "Module")) {
            validarModule(tokens, linea, numeroLinea);
            return;
        }

        // Compatibilidad con tu lógica original
        if (primero.es("RESERVED_WORD", "Module")) {
            moduleEncontrado = true;
            return;
        }

        // ⭐ PUNTO 5: Validación de declaraciones Dim
        if (primero.es("RESERVED_WORD", "Dim")) {

            if (!moduleEncontrado) {
                errorManager.agregarError(ErrorCode.DIM_ANTES_DE_MODULE, linea, numeroLinea);
                return;
            }

            validarDeclaracionDim(tokens, linea, numeroLinea);
            return;
        }

        // ⭐ PUNTO 6: Validación de Console.WriteLine(...)
        if (esConsoleWriteLine(tokens)) {
            validarConsoleWriteLine(tokens, linea, numeroLinea);
        }
    }

    // ============================================================
    // ⭐ PUNTO 8: Detectar End Module
    // ============================================================
    private boolean esEndModule(List<Token> tokens) {

        if (tokens.size() < 2) return false;

        return tokens.get(0).es("RESERVED_WORD", "End") &&
               tokens.get(1).es("RESERVED_WORD", "Module");
    }

    // ============================================================
    // ⭐ PUNTO 8: Validación del End Module
    // ============================================================
    private void validarEndModule(List<Token> tokens, String linea, int numeroLinea) {

        // 1. Validar espacio exacto entre End y Module
        int indexEnd = linea.indexOf("End");
        int indexModule = linea.indexOf("Module");

        if (indexModule - indexEnd != 4) {
            errorManager.agregarError(ErrorCode.ENDMODULE_ESPACIO_INCORRECTO, linea, numeroLinea);
            return;
        }

        // 2. Validar que NO haya tokens extra en la misma línea
        if (tokens.size() > 2) {
            errorManager.agregarError(ErrorCode.ENDMODULE_TIENE_TOKENS_EXTRA, linea, numeroLinea);
            return;
        }

        // 3. Registrar End Module como válido
        cantidadEndModule++;
        lineaEndModule = numeroLinea;
    }

    // ============================================================
    // ⭐ PUNTO 8: Validación al final del archivo
    // ============================================================
    public void validarFinDeArchivo(int ultimaLineaConContenido) {

        // Caso 1: No apareció ningún End Module válido
        if (cantidadEndModule == 0) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_NO_ES_ULTIMA_LINEA,
                    "Fin de archivo",
                    ultimaLineaConContenido
            );
            return;
        }

        // Caso 2: Apareció más de un End Module válido
        if (cantidadEndModule > 1) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_DUPLICADO,
                    "Fin de archivo",
                    lineaEndModule
            );
        }

        // Caso 3: El End Module válido NO es la última línea con contenido
        if (lineaEndModule != ultimaLineaConContenido) {
            errorManager.agregarError(
                    ErrorCode.ENDMODULE_NO_ES_ULTIMA_LINEA,
                    "Fin de archivo",
                    ultimaLineaConContenido
            );
        }
    }

    // ============================================================
    // ⭐ PUNTO 7: Validación del Module
    // ============================================================
    private void validarModule(List<Token> tokens, String linea, int numeroLinea) {

        if (!importsEncontrado) {
            errorManager.agregarError(ErrorCode.MODULE_ANTES_DE_IMPORTS, linea, numeroLinea);
            return;
        }

        if (tokens.size() < 2) {
            errorManager.agregarError(ErrorCode.MODULE_SIN_IDENTIFICADOR, linea, numeroLinea);
            return;
        }

        Token identificador = tokens.get(1);

        if (identificador.type != TokenType.IDENTIFIER) {
            errorManager.agregarError(ErrorCode.MODULE_SIN_IDENTIFICADOR, linea, numeroLinea);
            return;
        }

        int indexModule = linea.indexOf("Module");
        int indexIdent = linea.indexOf(identificador.lexema);

        if (indexIdent - indexModule != 7) {
            errorManager.agregarError(ErrorCode.MODULE_ESPACIO_INCORRECTO, linea, numeroLinea);
            return;
        }

        moduleValidado = true;
        moduleEncontrado = true;
    }

    // ============================================================
    // FUNCIÓN AUXILIAR: Detectar Console.WriteLine
    // ============================================================
    private boolean esConsoleWriteLine(List<Token> tokens) {

        if (tokens.size() < 3) {
            return false;
        }

        return tokens.get(0).type == TokenType.IDENTIFIER &&
               tokens.get(0).lexema.equalsIgnoreCase("Console") &&
               tokens.get(1).lexema.equals(".") &&
               tokens.get(2).type == TokenType.IDENTIFIER &&
               tokens.get(2).lexema.equalsIgnoreCase("WriteLine");
    }

    // ============================================================
    // PUNTO 6: Validación de Console.WriteLine
    // ============================================================
    private void validarConsoleWriteLine(List<Token> tokens, String linea, int numeroLinea) {

        // 1. Detectar strings sin cerrar o comillas Unicode
        for (int i = 3; i < tokens.size(); i++) {
            Token t = tokens.get(i);

            if (t.type == TokenType.STRING_LITERAL) {
                if (!t.lexema.startsWith("\"") || !t.lexema.endsWith("\"")) {
                    errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                    return;
                }
            }

            if (t.lexema.equals("“") || t.lexema.equals("”")) {
                errorManager.agregarError(ErrorCode.STRING_SIN_CERRAR, linea, numeroLinea);
                return;
            }
        }

        // 2. Validar paréntesis de cierre
        Token ultimo = tokens.get(tokens.size() - 1);

        if (!ultimo.es("SYMBOL", ")")) {
            errorManager.agregarError(ErrorCode.PARENTESIS_FALTANTE, linea, numeroLinea);
            return;
        }

        // 3. Validar paréntesis vacíos
        if (tokens.size() == 5) {
            errorManager.agregarError(ErrorCode.PARENTESIS_VACIOS, linea, numeroLinea);
            return;
        }

        // 4. Validar strings sin cerrar dentro de los paréntesis
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

    // ============================================================
    // PUNTO 5: Declaraciones Dim
    // ============================================================
    private void validarDeclaracionDim(List<Token> tokens, String linea, int numeroLinea) {

        if (tokens.size() < 4) {
            errorManager.agregarError(ErrorCode.DECLARACION_INCOMPLETA, linea, numeroLinea);
            return;
        }

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

        Token asToken = tokens.get(2);

        if (asToken.type == TokenType.IDENTIFIER &&
            tokens.size() > 3 &&
            tokens.get(3).es("RESERVED_WORD", "As")) {

            errorManager.agregarError(ErrorCode.IDENTIFICADOR_CON_ESPACIOS, linea, numeroLinea);
            return;
        }

        if (!asToken.es("RESERVED_WORD", "As")) {
            errorManager.agregarError(ErrorCode.FALTA_AS, linea, numeroLinea);
        }

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

        if (tokens.size() > 4) {
            validarAsignacion(tokens, linea, numeroLinea, tipo);
        }

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
