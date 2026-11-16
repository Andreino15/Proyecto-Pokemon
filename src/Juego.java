import java.util.Scanner;
import java.util.Collections;

public class Juego {

    private Jugador jugador1;
    private Jugador jugador2;
    private Scanner scanner;

    public Juego() {
        scanner = new Scanner(System.in);
        System.out.print("Ingresa el nombre J1 ");
        this.jugador1 = new Jugador(scanner.nextLine());
        System.out.print("Ingresa el nombre j2 ");
        this.jugador2 = new Jugador(scanner.nextLine());
        System.out.println("¡¡A PELEAR!!");
    }

    private  void prepararPartida() {
        repartirManoInicial(jugador1);
        repartirManoInicial(jugador2);

        elegirPokemon(jugador1);
        elegirPokemon(jugador2);
    }

    private void repartirManoInicial(Jugador jugador) {
        int cantidadPokemon = 0;

        while (cantidadPokemon < 2) {
            cantidadPokemon = 0;
            for (int i = 0; i < 5; i++) {
                jugador.robarCarta();
            }
            for (int i = 0; i < jugador.getMano().size(); i++) {
                if (jugador.getMano().get(i) instanceof Pokemon) {
                    cantidadPokemon++;
                    if (cantidadPokemon >= 2) {
                        break;
                    }
                }
            }
            if (cantidadPokemon < 2) {
                jugador.getMazo().getCartas().addAll(jugador.getMano());
                jugador.getMano().clear();
                Collections.shuffle(jugador.getMazo().getCartas());
            }
        }
    }

    private void elegirPokemon(Jugador jugador) {
        System.out.println("Mano de " + jugador.getNombre() + ":");
        for (int i = 0; i < jugador.getMano().size(); i++) {
            Carta carta = jugador.getMano().get(i);
            System.out.println(i + " | " + carta.getNombre() + "| Tipo " + carta.getTipo());
        }
        while (jugador.getPokemonActivo() == null) {
            System.out.print("Escoge tu Pokemon ");
            if (scanner.hasNextInt()) {
                int eleccion = scanner.nextInt();
                if (eleccion >= 0 && eleccion < jugador.getMano().size()) {
                    Carta cartaElegida = jugador.getMano().get(eleccion);

                    if (cartaElegida instanceof Pokemon) {
                        jugador.setPokemonActivo((Pokemon) cartaElegida);
                        jugador.getMano().remove(eleccion);
                        System.out.println(jugador.getNombre() + " ha lanzado a " + jugador.getPokemonActivo().getNombre() + " |" + jugador.getPokemonActivo().tipo  + "\n");
                    } else {
                        System.out.println("Debes escoger a un pokemon");
                    }
                } else {
                    System.out.println("Numero invalido");
                }
            } else {
                System.out.println("Invalido");
                scanner.next();
            }
        }
    }

    private int calcularDano(Pokemon atacante, Pokemon defensor, int danoBase) {
        String tipoAtacante = atacante.getTipo();
        String tipoDefensor = defensor.getTipo();
        boolean debilidad = false;

        if (tipoDefensor.equals("Agua") && tipoAtacante.equals("Electrico")) {
            debilidad = true;
        } else if (tipoDefensor.equals("Fuego") && tipoAtacante.equals("Agua")) {
            debilidad = true;
        }

        if (debilidad==true) {
            System.out.println("Debilidad provocada");
            return danoBase + 20;
        }
        return danoBase;
    }

