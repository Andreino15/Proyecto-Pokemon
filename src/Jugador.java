import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un jugador de la partida.
 * Tiene un mazo propio, una mano de cartas, un Pokémon activo en campo
 * y un contador de rondas ganadas.
 */
public class Jugador {
    private String nombre;
    private Mazo mazo;                    // mazo de cartas del jugador (se crea automáticamente)
    private List<Carta> mano;             // cartas que tiene en la mano (máx 5)
    private Pokemon pokemonActivo;        // Pokémon que está peleando en el campo
    private int rondasGanadas;            // contador para ganar la partida (3 rondas)

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mazo = new Mazo();           // cada jugador crea su propio mazo barajado
        this.mano = new ArrayList<>();    // lista vacía al inicio
        this.rondasGanadas = 0;
    }

    /**
     * Saca la primera carta del mazo y la añade a la mano.
     * Si el mazo está vacío, imprime un aviso (no lanza error).
     */
    public void robarCarta() {
        if (!mazo.getCartas().isEmpty()) {
            Carta robada = mazo.getCartas().remove(0); // saca del inicio de la lista
            mano.add(robada);                            // la pone al final de la mano
        } else {
            System.out.println(nombre + " no tiene mas cartas dentro del mazo");
        }
    }

    public void setPokemonActivo(Pokemon pokemon) {
        this.pokemonActivo = pokemon;
    }

    // ── Getters ──
    public String getNombre() { return nombre; }
    public Mazo getMazo() { return mazo; }
    public List<Carta> getMano() { return mano; }
    public Pokemon getPokemonActivo() { return pokemonActivo; }
    public int getRondasGanadas() { return rondasGanadas; }

    public void incrementarRondasGanadas() {
        this.rondasGanadas++;
    }

    /**
     * Recorre la mano y devuelve true si hay al menos una carta Pokemon.
     * Útil para saber si el jugador puede seguir peleando.
     */
    public boolean tienePokemonEnMano() {
        for (Carta carta : mano) {
            if (carta instanceof Pokemon) {
                return true;
            }
        }
        return false;
    }
}
