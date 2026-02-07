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

    public void registrar(String nombre, String tipo) {
        variables.put(nombre.toLowerCase(), tipo.toLowerCase());
    }

    public boolean existe(String nombre) {
        return variables.containsKey(nombre.toLowerCase());
    }

    public String tipoDe(String nombre) {
        return variables.get(nombre.toLowerCase());
    }

    // NUEVO: para generar archivo de salida
    public Map<String, String> obtenerVariables() {
        return variables;
    }
}
