import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo de cartas de un jugador.
 * Al crearse, genera automáticamente 19 cartas (9 Pokémon + 10 curativas)
 * y las baraja aleatoriamente con Collections.shuffle().
 */
public class Mazo {
    private List<Carta> cartas;

    public Mazo() {
        this.cartas = new ArrayList<Carta>();
        iniciarMazo();   // carga las cartas
        barajar();       // las mezcla aleatoriamente
    }

    /**
     * Crea todas las cartas del mazo:
     *   - 9 Pokémon distintos (cada uno con PS=80, daño y tipo propios)
     *   - 5 Pociones (+20 PS) y 5 "Julio Profe" (+10 PS)
     */
    private void iniciarMazo() {

        Pokemon pikachu = new Pokemon("Pikachu", "Eléctrico", 80, "Pika-Pika Chuu", 25);
        cartas.add(pikachu);

        Pokemon charmander = new Pokemon("Charmander", "Fuego", 80, "Charmandeeer!!", 25);
        cartas.add(charmander);

        Pokemon squirtle = new Pokemon("Squirtle", "Agua", 80, "Squirttt-le", 20);
        cartas.add(squirtle);

        Pokemon bulbasaur = new Pokemon("Bulbasaur", "Planta", 80, "Buulbaaasaaaurrr!!!", 23);
        cartas.add(bulbasaur);

        Pokemon jigglypuff = new Pokemon("Jigglypuff", "Normal", 80, "Jiggggglyyyypuff!!!!", 20);
        cartas.add(jigglypuff);

        Pokemon meowth = new Pokemon("Meowth", "Normal", 80, "Meeeeooowwwthh", 20);
        cartas.add(meowth);

        Pokemon machop = new Pokemon("Machop", "Lucha", 80, "Maaaachop", 15);
        cartas.add(machop);

        Pokemon geodude = new Pokemon("Geodude", "Lucha", 80, "Geoodudee", 22);
        cartas.add(geodude);

        Pokemon gastly = new Pokemon("Gastly", "Psíquico", 80, "Gaastlyyyy", 15);
        cartas.add(gastly);

        // Cartas curativas: 5 Pociones (+20) y 5 Julio Profe (+10)
        for (int i = 0; i < 5; i++) {
            Entrenador pocion = new Entrenador("Pocion", "Curativa", "Cura 20 PS al Pokemon en juego", 20);
            cartas.add(pocion);

            Entrenador profJulio = new Entrenador("Julio Profe", "Curativa", "Cura 10 PS al Pokemon en juego", 10);
            cartas.add(profJulio);
        }
    }

    /**
     * Mezcla aleatoriamente el orden de las cartas.
     * Así cada partida reparte las cartas en orden diferente.
     */
    public void barajar() {
        Collections.shuffle(cartas);
    }

    /** Devuelve la lista interna de cartas (para robar del principio) */
    public List<Carta> getCartas() {
        return cartas;
    }
}
