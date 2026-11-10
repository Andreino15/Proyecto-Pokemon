import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private Mazo mazo;
    private List<Carta> mano;
    private Pokemon pokemonActivo;
    private int rondasGanadas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mazo = new Mazo();
        this.mano = new ArrayList<>();
        this.rondasGanadas = 0;
    }

    public void robarCarta() {
        if (!mazo.getCartas().isEmpty()) {
            Carta robada = mazo.getCartas().remove(0);
            mano.add(robada);
        } else {
            System.out.println(nombre + " no tiene mas cartas dentro del mazo");
        }
    }

    public void setPokemonActivo(Pokemon pokemon) {
        this.pokemonActivo = pokemon;
    }

    public String getNombre() { return nombre; }
    public Mazo getMazo() { return mazo; }
    public List<Carta> getMano() { return mano; }
    public Pokemon getPokemonActivo() { return pokemonActivo; }
    public int getRondasGanadas() { return rondasGanadas; }

    public void incrementarRondasGanadas() {
        this.rondasGanadas++;
    }

    /** Verifica si el jugador tiene al menos un Pokemon en su mano */
    public boolean tienePokemonEnMano() {
        for (Carta carta : mano) {
            if (carta instanceof Pokemon) {
                return true;
            }
        }
        return false;
    }
}
