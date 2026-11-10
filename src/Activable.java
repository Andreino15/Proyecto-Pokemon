/**
 * Interfaz que define el contrato "activable".
 *
 * Cualquier carta que se pueda "usar" durante el turno debe implementar
 * esta interfaz. En este juego, solo Entrenador la implementa (las pociones).
 *
 * Polimorfismo: en Juego.java se llama carta.activar(jugador) sin importar
 * si es una Pocion o un Julio Profe — cada uno sabe cuánto cura.
 */
public interface Activable {
    void activar(Jugador jugador);
}
