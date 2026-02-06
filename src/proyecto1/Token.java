/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los token individuales
*/

package proyecto1;

public class Token {

    private String valor;
    private TokenType tipo;

    public Token(String valor, TokenType tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public TokenType getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return "[" + tipo + " : " + valor + "]";
    }
}
