/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Enumeración de códigos de error del analizador
*/

package proyecto1;

public enum ErrorCode {

    IDENTIFICADOR_INVALIDO(
        101,
        "Se esperaba un identificador después de 'Dim'."
    ),

    FALTA_AS(
        102,
        "Se esperaba la palabra reservada 'As'."
    ),

    TIPO_INVALIDO(
        103,
        "Tipo de dato inválido en declaración Dim."
    ),

    VALOR_NO_COMPATIBLE(
        106,
        "El valor asignado no es compatible con el tipo declarado."
    ),

    TOKENS_EXTRA(
        107,
        "Tokens extra después de la declaración de variable."
    ),

    DIM_ANTES_DE_MODULE(
        108,
        "Las declaraciones Dim deben aparecer después del Module."
    ),

    OPERANDO_NO_NUMERICO(
        109,
        "Operando no numérico en la operación matemática."
    ),

    VARIABLE_NO_DECLARADA(
        110,
        "Se está utilizando una variable que no ha sido declarada."
    ),

    OPERADOR_INVALIDO(
        111,
        "Operador inválido en la operación matemática."
    ),

    OPERANDO_INVALIDO(
        112,
        "Operando inválido en la operación matemática."
    ),

    USO_PALABRA_RESERVADA_COMO_TIPO(
        113,
        "Los tipos de variables solamente pueden ser: (Integer, String, Boolean y Byte). No se puede usar una palabra reservada como tipo."
    ),

    USO_PALABRA_RESERVADA_COMO_IDENTIFICADOR(
        114,
        "No se puede usar una palabra reservada como identificador."
    ),

    DECLARACION_INCOMPLETA(
        115,
        "Declaración Dim incompleta."
    ),

    FALTA_IGUAL(
        116,
        "Se esperaba el operador '=' en la asignación."
    ),

    FALTA_VALOR(
        117,
        "Se esperaba un valor después del '='."
    ),

    //  identificadores inválidos por forma
    IDENTIFICADOR_INICIA_CON_GUION_BAJO(
        118,
        "Un identificador no puede iniciar con '_'."
    ),

    IDENTIFICADOR_INICIA_CON_NUMERO(
        119,
        "Un identificador no puede iniciar con un número."
    ),
        IDENTIFICADOR_CON_ESPACIOS(
        120,
        "Un identificador no puede contener espacios."
    );

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

    @Override
    public String toString() {
        return "Error " + codigo + ". " + mensaje;
    }
}
