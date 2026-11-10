public class Entrenador extends Carta implements Activable {
    private String efecto;
    private int cantidadCuracion;

    public Entrenador(String nombre, String tipo, String efecto, int cantidadCuracion) {
        super(nombre, tipo);
        this.efecto = efecto;
        this.cantidadCuracion = cantidadCuracion;
    }

    public String getEfecto() {
        return efecto;
    }

    public int getCantidadCuracion() {
        return cantidadCuracion;
    }

    @Override
    public void activar(Jugador jugador) {
        if (jugador.getPokemonActivo() != null) {
            Pokemon p = jugador.getPokemonActivo();
            int nuevaVida = p.getPs() + cantidadCuracion;
            if (nuevaVida > p.getPsMaximo()) {
                p.setPs(p.getPsMaximo());
            } else {
                p.setPs(nuevaVida);
            }
            System.out.println("¡" + getNombre() + " usado! Pokemon curado, ahora tienes " + p.getPs() + " PS");
        }
    }
}
