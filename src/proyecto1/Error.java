/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de eror de forma individual
*/
package proyecto1;

public class Error {

    private final int numeroLinea;
    private final String descripcion;
    private final String lineaOriginal;

    public Error(int numeroLinea, String descripcion, String lineaOriginal) {
        this.numeroLinea = numeroLinea;
        this.descripcion = descripcion;
        this.lineaOriginal = lineaOriginal;
    }

    public int getLinea() {
        return numeroLinea;
    }

    public String getNumero() {
        // Si deseas numerar errores, puedes devolver un ID.
        // Por ahora devolvemos el número de línea como identificador.
        return String.valueOf(numeroLinea);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getLineaOriginal() {
        return lineaOriginal;
    }

    @Override
    public String toString() {
        return "Línea " + numeroLinea + ": " + descripcion + " → \"" + lineaOriginal + "\"";
    }
}
