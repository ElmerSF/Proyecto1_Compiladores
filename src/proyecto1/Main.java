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
        //le pasamos de argunmento el archivo a la función leer archivo y recibimos 
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

        // Crear archivo .log le pasamos el nombre del archivo y el arreglo de líneas
        //nos regresa el nombre del log
        String archivoLog = fm.crearArchivoLog(archivo, lineas);

        // Instancias principales
        SymbolTable symbolTable = new SymbolTable();
        Lexer lexer = new Lexer();
        Validador validator = new Validador(errorManager, symbolTable);

        // Procesar línea por línea
        for (int i = 0; i < lineas.length; i++) {

            int numeroLinea = i + 1;
            String linea = lineas[i];
            
            //le pasamos la línea a la función tokenizar
            //nos devuelve un arreglo de lista
            var tokens = lexer.tokenizar(linea);

            // Debug de tokens
            //Esto lo puse para obener un arhivo txt con los token para revisar
            fm.escribirTokensDebug(tokens, numeroLinea);

            // Validación le pasamos el token, la línea y el número de línea
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
    // BARRA DE PROGRESO esto lo uso de apoyo visual en el cmd
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