    private boolean jugarTurno(Jugador activo, Jugador rival, boolean esPrimerTurno) {
        System.out.println("\n#################");
        System.out.println("Turno de " + activo.getNombre());

        activo.getPokemonActivo().reiniciarReduccionRetirada();

        activo.robarCarta();

        boolean turnoTerminado = false;
        boolean energiaUsada = false;
        boolean soporteUsado = false;

        while (!turnoTerminado) {
            Pokemon pokeActivo = activo.getPokemonActivo();
            System.out.println("\nPokemon activo | " + pokeActivo.getNombre() + " | tipo " + pokeActivo.getTipo()  + " | Ps " +pokeActivo.getPs() + "/" + pokeActivo.getPsMaximos() + " | Energias " + pokeActivo.getEnergias() + " | Costo retirada " + pokeActivo.getCostoRetiradaActual());
            System.out.println("Banca | " + activo.getBanca().size() );
            for (int i = 0; i < activo.getBanca().size(); i++) {
                Pokemon b = activo.getBanca().get(i);
                System.out.println(b.getNombre() + " |" + b.getPs() + " PS | Energias: " + b.getEnergias());
            }

            System.out.println("\n¿Que deseas hacer?");
            System.out.println("1. Pokemon a la banca");
            System.out.println("2. añadir energia (1 por turno)");
            System.out.println("3. Jugar carta de Entrenador/soporte");
            System.out.println("4. Retirar Pokemon activo");
            System.out.println("5. Atacar");
            System.out.println("6. Pasar turno");
            System.out.print("Digita tu opcion: ");

            if (scanner.hasNextInt()) {
                int opcion1 = scanner.nextInt();

                if (opcion1 == 1) {
                    if (activo.getBanca().size() >= 2) {
                        System.out.println("Tu banca ya esta llena");
                    } else {
                        System.out.println("Tus cartas en la mano:");
                        for (int i = 0; i < activo.getMano().size(); i++) {
                            System.out.println(i + " | " + activo.getMano().get(i).getNombre() + " |" + activo.getMano().get(i).getTipo() + ")");
                        }
                        System.out.print("Elige el numero del Pokemon para la banca ");
                        if (scanner.hasNextInt()) {
                            int indiceBanca = scanner.nextInt();
                            if (indiceBanca >= 0 && indiceBanca < activo.getMano().size()) {
                                if (activo.getMano().get(indiceBanca) instanceof Pokemon) {
                                    Pokemon pokeBanca = (Pokemon) activo.getMano().remove(indiceBanca);
                                    activo.getBanca().add(pokeBanca);
                                    System.out.println("Bajaste a " + pokeBanca.getNombre() + " a la banca");
                                } else {
                                    System.out.println("Esa carta no es un Pokemon");
                                }
                            } else {
                                System.out.println("Numero fuera de rango");
                            }
                        } else {
                            System.out.println("Entrada invalida");
                            scanner.next();
                        }
                    }

                } else if (opcion1 == 2) {
                    if (energiaUsada==true) {
                        System.out.println("Ya añadiste tu energia de este turno");
                    } else {
                        System.out.println("¿A que Pokemon quieres añadir la energia?");
                        System.out.println(" presiona el 0 para seleccionar | " + pokeActivo.getNombre());
                        for (int i = 0; i < activo.getBanca().size(); i++) {
                            System.out.println("presiona el " + i + 1 + "para seleccionar |" +  " Banca: " + activo.getBanca().get(i).getNombre());
                        }
                        System.out.print("Elige el numero: ");
                        if (scanner.hasNextInt()) {
                            int energiaSeleccion = scanner.nextInt();
                            if (energiaSeleccion == 0) {
                                pokeActivo.agregarEnergia(1);
                                energiaUsada = true;
                                System.out.println("Energia añadida a " + pokeActivo.getNombre());
                            } else if (energiaSeleccion >= 1 && energiaSeleccion <= activo.getBanca().size()) {
                                activo.getBanca().get(energiaSeleccion - 1).agregarEnergia(1);
                                energiaUsada = true;
                                System.out.println("Energia añadida a " + activo.getBanca().get(energiaSeleccion - 1).getNombre());
                            } else {
                                System.out.println("Numero fuera de rango.");
                            }
                        } else {
                            System.out.println("Entrada invalida.");
                            scanner.next();
                        }
                    }

                } else if (opcion1 == 3) {
                    System.out.println("Tus cartas en la mano:");
                    for (int i = 0; i < activo.getMano().size(); i++) {
                        System.out.println(i + "|" + activo.getMano().get(i).getNombre() + " |" + activo.getMano().get(i).getTipo());
                    }
                    System.out.print("Elige la carta de Entrenador/soporte ");
                    if (scanner.hasNextInt()) {
                        int indiceCarta = scanner.nextInt();
                        if (indiceCarta >= 0 && indiceCarta < activo.getMano().size()) {
                            Carta cartaElegida = activo.getMano().get(indiceCarta);
                            if (cartaElegida instanceof Entrenador) {
                                Entrenador cartaEntrenador = (Entrenador) cartaElegida;
                                if (cartaEntrenador.getTipo().equals("Soporte") && soporteUsado==true) {
                                    System.out.println("Ya jugaste una carta de Soporte en este turno");
                                } else {
                                    if (cartaEntrenador.getTipo().equals("Soporte")) {
                                        soporteUsado = true;
                                    }
                                    activo.getMano().remove(indiceCarta);
                                    cartaEntrenador.activar(activo);
                                }
                            } else {
                                System.out.println("No es una carta de tipo Entrenador");
                            }
                        } else {
                            System.out.println("Numero fuera de rango");
                        }
                    } else {
                        System.out.println("Entrada invalida");
                        scanner.next();
                    }

                } else if (opcion1 == 4) {
                    if (activo.getBanca().isEmpty()) {
                        System.out.println("No tienes Pokemon en la banca para cambiar");
                    } else {
                        int costoRetirada = pokeActivo.getCostoRetiradaActual();
                        if (pokeActivo.getEnergias() < costoRetirada) {
                            System.out.println("No tienes suficiente energia para retirar, necesitas " + costoRetirada);
                        } else {
                            System.out.println("Pokemon en banca");
                            for (int i = 0; i < activo.getBanca().size(); i++) {
                                System.out.println(i + "| " + activo.getBanca().get(i).getNombre() + " | " + activo.getBanca().get(i).getPs() + " ");
                            }
                            System.out.print("Elige el Pokemon que pasara a jugar ");
                            if (scanner.hasNextInt()) {
                                int indiceCambio = scanner.nextInt();
                                if (indiceCambio >= 0 && indiceCambio < activo.getBanca().size()) {
                                    pokeActivo.quitarEnergia(costoRetirada);
                                    Pokemon nuevoActivo = activo.getBanca().get(indiceCambio); 
                                    activo.getBanca().remove(indiceCambio);                    
                                    activo.getBanca().add(pokeActivo);                          
                                    activo.setPokemonActivo(nuevoActivo);
                                    System.out.println("Retiraste a " + pokeActivo.getNombre() + " Ahora peleas con " + nuevoActivo.getNombre());
                                } else {
                                    System.out.println("Numero fuera de rango");
                                }
                            } else {
                                System.out.println("Entrada invalida");
                                scanner.next();
                            }
                        }
                    }

                } else if (opcion1 == 5) {
                    if (esPrimerTurno==true) {
                        System.out.println("No puedes atacar en el primer turno");
                    } else {
                        System.out.println("Ataques disponibles");
                        System.out.println("1. " + pokeActivo.getNombreAtaque1() + " | Costo " + pokeActivo.getCostoEnergia1() + " energias | Daño: " + pokeActivo.getDanoAtaque1());
                        if (pokeActivo.getNombreAtaque2() != null) {
                            System.out.println("2. " + pokeActivo.getNombreAtaque2() + " | Costo " + pokeActivo.getCostoEnergia2() + " energias | Daño: " + pokeActivo.getDanoAtaque2());
                        }
                        System.out.print("Elige el ataque ");

                        if (scanner.hasNextInt()) {
                            int ataqueElegido = scanner.nextInt();
                            int costoNecesario = 0;
                            int danoBase = 0;
                            String nombreAtaque = "";
                            boolean ataqueValido = false;

                            if (ataqueElegido == 1) {
                                costoNecesario = pokeActivo.getCostoEnergia1();
                                danoBase = pokeActivo.getDanoAtaque1();
                                nombreAtaque = pokeActivo.getNombreAtaque1();
                                ataqueValido = true;
                            } else if (ataqueElegido == 2 && pokeActivo.getNombreAtaque2() != null) {
                                costoNecesario = pokeActivo.getCostoEnergia2();
                                danoBase = pokeActivo.getDanoAtaque2();
                                nombreAtaque = pokeActivo.getNombreAtaque2();
                                ataqueValido = true;
                            }

                            if (!ataqueValido) {
                                System.out.println("Ataque invalido");
                            } else if (pokeActivo.getEnergias() < costoNecesario) {
                                System.out.println("No tienes suficientes energias para usar " + nombreAtaque);
                            } else {
                                System.out.println(pokeActivo.getNombre() + " uso " + nombreAtaque );
                                int danoTotal = calcularDano(pokeActivo, rival.getPokemonActivo(), danoBase);
                                int psActuales2 = rival.getPokemonActivo().getPs();
                                rival.getPokemonActivo().setPs(psActuales2 - danoTotal);
                                System.out.println("El rival recibe " + danoTotal + " de daño y le quedan " + rival.getPokemonActivo().getPs() + " ps");

                                if (rival.getPokemonActivo().getPs() <= 0) {
                                    System.out.println("\n¡" + rival.getPokemonActivo().getNombre() + " se ha debilitado");

                                    int premios = 1;
                                    if (rival.getPokemonActivo().getNombre().endsWith("ex")) {
                                        premios = 2;
                                        System.out.println("Derrotaste a un Pokemon-ex, Obtienes 2 puntos");
                                    } else {
                                        System.out.println("Obtienes 1 punto");
                                    }

                                    for (int i = 0; i < premios; i++) {
                                        activo.incrementarRondasGanadas();
                                    }

                                    System.out.println("Puntos de premio de " + activo.getNombre() + " | " + activo.getRondasGanadas() + "/3");

                                    if (activo.getRondasGanadas() >= 3) {
                                        System.out.println( activo.getNombre() + " HA GANADO LA PARTIDA!!");
                                        return false;
                                    }

                                    if (rival.getBanca().isEmpty()) {
                                        System.out.println( rival.getNombre() + " no tiene Pokemon en la banca " + activo.getNombre() + " HA GANADO LA PARTIDA!");
                                        return false;
                                    }

                                    rival.setPokemonActivo(null);
                                    while (rival.getPokemonActivo() == null) {
                                        System.out.println("\n" + rival.getNombre() + ", elige un Pokemon de tu banca para seguir jugando");
                                        for (int i = 0; i < rival.getBanca().size(); i++) {
                                            System.out.println(i + " | " + rival.getBanca().get(i).getNombre() + " | " + rival.getBanca().get(i).getPs() + " ps");
                                        }
                                        System.out.print("Elige el numero ");
                                        if (scanner.hasNextInt()) {
                                            int reemplazo = scanner.nextInt();
                                            if (reemplazo >= 0 && reemplazo < rival.getBanca().size()) {
                                                rival.setPokemonActivo(rival.getBanca().get(reemplazo));
                                                rival.getBanca().remove(reemplazo);
                                                System.out.println( rival.getNombre() + " juega con " + rival.getPokemonActivo().getNombre() );
                                            } else {
                                                System.out.println("Numero invalido");
                                            }
                                        } else {
                                            System.out.println("Entrada invalida");
                                            scanner.next();
                                        }
                                    }
                                }
                                turnoTerminado = true;
                            }
                        } else {
                            System.out.println("Entrada invalida");
                            scanner.next();
                        }
                    }

                } else if (opcion1 == 6) {
                    System.out.println(activo.getNombre() + " termina su turno");
                    turnoTerminado = true;

                } else {
                    System.out.println("Numero fuera de rango");
                }
            } else {
                System.out.println("Entrada invalida");
                scanner.next();
            }
        }

        return true;
    }

