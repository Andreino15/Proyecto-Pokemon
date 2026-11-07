import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private String nombre;
    private Mazo mazo;
    private List<Carta> mano;
    private Pokemon pokemonActivo;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mazo = new Mazo(); 
        this.mano = new ArrayList<>(); 
    }


    public void robarCarta() {
        if (!mazo.getCartas().isEmpty()) {
            Carta robada = mazo.getCartas().remove(0);
            mano.add(robada);
        } else {
            System.out.println(nombre + " no tiene mas cartas dentro del mazo ");
        }
    }

    public void setPokemonActivo(Pokemon pokemon) {
        this.pokemonActivo = pokemon;
    }

    public String getNombre() { return nombre; }
    public Mazo getMazo() { return mazo; }
    public List<Carta> getMano() { return mano; }
    public Pokemon getPokemonActivo() { return pokemonActivo; }
}