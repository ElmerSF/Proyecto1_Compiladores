/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para validación de los token
Valida reglas del lenguaje como palabras reservadas
*/
package proyecto1;

import java.util.List;

/**
 * Clase encargada de validar reglas del proyecto.
 * Por ahora solo estructura básica para evitar errores.
 */
public class Validador {


    public Validador(ErrorManager errorManager) {
    }

    /**
     * Método principal de validación por línea.
     * Por ahora no hace nada, solo evita la excepción.
     * @param tokens
     * @param linea
     * @param numeroLinea
     */
    public void validarLinea(List<Token> tokens, String linea, int numeroLinea) {
        // Aquí luego agregaremos todas las validaciones reales:
        // - Palabras reservadas
        // - Identificadores
        // - Dim
        // - Console.WriteLine
        // - Module / End Module
        // - Comentarios
        // - etc.
    }
}
