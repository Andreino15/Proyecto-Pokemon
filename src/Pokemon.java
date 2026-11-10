public class Pokemon extends Carta {
    private int ps;
    private final int psMaximo;
    private String gritoBatalla;
    private int danoAtaque;

    public Pokemon(String nombre, String tipo, int ps, String gritoBatalla, int danoAtaque) {
        super(nombre, tipo);
        this.ps = ps;
        this.psMaximo = ps;
        this.gritoBatalla = gritoBatalla;
        this.danoAtaque = danoAtaque;
    }

    public int getPs() { return ps; }
    public void setPs(int ps) { this.ps = ps; }
    public int getPsMaximo() { return psMaximo; }
    public String getGritoBatalla() { return gritoBatalla; }
    public int getDanoAtaque() { return danoAtaque; }

    /** Restaura el PS al máximo (usado al devolver el Pokémon a la mano) */
    public void curarAlMaximo() {
        this.ps = this.psMaximo;
    }
}
