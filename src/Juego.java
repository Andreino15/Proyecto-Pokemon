import java.util.Scanner;

public class Juego {

private Jugador jugador1;
private Jugador jugador2;
private Scanner scanner;
public Juego() {scanner = new Scanner(System.in);
System.out.print("Ingresa el nombre J1 ");
        this.jugador1 = new Jugador(scanner.nextLine());
        System.out.print("Ingresa el nombre j2 ");
        this.jugador2 = new Jugador(scanner.nextLine());
        System.out.println("¡¡A PELEAR!!");
}
public void prepararPartida() {

        System.out.println("Repartiendo cartas.. \n ");
        for (int i = 0; i < 5; i++) {
            jugador1.robarCarta();
            jugador2.robarCarta();
        }
         elegirPokemon(jugador1);
            elegirPokemon(jugador2);
}
private void elegirPokemon(Jugador jugador) {
//## mana1
        System.out.println("Mano de " + jugador.getNombre() + ":");
        for (int i = 0; i < jugador.getMano().size(); i++) {
            Carta carta = jugador.getMano().get(i);
            System.out.println(i + ". " + carta.getNombre() + " (" + carta.getTipo() + ")");
        }
            while (jugador.getPokemonActivo() == null) {
            System.out.print("Escoge tu Pokemon ");
            int eleccion = scanner.nextInt(); 
            
            if (eleccion >= 0 && eleccion < jugador.getMano().size()) {
                Carta cartaElegida = jugador.getMano().get(eleccion);

                if (cartaElegida instanceof Pokemon) {
                    jugador.setPokemonActivo((Pokemon) cartaElegida);
                    jugador.getMano().remove(eleccion); 
                    System.out.println("¡" + jugador.getNombre() + " ha lanzado a " + jugador.getPokemonActivo().getGritoBatalla() + "!\n");
                } else {
                    System.out.println("Debes escoger a un pokemon");
                }
            } else {
                System.out.println("¡Numero invalido!");
            }
        }
    }
private boolean jugarTurno(Jugador activo, Jugador rival) {
        System.out.println("\n#################");
        System.out.println("Turno de " + activo.getNombre());
        
        //rellena sacandoi las cartas que tiene, y mira si tiene en el mazo para rellenar (tope 5 (0-4))
        while (activo.getMano().size() < 5 && !activo.getMazo().getCartas().isEmpty()) {
            activo.robarCarta();
        }
        
        if (activo.getMazo().getCartas().isEmpty()) {
            System.out.println("¡" + activo.getNombre() + "No tiene mas cartas!.. GANA " + rival.getNombre());
            return false;
        }

        System.out.println("Tu Pokemon activo: " + activo.getPokemonActivo().getNombre() + " (" + activo.getPokemonActivo().getPs() + " PS)");
        System.out.println("¿Que deseas hacer?");
        System.out.println("1. Atacar");
        System.out.println("2. Cambiar Pokemon");
        System.out.println("3. Cartas Curativas (Entrenador-Pocion");
        System.out.print("Digita tu opcion (1, 2 o 3): ");
        int opcion1 = scanner.nextInt();

        if (opcion1 == 1) {
            System.out.println(activo.getPokemonActivo().getNombre() + " ataca!");
            int psActuales2 = rival.getPokemonActivo().getPs();
            rival.getPokemonActivo().setPs(psActuales2 - activo.getPokemonActivo().getDanoAtaque()); 
            System.out.println("El rival recibe " + activo.getPokemonActivo().getDanoAtaque() + " de daño y le quedan " + rival.getPokemonActivo().getPs() + " PS.");

        } else if (opcion1 == 2) {
            System.out.println("Tus cartas en la mano:");
            for (int i = 0; i < activo.getMano().size(); i++) {
                System.out.println(i + ". " + activo.getMano().get(i).getNombre());
            }
            System.out.print("Elige el numero de tu nuevo Pokemon: ");
            int numeroCambio = scanner.nextInt();
            
            //////# cambio de poke, revisa si es mayor a 0 y si es menor a la cantidad de cartas en la mano
            /// revisa si es de tipo pokemon, guarda el pokemon que tenia activo, para despues con el set anade el pokemon escogido
            /// a la batalla, remove quita el pokemon de la mano, el que escogio.
            if (numeroCambio >= 0 && numeroCambio < activo.getMano().size()) {
                if (activo.getMano().get(numeroCambio) instanceof Pokemon) {
                    activo.getMano().add(activo.getPokemonActivo()); 
                    
                    activo.setPokemonActivo((Pokemon) activo.getMano().get(numeroCambio)); 
                    activo.getMano().remove(numeroCambio); 
                    System.out.println("Devolviste a tu pokemon. Ahora peleas con " + activo.getPokemonActivo().getGritoBatalla() );
                } else {
                    System.out.println("Esa carta no es un Pokemon. Pierdes el turno");
                }
            } else {
                System.out.println("Numero fuera de rango. Pierdes el turno.");
            }

        } else if (opcion1 == 3) {
            System.out.println("Tus cartas en la mano:");
            for (int i = 0; i < activo.getMano().size(); i++) {
                System.out.println(i + ". " + activo.getMano().get(i).getNombre());
            }
            System.out.print("Elige la carta curativa: ");
            int indiceCarta = scanner.nextInt();
            
            
            if (indiceCarta >= 0 && indiceCarta < activo.getMano().size()) {
                Carta cartaElegida = activo.getMano().get(indiceCarta);
                
                if (cartaElegida instanceof Activable) {
                    Activable cartaActivable = (Activable) cartaElegida;
                    cartaActivable.activar(activo); 
                    
                    activo.getMano().remove(indiceCarta); 
                    
                    if (!activo.getMazo().getCartas().isEmpty()) {
                        activo.robarCarta();
                        System.out.println("Has robado una carta del mazo");
                    }
                } else {
                    System.out.println("No es una carta valida. Pierdes tu turno ");
                }
            } else {
                System.out.println("Numero fuera de rango. Pierdes tu turno ");
            }
        }else {
            System.out.println("Numero fuera de rango. Pierdes tu turno");
        }

        if (rival.getPokemonActivo().getPs() <= 0) {
            System.out.println("\n¡" + rival.getPokemonActivo().getNombre() + " se ha debilitado!");
            System.out.println("¡" + activo.getNombre() + " GANA LA PARTIDA!");
            return false;
        }

        return true;
    }

    public void iniciarCombate() {
        System.out.println("-- ¡INICIA EL COMBATE! --");
        boolean combateActivo = true;

        while (combateActivo && jugador1.getPokemonActivo() != null && jugador2.getPokemonActivo() != null) {
            
            
            combateActivo = jugarTurno(jugador1, jugador2);
            if (!combateActivo) return; 
            
            int pausa1 = 0;
            while (pausa1 != 1) {
                System.out.print("\nEscribe 1 y presiona Enter para pasar al turno del rival: ");
                pausa1 = scanner.nextInt();
            }

        
            combateActivo = jugarTurno(jugador2, jugador1);
            if (!combateActivo) return;
            
            int pausa2 = 0;
            while (pausa2 != 1) {
                System.out.print("\nEscribe 1 y presiona Enter para iniciar la siguiente ronda: ");
                pausa2 = scanner.nextInt();
            }
        }
    }

    public static void main(String[] args) {
        Juego partida = new Juego();
        partida.prepararPartida();
        partida.iniciarCombate();
    }
}