public class Pokemon extends Carta {
    private int ps;
    private int psMaximos;
    private int costoRetirada;
    private int reduccionRetirada;
    private int energias;

    private String nombreAtaque1;
    private int costoEnergia1;
    private int danoAtaque1;

    private String nombreAtaque2;
    private int costoEnergia2;
    private int danoAtaque2;

    public Pokemon(String nombre, String tipo, int ps, int costoRetirada,
                   String nombreAtaque1, int costoEnergia1, int danoAtaque1) {
        super(nombre, tipo);
        this.ps = ps;
        this.psMaximos = ps;
        this.costoRetirada = costoRetirada;
        this.reduccionRetirada = 0;
        this.energias = 0;
        this.nombreAtaque1 = nombreAtaque1;
        this.costoEnergia1 = costoEnergia1;
        this.danoAtaque1 = danoAtaque1;
    }

    public Pokemon(String nombre, String tipo, int ps, int costoRetirada, String nombreAtaque1, int costoEnergia1, int danoAtaque1,
                   String nombreAtaque2, int costoEnergia2, int danoAtaque2) {
        super(nombre, tipo);
        this.ps = ps;
        this.psMaximos = ps;
        this.costoRetirada = costoRetirada;
        this.reduccionRetirada = 0;
        this.energias = 0;
        this.nombreAtaque1 = nombreAtaque1;
        this.costoEnergia1 = costoEnergia1;
        this.danoAtaque1 = danoAtaque1;
        this.nombreAtaque2 = nombreAtaque2;
        this.costoEnergia2 = costoEnergia2;
        this.danoAtaque2 = danoAtaque2;
    }

    public int getPs() { return ps; }
    public void setPs(int ps) { this.ps = ps; }
    public int getPsMaximos() { return psMaximos; }
    public int getCostoRetirada() { return costoRetirada; }

    public void reducirRetirada() {
        this.reduccionRetirada += 1;
    }

    public void reiniciarReduccionRetirada() {
        this.reduccionRetirada = 0;
    }

    public int getCostoRetiradaActual() {
        int costo = this.costoRetirada - this.reduccionRetirada;
        if (costo < 0) {
            return 0;
        }
        return costo;
    }

    public int getEnergias() { return energias; }
    public void agregarEnergia(int cantidad) { this.energias += cantidad; }
    public void quitarEnergia(int cantidad) {
        this.energias -= cantidad;
        if (this.energias < 0) this.energias = 0;
    }
    public String getNombreAtaque1() { return nombreAtaque1; }
    public int getCostoEnergia1() { return costoEnergia1; }
    public int getDanoAtaque1() { return danoAtaque1; }
    public String getNombreAtaque2() { return nombreAtaque2; }
    public int getCostoEnergia2() { return costoEnergia2; }
    public int getDanoAtaque2() { return danoAtaque2; }
}