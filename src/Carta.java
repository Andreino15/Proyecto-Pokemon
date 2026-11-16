/**
 * Clase abstracta base para todas las cartas del juego.
 * Define los atributos comunes: nombre y tipo.
 *
 * ¿Por qué abstracta? Porque nunca existirá una "Carta" genérica,
 * siempre será un Pokemon o un Entrenador (heredan de aquí).
 */
public abstract class Carta {
    protected String nombre;  // Ej: "Pikachu", "Pocion"
    protected String tipo;    // Ej: "Eléctrico", "Curativa"

    public Carta(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    // Getters: permiten leer los atributos desde fuera de la clase
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
}
