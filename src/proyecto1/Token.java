/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los token individuales
*/

package proyecto1;

public class Token {

    public final String lexema;     // El texto original del token
    public final TokenType type;    // El tipo del token

    public Token(String lexema, TokenType type) {
        this.lexema = lexema;
        this.type = type;
    }

    public String getLexema() {
        return lexema;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[" + type + " : " + lexema + "]";
    }

    /**
     * Método para comparar tipo y lexema.
     * Ejemplo: token.es("RESERVED_WORD", "Dim")
     */
    public boolean es(String tipoEsperado, String lexemaEsperado) {
        return this.type.toString().equals(tipoEsperado)
                && this.lexema.equalsIgnoreCase(lexemaEsperado);
    }

    /**
     * Sobrecarga para comparar solo tipo.
     * Ejemplo: token.es("OPERATOR")
     */
    public boolean es(String tipoEsperado) {
        return this.type.toString().equals(tipoEsperado);
    }
}
