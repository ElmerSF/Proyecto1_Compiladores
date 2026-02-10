/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase principal donde se inicia
*/

package proyecto1;

public class Main {

    static ErrorManager errorManager = new ErrorManager();
    static String directorio = System.getProperty("user.dir");

    public static void main(String[] args) {

        // Mensaje de bienvenida
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println("\t Analizador Léxico   Proyecto 1 UNED Estudiante: Elmer Salazar (3-426-158)");
        System.out.println("--------------------------------------------------------------------------------------------------------------\n");

        //si no se recibe ningún documento
        if (args.length == 0) {
            System.out.println("No se indicó ningún archivo como argumento.");
            return;
        }

        String archivo = args[0];

        //si el archivo no tiene la extensión vb la correcta
        if (!archivo.toLowerCase().endsWith(".vb")) {
            System.out.println("El archivo debe tener extensión .vb");
            return;
        }

        System.out.println("Ubicación actual: " + directorio);
        System.out.println("Archivo recibido: " + archivo + "\n");

        // ASCII ART
        System.out.println("\033[32m                          .-\"\"\"-.");
        System.out.println("\033[32m                         / .===. \\");
        System.out.println("\033[32m                         \\/ 6 6 \\/");
        System.out.println("\033[32m                         ( \\___/ )");
        System.out.println("\033[32m    _________________ooo__\\_____/_____________________");
        System.out.println("\033[32m   /                                                  \\");
        System.out.println("\033[36m   |ANALIZADOR LÉXICO   Proyecto 1 Compiladores 03307  |");
        System.out.println("\033[32m   \\______________________________ooo_________________/");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |_ | _|");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |__|__|");
        System.out.println("\033[32m                         /-'Y'-\\");
        System.out.println("\033[32m                        (__/ \\__)\n");
        System.out.println("\033[0m");

        // Leer archivo
        FileManager fm = new FileManager();
        
        //recibimos un arreglo de líneas
        String[] lineas = fm.leerArchivo(archivo);

        if (lineas == null) {
            System.out.println("No se pudo leer el archivo.");
            return;
        }
        if (lineas.length == 0) {
            System.out.println("El archivo está vacío.");
            return;
        }

        
        System.out.println("=== DEBUG DE LÍNEAS ===");
for (int i = 0; i < lineas.length; i++) {
    System.out.println("[" + (i+1) + "] (" + lineas[i].length() + ") -> '" + lineas[i] + "'");
}
System.out.println("========================");

        
        
        // Crear archivo .log
        String archivoLog = fm.crearArchivoLog(archivo, lineas);

        // Instancias principales
        SymbolTable symbolTable = new SymbolTable();
        Lexer lexer = new Lexer();
        Validador validator = new Validador(errorManager, symbolTable);

        // Procesar línea por línea
        for (int i = 0; i < lineas.length; i++) {

            int numeroLinea = i + 1;
            String linea = lineas[i];
            
            var tokens = lexer.tokenizar(linea);

            // Debug de tokens
            fm.escribirTokensDebug(tokens, numeroLinea);

            // Validación
            validator.validarLinea(tokens, linea, numeroLinea);
        }

        // ⭐ CÓDIGO NUEVO PUNTO 8:
        // Calcular la última línea con contenido (ignorando líneas vacías o invisibles al final)
        int ultimaLineaConContenido = lineas.length;

        while (ultimaLineaConContenido > 0) {

            String linea = lineas[ultimaLineaConContenido - 1];

            // Limpieza EXTREMA: elimina TODO lo que no sea visible
            String limpia = linea
                    .replaceAll("[\\s\\u00A0\\u200B\\uFEFF\\u202F\\u205F\\u3000]+", "")
                    .replace("\uFEFF", "")   // BOM
                    .trim();

            if (limpia.isEmpty()) {
                ultimaLineaConContenido--;
            } else {
                break;
            }
        }

        if (ultimaLineaConContenido == 0) {
            ultimaLineaConContenido = 1;
        }

        // Validar que End Module sea la última línea con contenido del archivo
        validator.validarFinDeArchivo(ultimaLineaConContenido);

        // Generar archivo de tabla de símbolos
        fm.generarDebugSymbolTable(symbolTable);

        // Escribir errores en el log
        fm.escribirErrores(archivoLog, errorManager);

        // Barra de progreso
        mostrarBarraProgreso();

        System.out.println("Archivo generado: " + archivoLog);
    }

    // -------------------------------
    // BARRA DE PROGRESO
    // -------------------------------
    public static void mostrarBarraProgreso() {
        System.out.print("\nProcesando: \033[32m");
        for (int i = 0; i < 50; i++) {
            System.out.print(">");
            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {}
        }
        System.out.println("\033[0m 100%");
    }
}
