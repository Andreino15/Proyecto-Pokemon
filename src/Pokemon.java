/**
 * Representa una carta de Pokémon. Hereda de Carta.
 *
 * Atributos adicionales:
 *   - ps: puntos de vida actuales (pueden bajar en combate)
 *   - psMaximo: PS iniciales (final, no cambian) — sirve para curar al máximo
 *   - gritoBatalla: frase que imprime cuando entra al campo
 *   - danoAtaque: daño base que hace en cada ataque
 */
public class Pokemon extends Carta {
    private int ps;
    private final int psMaximo;          // constante: no cambia después del constructor
    private String gritoBatalla;
    private int danoAtaque;

    public Pokemon(String nombre, String tipo, int ps, String gritoBatalla, int danoAtaque) {
        super(nombre, tipo);             // llama al constructor de Carta
        this.ps = ps;
        this.psMaximo = ps;              // al crear, PS = PS máximo
        this.gritoBatalla = gritoBatalla;
        this.danoAtaque = danoAtaque;
    }

    public int getPs() { return ps; }
    public void setPs(int ps) { this.ps = ps; }        // se usa para recibir daño o curar
    public int getPsMaximo() { return psMaximo; }
    public String getGritoBatalla() { return gritoBatalla; }
    public int getDanoAtaque() { return danoAtaque; }

    /**
     * Restaura los PS al valor máximo.
     * Se usa cuando el Pokémon vuelve a la mano tras cambiar de activo.
     */
    public void curarAlMaximo() {
        this.ps = this.psMaximo;
    }
}
