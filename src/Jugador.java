import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private Mazo mazo;
    private List<Carta> mano;
    private Pokemon pokemonActivo;
    private List<Pokemon> banca;
    private int rondasGanadas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mazo = new Mazo();
        this.mano = new ArrayList<Carta>();
        this.banca = new ArrayList<Pokemon>();
    }

    public boolean robarCarta() {
        if (!mazo.getCartas().isEmpty()) {
            Carta robada = mazo.getCartas().get(0); 
            mazo.getCartas().remove(0);             
            mano.add(robada);                      
            return true;
        } else {
            System.out.println(nombre + " no tiene mas cartas dentro del mazo");
            return false;
        }
    }

    public void setPokemonActivo(Pokemon pokemon) {
        this.pokemonActivo = pokemon;
    }

    public String getNombre() { return nombre; }
    public Mazo getMazo() { return mazo; }
    public List<Carta> getMano() { return mano; }
    public Pokemon getPokemonActivo() { return pokemonActivo; }
    public List<Pokemon> getBanca() { return banca; }
    public int getRondasGanadas() { return rondasGanadas; }
    public void incrementarRondasGanadas() {
        this.rondasGanadas++;
    }
}