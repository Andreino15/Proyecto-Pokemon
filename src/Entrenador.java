public class Entrenador extends Carta implements Activable {
    private String efecto;

    public Entrenador(String nombre, String tipo, String efecto) {
        super(nombre, tipo);
        this.efecto = efecto;
    }

    public String getEfecto() {
        return efecto;
    }

    @Override
    public void activar(Jugador jugador) {
        if (jugador.getPokemonActivo() != null) {
            int vidaActual = jugador.getPokemonActivo().getPs();
            int nuevaVida = vidaActual + 20;
            if (this.getNombre().equals("Julio Profe")) {
                nuevaVida = nuevaVida - 10;
            }
            
            if (nuevaVida > 80) {
                jugador.getPokemonActivo().setPs(80);
            } else {
                jugador.getPokemonActivo().setPs(nuevaVida);
            }
            
            System.out.println("Pokemon curado, ahora tienes " + jugador.getPokemonActivo().getPs() + " Ps" );
        }
    }
}