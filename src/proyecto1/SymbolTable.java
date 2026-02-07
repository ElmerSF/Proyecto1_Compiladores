/*
UNED Informática Compiladores 3307
Estudiante Elmer Eduardo Salazar Flores 3-0426-0158
I Cuatrimestre 2026
Clase para manejo de variables declaradas y validación 
de operaciones */
package proyecto1;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private final Map<String, String> variables = new HashMap<>();

    // Registrar variable en la tabla
    public void registrar(String nombre, String tipo) {
        variables.put(nombre.toLowerCase(), tipo.toLowerCase());
    }

    // Verificar si existe
    public boolean existe(String nombre) {
        return variables.containsKey(nombre.toLowerCase());
    }

    // Obtener tipo de una variable
    public String tipoDe(String nombre) {
        return variables.get(nombre.toLowerCase());
    }
}
