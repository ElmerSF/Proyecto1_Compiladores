/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de variables declaradas y validación 
de operaciones 
Esta clase implementa la "Tabla de Símbolos"
Su función es almacenar 
las variables declaradas junto con su tipo, para que luego 
el Validador pueda verificar:

- Si una variable existe antes de usarse
- Si el tipo de la variable es compatible con operaciones
- Si una variable ya fue declarada (si quisieras validar eso)
- Qué tipo tiene cada identificador
*/
package proyecto1;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

//<editor-fold defaultstate="collapsed" desc="Explicación del uso de Map">
 /* Estructura de almacenamiento: Map<String, String>
 *
 * Esta estructura almacena las variables declaradas en el programa junto con su tipo.
 * Se utiliza un Map porque permite asociar una clave (nombre de la variable) con un valor
 * (tipo declarado), lo cual es exactamente lo que necesita una tabla de símbolos.
 *
 * Ejemplo: 
 * {"numero1" : "integer", 
 * "texto" : "string", 
 * "esmayor" : "boolean" 
 *
 * ¿Por qué Map?
 * -------------
 * Según la documentación oficial de Oracle:
 * “The Map interface maps keys to values. A map cannot contain duplicate keys; each key can map to at most one value.”
 * (Oracle Java Documentation)
 *
 * Esto significa que un Map garantiza:
 * - No se pueden repetir nombres de variables (clave única).
 * - Cada variable tiene exactamente un tipo asociado.
 * - Las búsquedas, inserciones y validaciones son rápidas (O(1) en HashMap).
 *
 * En este proyecto, el Map funciona como la Tabla de Símbolos:
 * - registrar(nombre, tipo) → guarda la variable declarada.
 * - existe(nombre) → permite validar si una variable ya fue declarada.
 * - tipoDe(nombre) → permite validar compatibilidad de tipos en asignaciones y operaciones.
 *
 * Se usa HashMap específicamente porque:
 * - Es la implementación más eficiente para búsquedas rápidas.
 * - No requiere ordenamiento.
 * - Es ideal para análisis léxico/sintáctico donde se hacen muchas consultas rápidas.
 *
 * Referencia:
 * Oracle. (n.d.). *The Map Interface*. Oracle Java Documentation.
 * https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html
 */
// </editor-fold>

    
    private final Map<String, String> variables = new HashMap<>();

    public void registrar(String nombre, String tipo) {
        variables.put(nombre.toLowerCase(), tipo.toLowerCase());
    }

    public boolean existe(String nombre) {
        return variables.containsKey(nombre.toLowerCase());
    }

    public String tipoDe(String nombre) {
        return variables.get(nombre.toLowerCase());
    }

    // Para generar archivo de salida
    public Map<String, String> obtenerVariables() {
        return variables;
    }
}
