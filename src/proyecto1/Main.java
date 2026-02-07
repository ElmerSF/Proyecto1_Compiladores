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

        if (args.length == 0) {
            System.out.println("No se indicó ningún archivo como argumento.");
            return;
        }

        String archivo = args[0];

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
        String[] lineas = fm.leerArchivo(archivo);

        if (lineas == null) {
            System.out.println("No se pudo leer el archivo.");
            return;
        }
        if (lineas.length == 0) {
            System.out.println("El archivo está vacío.");
            return;
        }

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

        // Generar archivo de tabla de símbolos
        fm.generarDebugSymbolTable(symbolTable);
        //generarDebugSymbolTable(symbolTable);

        // Escribir errores en el log
        fm.escribirErrores(archivoLog, errorManager);

        // Barra de progreso
        mostrarBarraProgreso();

        System.out.println("Archivo generado: " + archivoLog);
    }

    // -------------------------------
    // GENERAR ARCHIVO SYMBOLTABLE_DEBUG esto lo pase a FileManager
    // -------------------------------
   /* private static void generarDebugSymbolTable(SymbolTable symbolTable) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter("symboltable_debug.txt")) {

            writer.println("TABLA DE SÍMBOLOS");
            writer.println("------------------");

            for (var entry : symbolTable.obtenerVariables().entrySet()) {
                writer.println("Variable: " + entry.getKey() + "   Tipo: " + entry.getValue());
            }

        } catch (Exception e) {
            System.out.println("Error al generar symboltable_debug.txt: " + e.getMessage());
        }
    }*/

    // -------------------------------
    // BARRA DE PROGRESO
    // -------------------------------
    public static void mostrarBarraProgreso() {
        System.out.print("\nProcesando: \033[32m");
        for (int i = 0; i < 50; i++) {
            System.out.print(">");
            try {
                Thread.sleep(90);
            } catch (InterruptedException e) {}
        }
        System.out.println("\033[0m 100%");
    }
}