    private void iniciarCombate() {
        System.out.println("-- ¡¡INICIA EL COMBATE!! --");
        boolean combateActivo = true;
        boolean esPrimerTurno2 = true;

        while (combateActivo==true && jugador1.getPokemonActivo() != null && jugador2.getPokemonActivo() != null) {

            combateActivo = jugarTurno(jugador1, jugador2, esPrimerTurno2);
            esPrimerTurno2 = false;

            if (!combateActivo) return;

            int pausa1 = 0;
            while (pausa1 != 1) {
                System.out.print("\nEscribe 1 y presiona Enter para pasar el turno ");
                if (scanner.hasNextInt()) {
                    pausa1 = scanner.nextInt();
                } else {
                    System.out.println("Entrada invalida. ingresa el numero 1");
                    scanner.next();
                }
            }

            combateActivo = jugarTurno(jugador2, jugador1, false);
            if (!combateActivo) return;

            int pausa2 = 0;
            while (pausa2 != 1) {
                System.out.print("\nEscribe 1 y presiona Enter para pasar el turno ");
                if (scanner.hasNextInt()) {
                    pausa2 = scanner.nextInt();
                } else {
                    System.out.println("Entrada invalida. ingresa el numero 1");
                    scanner.next();
                }
            }
        }
    }

    public static void main(String[] args) {
        Juego partida = new Juego();
        partida.prepararPartida();
        partida.iniciarCombate();
    }
}