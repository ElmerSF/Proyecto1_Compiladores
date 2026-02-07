/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los errores encontrados
*/
package proyecto1;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ErrorManager {

    private final List<Error> errores = new ArrayList<>();

    /**
     * Agrega un error a la lista.
     */
        public void agregarError(ErrorCode codigo, String linea, int numeroLinea) {
        errores.add(new Error(codigo, numeroLinea, linea));
    }

    /**
     * Devuelve la lista de errores.
     */
    public List<Error> getErrores() {
        return errores;
    }

    /**
     * Escribe los errores en un archivo .log
     */
    public void escribirLog(String nombreArchivo) {
        try {
            FileWriter fw = new FileWriter(nombreArchivo);
            PrintWriter pw = new PrintWriter(fw);

            if (errores.isEmpty()) {
                pw.println("No se encontraron errores.");
            } else {
                for (Error e : errores) {
                    pw.println(e.toString());
                }
            }

            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println("Error al escribir archivo log: " + e.getMessage());
        }
    }
}
