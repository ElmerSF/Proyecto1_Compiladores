/*
UNED Informática Compiladores 3307
Estudiante: Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase principal donde se inicia
se hace respaldo en git
y se ajusta
*/

package proyecto1;

public class Main {

    static ErrorManager errorManager = new ErrorManager(); //instanciamos la clase ErrorManager
    static String directorio = System.getProperty("user.dir"); // variable directorio le pasamos la información de la carpeta

    public static void main(String[] args) {

        // Mensaje de bienvenida
        System.out.println("--------------------------------------------------------------------------------------------------------------");
        System.out.println("\t Analizador Léxico   Proyecto 1 UNED");
        System.out.println("--------------------------------------------------------------------------------------------------------------\n");

        // Validación: se requiere un archivo como argumento
        if (args.length == 0) {
            System.out.println("No se indicó ningún archivo como argumento.");
            System.out.println("¡Recuerde que se requiere un archivo .vb para analizar!");
            return;
        }

        String archivo = args[0];

        // Validación de extensión
        if (!archivo.toLowerCase().endsWith(".vb")) {
            System.out.println("El archivo debe tener extensión .vb");
            return;
        }

        // Mostrar ubicación
        System.out.println("Ubicación actual: " + directorio);
        System.out.println("Archivo recibido: " + archivo + "\n");

        // Encabezado visual estilo personalizado
        System.out.println("\033[32m                          .-\"\"\"-.");
        System.out.println("\033[32m                         / .===. \\");
        System.out.println("\033[32m                         \\/ 6 6 \\/");
        System.out.println("\033[32m                         ( \\___/ )");
        System.out.println("\033[32m    _________________ooo__\\_____/_____________________");
        System.out.println("\033[32m   /                                                  \\");
        System.out.println("\033[36m  |   ANALIZADOR LÉXICO   Proyecto 1 Compiladores      |");
        System.out.println("\033[32m   \\______________________________ooo_________________/");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |_ | _|");
        System.out.println("\033[32m                         |  |  |");
        System.out.println("\033[32m                         |__|__|");
        System.out.println("\033[32m                         /-'Y'-\\");
        System.out.println("\033[32m                        (__/ \\__)\n");

        // Reseteamos el color
        System.out.println("\033[0m");

        // Iniciar lectura del archivo
        FileManager fm = new FileManager(); //instanciamos la clase FileManager
        String[] lineas = fm.leerArchivo(archivo); //le pasamos el argumento archivo a la función leer archivo

        if (lineas == null) {// si no se logra leer el arhivo
            System.out.println("No se pudo leer el archivo.");
            return;
        }
        if (lineas.length == 0) {//si el archivo está vacío
            System.out.println("El archivo está vacío. Se completó la lectura."); 
            return; 
        }

        // Crear archivo .log numerado
        String archivoLog = fm.crearArchivoLog(archivo, lineas);

        Lexer lexer = new Lexer();
        Validador validator = new Validador(errorManager);

        // Procesar línea por línea
        for (int i = 0; i < lineas.length; i++) {
            int numeroLinea = i + 1;
            String linea = lineas[i];

            var tokens = lexer.tokenizar(linea);

            // Validación de la línea
            validator.validarLinea(tokens, linea, numeroLinea);
        }

        // Escribir errores en el log
        fm.escribirErrores(archivoLog, errorManager);

        System.out.println("Proceso finalizado.");
        System.out.println("Archivo generado: " + archivoLog);
    }
}
