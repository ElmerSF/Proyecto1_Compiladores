/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de eror de forma individual
*/
package proyecto1;

public class Error {

    private final ErrorCode codigo;
    private final int numeroLinea;
    private final String lineaOriginal;

    public Error(ErrorCode codigo, int numeroLinea, String lineaOriginal) {
        this.codigo = codigo;
        this.numeroLinea = numeroLinea;
        this.lineaOriginal = lineaOriginal;
    }

    public int getNumero() {
        return codigo.getCodigo();
    }

    public int getLinea() {
        return numeroLinea;
    }

    public String getDescripcion() {
        return codigo.getMensaje();
    }

    public String getLineaOriginal() {
        return lineaOriginal;
    }

    @Override
    public String toString() {
        return "Error " + getNumero() +
               ". Línea " + String.format("%04d", numeroLinea) +
               ". " + getDescripcion() +
               " → \"" + lineaOriginal + "\"";
    }
}
