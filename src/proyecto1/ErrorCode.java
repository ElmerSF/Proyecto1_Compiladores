/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1;

public enum ErrorCode {

    // --- Errores del punto 5 (Dim) ---
    DECLARACION_INCOMPLETA(100, "Declaración Dim incompleta."),
    IDENTIFICADOR_INVALIDO(101, "Se esperaba un identificador después de 'Dim'."),
    FALTA_AS(102, "Se esperaba la palabra reservada 'As'."),
    TIPO_INVALIDO(103, "Tipo de dato inválido en declaración Dim."),
    FALTA_IGUAL(104, "Se esperaba '=' para asignación."),
    FALTA_VALOR(105, "Falta el valor después de '='."),
    VALOR_NO_COMPATIBLE(106, "El valor asignado no es compatible con el tipo declarado."),
    TOKENS_EXTRA(107, "Tokens extra después de la declaración de variable."),
    DIM_ANTES_DE_MODULE(108, "Las declaraciones Dim deben aparecer después del Module."),

    // --- Errores para operaciones matemáticas ---
    OPERADOR_INVALIDO(109, "Operador inválido en la operación matemática."),
    VARIABLE_NO_DECLARADA(110, "Se está utilizando una variable no declarada."),
    OPERANDO_NO_NUMERICO(111, "El operando no es numérico y no puede usarse en una operación matemática."),
    OPERANDO_INVALIDO(112, "Operando inválido en la operación matemática.");

    private final int codigo;
    private final String mensaje;

    ErrorCode(int codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }
}
