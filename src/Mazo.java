import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private List<Carta> cartas;

    public Mazo() {
        this.cartas = new ArrayList<Carta>();
        iniciarMazo();
        barajar();
    }

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

        // Cartas curativas
        for (int i = 0; i < 5; i++) {
            Entrenador pocion = new Entrenador("Pocion", "Curativa", "Cura 20 PS al Pokemon en juego", 20);
            cartas.add(pocion);

            Entrenador profJulio = new Entrenador("Julio Profe", "Curativa", "Cura 10 PS al Pokemon en juego", 10);
            cartas.add(profJulio);
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public List<Carta> getCartas() {
        return cartas;
    }
}
