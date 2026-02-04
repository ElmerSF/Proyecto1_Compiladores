/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los archivos 
lectura del .vb
creación del .log 
*/


/**
 *
 * @author elmer
 */
package proyecto1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de:
 *  - Leer el archivo .vb línea por línea
 *  - Crear el archivo .log con numeración de 4 dígitos
 *  - Escribir los errores detectados por el analizador
 *
 * Importante:
 *  - Aquí NO se hace análisis léxico ni sintáctico.
 *  - Solo se maneja entrada/salida de archivos.
 */
public class FileManager {

    /**
     * Lee un archivo de texto (.vb) y devuelve todas las líneas
     * en un arreglo de String, respetando el formato original.
     *
     * @param nombreArchivo Nombre del archivo a leer (con extensión)
     * @return Arreglo de líneas del archivo, o null si hubo error
     */
    public String[] leerArchivo(String nombreArchivo) {

        try {
            File archivo = new File(nombreArchivo);

            if (!archivo.exists()) {
                System.out.println("El archivo indicado no existe: " + nombreArchivo);
                return null;
            }

            ArrayList<String> lineas = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            // Leemos línea por línea SIN modificar espacios ni formato
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            br.close();

            return lineas.toArray(new String[0]);

        } catch (Exception e) {
            System.out.println("!!!Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea el archivo .log a partir del archivo original .vb.
     * - El nombre del .log es: nombreArchivo-errores.log
     * - Copia todas las líneas del .vb
     * - Agrega al inicio de cada línea un número de 4 dígitos.
     *
     * @param nombreArchivo Nombre del archivo original (.vb)
     * @param lineas        Líneas leídas del archivo original
     * @return Nombre del archivo .log generado
     */
    public String crearArchivoLog(String nombreArchivo, String[] lineas) {

        try {
            // Obtener nombre base sin extensión
            String nombreBase;

            int punto = nombreArchivo.lastIndexOf('.');
            if (punto > 0) {
                nombreBase = nombreArchivo.substring(0, punto);
            } else {
                nombreBase = nombreArchivo;
            }

            String nombreLog = nombreBase + "-errores.log";

            FileWriter fw = new FileWriter(nombreLog, false); // false = sobrescribir
            PrintWriter pw = new PrintWriter(fw);

            // Escribimos cada línea con numeración de 4 dígitos
            for (int i = 0; i < lineas.length; i++) {
                int numeroLinea = i + 1;
                String numeroFormateado = String.format("%04d", numeroLinea);
                String contenido = lineas[i];

                // Respetar exactamente el contenido original
                pw.println(numeroFormateado + " " + contenido);
            }

            pw.flush();
            pw.close();
            fw.close();

            return nombreLog;

        } catch (Exception e) {
            System.out.println("Error al crear el archivo .log: " + e.getMessage());
            return null;
        }
    }

    /**
     * Escribe los errores en el archivo .log.
     * Aquí estamos implementando la Opción 2 del enunciado:
     *  - Todos los errores se listan al final del archivo .log
     *  - Se indica el número de línea y la descripción.
     *
     * @param nombreLog     Nombre del archivo .log ya creado
     * @param errorManager  Administrador de errores con la lista de errores
     */
    public void escribirErrores(String nombreLog, ErrorManager errorManager) {

        try {
            if (nombreLog == null) {
                System.out.println("No se puede escribir errores: archivo .log nulo.");
                return;
            }

            List<Error> errores = errorManager.getErrores();

            // Si no hay errores, no agregamos nada extra
            if (errores == null || errores.isEmpty()) {
                return;
            }

            // Abrimos el .log en modo append (true)
            FileWriter fw = new FileWriter(nombreLog, true);
            PrintWriter pw = new PrintWriter(fw);

            pw.println();
            pw.println("------------------------------------------------------------");
            pw.println("ERRORES DETECTADOS:");
            pw.println("------------------------------------------------------------");

            for (Error err : errores) {
                // Formato sugerido:
                // Error 200. Línea 0001. Descripción...
                String lineaFormateada = String.format("%04d", err.getLinea());
                pw.println("Error " + err.getNumero() + ". Línea " + lineaFormateada + ". " + err.getDescripcion());
            }

            pw.flush();
            pw.close();
            fw.close();

        } catch (Exception e) {
            System.out.println("Error al escribir errores en el .log: " + e.getMessage());
        }
    }
}
