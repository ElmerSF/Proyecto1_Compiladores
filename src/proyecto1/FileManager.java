/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de los archivos 
Leer el archivo .vb línea por línea
- Crear el archivo .log con numeración de 4 dígitos
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
            //areglo de lienas
            ArrayList<String> lineas = new ArrayList<>();

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            String linea;

            // Leemos línea por línea SIN modificar espacios ni formato
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }

            br.close();
                //se retorna un arreglo de líneas
            return lineas.toArray(new String[0]);

        } catch (Exception e) {
            System.out.println("!!!Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }
    //esto es solo temporal para revisar los token
    //entrega un txt con los token clasificados
    public void escribirTokensDebug(List<Token> tokens, int numeroLinea) {
    try {
        FileWriter fw = new FileWriter("tokens_debug.txt", true);
        PrintWriter pw = new PrintWriter(fw);

        pw.println("Línea " + String.format("%04d", numeroLinea) + ":");

        for (Token t : tokens) {
            pw.println(t.toString());
        }

        pw.println(); // línea en blanco

        pw.flush();
        pw.close();
        fw.close();

    } catch (Exception e) {
        System.out.println("Error al escribir tokens de debug: " + e.getMessage());
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

            // <editor-fold defaultstate="collapsed" desc="Intento para manejar la numeración del archivo">
 /*
                  if (contador < 8) {

                    pendiente = (pendiente + " " + revi.AnalizaTexto(linea)+" "+revi.ada());
                    reglonerror.println("0000" + contador + " " + linea + " ");

                    contador++;
                } else {
                    if (contador == 8) {
                        Respuesta = (Respuesta + revi.AnalizaTexto(linea));
                        reglonerror.println("0000" + contador + " " + linea + " " + pendiente + Respuesta);
                        contador++;
                    } else {

                        if (contador <10) {
                            Respuesta = (Respuesta + revi.AnalizaTexto(linea));
                            reglonerror.println("0000" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                            contador++;
                        } else {
                            if (contador < 100) {
                                Respuesta = revi.AnalizaTexto(linea);
                                reglonerror.println("000" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                contador++;
                            } else {
                                if (contador < 1000) {
                                    Respuesta = revi.AnalizaTexto(linea);
                                    reglonerror.println("00" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                    contador++;
                                } else {
                                    if (contador < 10000) {
                                        Respuesta = revi.AnalizaTexto(linea);
                                        reglonerror.println("0" + contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                        contador++;
                                    } else {
                                        Respuesta = revi.AnalizaTexto(linea);
                                        reglonerror.println(contador + " " + linea + " " + Respuesta+" "+revi.ada());
                                        contador++;

                                    }
                                }
                            }
                        }
                    }
                }

            
            */
// </editor-fold>
   
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
    // esto es para escribir un archivo txt para validación y revisión de las variables declaradas 
    public void generarDebugSymbolTable(SymbolTable symbolTable) {
    try (PrintWriter writer = new PrintWriter("symboltable_debug.txt")) {

        writer.println("TABLA DE SÍMBOLOS");
        writer.println("------------------");

        for (var entry : symbolTable.obtenerVariables().entrySet()) {
            writer.println("Variable: " + entry.getKey() + "   Tipo: " + entry.getValue());
        }

    } catch (Exception e) {
        System.out.println("Error al generar symboltable_debug.txt: " + e.getMessage());
    }
}

}
