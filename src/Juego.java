import java.util.Scanner;

public class Juego {

    private Jugador jugador1;
    private Jugador jugador2;
    private Scanner scanner;

    public Juego() {
        scanner = new Scanner(System.in);
        System.out.print("Ingresa el nombre J1: ");
        this.jugador1 = new Jugador(scanner.nextLine().trim());
        System.out.print("Ingresa el nombre J2: ");
        this.jugador2 = new Jugador(scanner.nextLine().trim());
        System.out.println("¡¡A PELEAR!!");
    }

    // ─── Lectura segura de enteros (evita InputMismatchException y buffer sucio) ───
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpia el salto de línea pendiente
                return valor;
            } else {
                System.out.println("¡Entrada no válida! Escribe un número.");
                scanner.nextLine(); // descarta la entrada inválida
            }
        }
    }

    // ─── Preparar partida ───
    public void prepararPartida() {
        System.out.println("\nRepartiendo cartas...\n");
        for (int i = 0; i < 5; i++) {
            jugador1.robarCarta();
            jugador2.robarCarta();
        }
        // Asegurar que cada jugador tenga al menos un Pokémon en la mano inicial
        asegurarPokemonInicial(jugador1);
        asegurarPokemonInicial(jugador2);

        elegirPokemon(jugador1);
        elegirPokemon(jugador2);
    }

    private void asegurarPokemonInicial(Jugador jugador) {
        while (!jugador.tienePokemonEnMano() && !jugador.getMazo().getCartas().isEmpty()) {
            System.out.println(jugador.getNombre() + " no tiene Pokémon en la mano inicial, robando...");
            jugador.robarCarta();
        }
        if (!jugador.tienePokemonEnMano()) {
            System.out.println("¡" + jugador.getNombre() + " no tiene Pokémon ni en mano ni en mazo! Pierde la partida.");
        }
    }

    private void elegirPokemon(Jugador jugador) {
        System.out.println("\nMano de " + jugador.getNombre() + ":");
        mostrarMano(jugador);

        while (jugador.getPokemonActivo() == null) {
            int eleccion = leerEntero("Escoge tu Pokemon (índice): ");

            if (eleccion >= 0 && eleccion < jugador.getMano().size()) {
                Carta cartaElegida = jugador.getMano().get(eleccion);
                if (cartaElegida instanceof Pokemon) {
                    jugador.setPokemonActivo((Pokemon) cartaElegida);
                    jugador.getMano().remove(eleccion);
                    System.out.println("¡" + jugador.getNombre() + " ha lanzado a "
                            + jugador.getPokemonActivo().getGritoBatalla() + "!\n");
                } else {
                    System.out.println("Debes escoger un Pokémon.");
                }
            } else {
                System.out.println("¡Número inválido!");
            }
        }
    }

    // ─── Mostrar mano ───
    private void mostrarMano(Jugador jugador) {
        for (int i = 0; i < jugador.getMano().size(); i++) {
            Carta carta = jugador.getMano().get(i);
            System.out.println(i + ". " + carta.getNombre() + " (" + carta.getTipo() + ")");
        }
    }

    // ─── Multiplicador de daño por tipo ───
    private double multiplicadorTipo(String tipoAtacante, String tipoDefensor) {
        if (tipoAtacante.equals("Agua") && tipoDefensor.equals("Fuego")) return 1.5;
        if (tipoAtacante.equals("Fuego") && tipoDefensor.equals("Planta")) return 1.5;
        if (tipoAtacante.equals("Planta") && tipoDefensor.equals("Agua")) return 1.5;
        if (tipoAtacante.equals("Eléctrico") && tipoDefensor.equals("Agua")) return 1.5;
        return 1.0;
    }

    // ─── Jugar un turno ───
    private boolean jugarTurno(Jugador activo, Jugador rival) {
        System.out.println("\n#################");
        System.out.println("Turno de " + activo.getNombre());

        // Rellenar mano hasta 5 cartas si el mazo tiene cartas
        while (activo.getMano().size() < 5 && !activo.getMazo().getCartas().isEmpty()) {
            activo.robarCarta();
        }

        // Si el mazo está vacío, el rival gana la ronda y possibly la partida
        if (activo.getMazo().getCartas().isEmpty()) {
            System.out.println("¡" + activo.getNombre() + " no tiene más cartas! GANA " + rival.getNombre() + ".");
            rival.incrementarRondasGanadas();
            System.out.println(rival.getNombre() + " tiene " + rival.getRondasGanadas() + " ronda(s) ganada(s).");
            if (rival.getRondasGanadas() >= 3) {
                System.out.println("¡" + rival.getNombre() + " HA GANADO LA PARTIDA!");
                return false;
            }
            return false; // termina el combate, la siguiente ronda se maneja fuera si aplica
        }

        // Verificar que el activo tenga Pokémon activo
        if (activo.getPokemonActivo() == null) {
            System.out.println("¡" + activo.getNombre() + " no tiene Pokémon activo! Debe elegir uno.");
            elegirNuevoPokemon(activo);
            if (activo.getPokemonActivo() == null) {
                System.out.println("¡" + activo.getNombre() + " no pudo elegir Pokémon! GANA " + rival.getNombre() + ".");
                rival.incrementarRondasGanadas();
                return rival.getRondasGanadas() < 3;
            }
        }

        Pokemon pkmActivo = activo.getPokemonActivo();
        System.out.println("Tu Pokemon activo: " + pkmActivo.getNombre() + " (" + pkmActivo.getPs() + " PS)");
        System.out.println("¿Qué deseas hacer?");
        System.out.println("1. Atacar");
        System.out.println("2. Cambiar Pokemon");
        System.out.println("3. Usar carta Curativa (Entrenador/Pocion)");
        int opcion = leerEntero("Digita tu opción (1, 2 o 3): ");

        if (opcion == 1) {
            // ── ATACAR ──
            if (rival.getPokemonActivo() == null) {
                System.out.println("El rival no tiene Pokémon activo. No puedes atacar.");
            } else {
                Pokemon pkmRival = rival.getPokemonActivo();
                double mult = multiplicadorTipo(pkmActivo.getTipo(), pkmRival.getTipo());
                int dano = (int) (pkmActivo.getDanoAtaque() * mult);

                System.out.println(pkmActivo.getNombre() + " ataca a " + pkmRival.getNombre() + "!");
                if (mult > 1.0) {
                    System.out.println("¡Es super efectivo! (x" + mult + ")");
                }
                pkmRival.setPs(pkmRival.getPs() - dano);
                System.out.println("El rival recibe " + dano + " de daño y le quedan " + Math.max(0, pkmRival.getPs()) + " PS.");
            }

        } else if (opcion == 2) {
            // ── CAMBIAR POKÉMON ──
            if (activo.getMano().isEmpty()) {
                System.out.println("No tienes cartas en la mano para cambiar. Pierdes el turno.");
            } else {
                System.out.println("Tus cartas en la mano:");
                mostrarMano(activo);
                int numeroCambio = leerEntero("Elige el número de tu nuevo Pokemon: ");

                if (numeroCambio >= 0 && numeroCambio < activo.getMano().size()) {
                    Carta carta = activo.getMano().get(numeroCambio);
                    if (carta instanceof Pokemon) {
                        // Verificar límite de mano: si ya tiene 5 cartas, no se puede devolver el activo
                        if (activo.getMano().size() >= 5) {
                            System.out.println("Tu mano está llena (5 cartas). No puedes devolver el Pokémon activo. Pierdes el turno.");
                        } else {
                            // Devolver el activo a la mano curado al máximo
                            pkmActivo.curarAlMaximo();
                            activo.getMano().add(pkmActivo);
                            // Sacar el nuevo
                            activo.setPokemonActivo((Pokemon) carta);
                            activo.getMano().remove(numeroCambio);
                            System.out.println("Devolviste a " + pkmActivo.getNombre() + " (curado a " + pkmActivo.getPs() + " PS).");
                            System.out.println("Ahora peleas con " + activo.getPokemonActivo().getGritoBatalla());
                        }
                    } else {
                        System.out.println("Esa carta no es un Pokémon. Pierdes el turno.");
                    }
                } else {
                    System.out.println("Número fuera de rango. Pierdes el turno.");
                }
            }

        } else if (opcion == 3) {
            // ── CARTA CURATIVA ──
            if (activo.getMano().isEmpty()) {
                System.out.println("No tienes cartas en la mano. Pierdes el turno.");
            } else {
                System.out.println("Tus cartas en la mano:");
                mostrarMano(activo);
                int indiceCarta = leerEntero("Elige la carta curativa: ");

                if (indiceCarta >= 0 && indiceCarta < activo.getMano().size()) {
                    Carta cartaElegida = activo.getMano().get(indiceCarta);
                    if (cartaElegida instanceof Activable) {
                        ((Activable) cartaElegida).activar(activo);
                        activo.getMano().remove(indiceCarta);
                        if (!activo.getMazo().getCartas().isEmpty()) {
                            activo.robarCarta();
                            System.out.println("Has robado una carta del mazo.");
                        }
                    } else {
                        System.out.println("No es una carta curativa válida. Pierdes tu turno.");
                    }
                } else {
                    System.out.println("Número fuera de rango. Pierdes tu turno.");
                }
            }
        } else {
            System.out.println("Opción no válida. Pierdes tu turno.");
        }

        // ── Verificar si el Pokémon rival se debilitó ──
        if (rival.getPokemonActivo() != null && rival.getPokemonActivo().getPs() <= 0) {
            System.out.println("\n¡" + rival.getPokemonActivo().getNombre() + " se ha debilitado!");
            rival.setPokemonActivo(null); // forzar nueva elección

            activo.incrementarRondasGanadas();
            System.out.println("¡" + activo.getNombre() + " GANA LA RONDA! Rondas ganadas: " + activo.getRondasGanadas());

            if (activo.getRondasGanadas() >= 3) {
                System.out.println("¡" + activo.getNombre() + " HA GANADO LA PARTIDA!");
                return false;
            }

            // El rival debe elegir un nuevo Pokémon
            if (!rival.tienePokemonEnMano()) {
                // Intenta robar del mazo
                while (!rival.tienePokemonEnMano() && !rival.getMazo().getCartas().isEmpty()) {
                    rival.robarCarta();
                }
                if (!rival.tienePokemonEnMano()) {
                    System.out.println(rival.getNombre() + " no tiene más Pokemones disponibles.");
                    System.out.println("¡" + activo.getNombre() + " GANA LA PARTIDA!");
                    return false;
                }
            }

            System.out.println("\n" + rival.getNombre() + ", debes elegir tu nuevo Pokemon activo:");
            elegirNuevoPokemon(rival);
        }

        return true;
    }

    // ─── Elegir nuevo Pokémon después de un KO ───
    private void elegirNuevoPokemon(Jugador jugador) {
        boolean seleccionValida = false;
        while (!seleccionValida) {
            System.out.println("Mano de " + jugador.getNombre() + ":");
            mostrarMano(jugador);
            int eleccion = leerEntero("Elige el número de tu nuevo Pokemon: ");

            if (eleccion >= 0 && eleccion < jugador.getMano().size()) {
                Carta carta = jugador.getMano().get(eleccion);
                if (carta instanceof Pokemon) {
                    jugador.setPokemonActivo((Pokemon) carta);
                    jugador.getMano().remove(eleccion);
                    System.out.println(jugador.getNombre() + " ahora juega con "
                            + jugador.getPokemonActivo().getGritoBatalla());
                    seleccionValida = true;
                } else {
                    System.out.println("Esa carta no es un Pokémon.\n");
                }
            } else {
                System.out.println("Número fuera de rango.\n");
            }
        }
    }

    // ─── Iniciar combate ───
    public void iniciarCombate() {
        System.out.println("\n-- ¡INICIA EL COMBATE! --");
        boolean combateActivo = true;

        while (combateActivo
                && jugador1.getPokemonActivo() != null
                && jugador2.getPokemonActivo() != null) {

            combateActivo = jugarTurno(jugador1, jugador2);
            if (!combateActivo) break;

            leerEntero("\nEscribe 1 y presiona Enter para pasar al turno del rival: ");

            combateActivo = jugarTurno(jugador2, jugador1);
            if (!combateActivo) break;

            leerEntero("\nEscribe 1 y presiona Enter para iniciar la siguiente ronda: ");
        }

        System.out.println("\n-- FIN DEL COMBATE --");
        scanner.close();
    }

    public static void main(String[] args) {
        Juego partida = new Juego();
        partida.prepararPartida();
        partida.iniciarCombate();
    }
}
