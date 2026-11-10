/**
 * Carta de tipo "Entrenador" (poción). Hereda de Carta e implementa Activable.
 *
 * Al activarla, cura al Pokémon activo del jugador una cantidad fija:
 *   - Pocion: +20 PS
 *   - Julio Profe: +10 PS
 * La curación nunca supera el PS máximo del Pokémon.
 */
public class Entrenador extends Carta implements Activable {
    private String efecto;            // descripción textual del efecto
    private int cantidadCuracion;     // cuánto PS cura (20 o 10)

    public Entrenador(String nombre, String tipo, String efecto, int cantidadCuracion) {
        super(nombre, tipo);
        this.efecto = efecto;
        this.cantidadCuracion = cantidadCuracion;
    }

    public String getEfecto() { return efecto; }
    public int getCantidadCuracion() { return cantidadCuracion; }

    /**
     * Aplica la curación al Pokémon activo del jugador.
     * Si la vida resultante supera el máximo, se limita al máximo.
     */
    @Override
    public void activar(Jugador jugador) {
        if (jugador.getPokemonActivo() != null) {
            Pokemon p = jugador.getPokemonActivo();
            int nuevaVida = p.getPs() + cantidadCuracion;
            if (nuevaVida > p.getPsMaximo()) {
                p.setPs(p.getPsMaximo());   // tope en el PS máximo
            } else {
                p.setPs(nuevaVida);
            }
            System.out.println("¡" + getNombre() + " usado! Pokemon curado, ahora tienes "
                    + p.getPs() + " PS");
        }
    }
}
