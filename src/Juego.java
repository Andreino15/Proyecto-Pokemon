import java.util.Scanner;

/**
 * Clase principal del juego. Controla todo el flujo de la partida:
 * creación de jugadores, reparto de cartas, turnos, ataques y victoria.
 *
 * Patrón de diseño: esta clase actúa como "controlador" del juego,
 * coordinando las acciones entre Jugador, Mazo, Pokemon y Entrenador.
 */
public class Juego {

    // ─── Atributos ───
    private Jugador jugador1;   // Primer jugador (humano 1)
    private Jugador jugador2;   // Segundo jugador (humano 2)
    private Scanner scanner;    // Lee la entrada del teclado (System.in)

    /**
     * Constructor: crea el Scanner y pide los nombres de ambos jugadores.
     * Se ejecuta una sola vez al iniciar el programa.
     */
    public Juego() {
        scanner = new Scanner(System.in);
        System.out.print("Ingresa el nombre J1: ");
        // .trim() elimina espacios en blanco al inicio/final del nombre
        this.jugador1 = new Jugador(scanner.nextLine().trim());
        System.out.print("Ingresa el nombre J2: ");
        this.jugador2 = new Jugador(scanner.nextLine().trim());
        System.out.println("¡¡A PELEAR!!");
    }

    // ─────────────────────────────────────────────────────────────
    // MÉTODO AUXILIAR DE LECTURA SEGURA
    // ─────────────────────────────────────────────────────────────

    /**
     * Lee un número entero del teclado de forma segura.
     *
     * ¿Por qué existe este método?
     * Si el usuario escribe texto en vez de un número, scanner.nextInt()
     * lanza una excepción (InputMismatchException) y el programa crashea.
     * Además, nextInt() deja un salto de línea pendiente en el buffer que
     * puede afectar lecturas posteriores con nextLine().
     *
     * Este método:
     *   1. Verifica con hasNextInt() si lo que viene es un número.
     *   2. Si es número, lo lee, limpia el buffer con nextLine() y lo retorna.
     *   3. Si NO es número, descarta la entrada inválida y vuelve a preguntar.
     *   → Bucle infinito hasta recibir un entero válido.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (scanner.hasNextInt()) {
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpia el salto de línea pendiente en el buffer
                return valor;
            } else {
                System.out.println("¡Entrada no válida! Escribe un número.");
                scanner.nextLine(); // descarta la entrada inválida del buffer
            }
        }
    }

    // ─────────────────────────────────────────────────────────────
    // FASE 1: PREPARAR LA PARTIDA
    // ─────────────────────────────────────────────────────────────

    /**
     * Reparte 5 cartas iniciales a cada jugador y luego cada uno elige
     * su Pokémon activo para empezar a pelear.
     */
    public void prepararPartida() {
        System.out.println("\nRepartiendo cartas...\n");
        // Repartir 5 cartas alternadas: primero J1, luego J2, 5 veces
        for (int i = 0; i < 5; i++) {
            jugador1.robarCarta();   // saca la carta del mazo y la pone en la mano
            jugador2.robarCarta();
        }
        // Por si la mano inicial no tiene ningún Pokémon (solo pociones),
        // se roba automáticamente hasta conseguir uno
        asegurarPokemonInicial(jugador1);
        asegurarPokemonInicial(jugador2);

        // Cada jugador elige su Pokémon inicial
        elegirPokemon(jugador1);
        elegirPokemon(jugador2);
    }

    /**
     * Garantiza que el jugador tenga al menos un Pokémon en la mano.
     * Si no lo tiene, roba cartas del mazo hasta conseguir uno.
     */
    private void asegurarPokemonInicial(Jugador jugador) {
        while (!jugador.tienePokemonEnMano() && !jugador.getMazo().getCartas().isEmpty()) {
            System.out.println(jugador.getNombre() + " no tiene Pokémon en la mano inicial, robando...");
            jugador.robarCarta();
        }
        if (!jugador.tienePokemonEnMano()) {
            System.out.println("¡" + jugador.getNombre() + " no tiene Pokémon ni en mano ni en mazo! Pierde la partida.");
        }
    }

    /**
     * Muestra la mano del jugador y le pide que elija un Pokémon válido.
     * Usa instanceof para verificar que la carta elegida sea de tipo Pokemon
     * (y no una poción, por ejemplo). El bucle continúa hasta que elija bien.
     */
    private void elegirPokemon(Jugador jugador) {
        System.out.println("\nMano de " + jugador.getNombre() + ":");
        mostrarMano(jugador);

        // El bucle se repite mientras no tenga Pokémon activo asignado
        while (jugador.getPokemonActivo() == null) {
            int eleccion = leerEntero("Escoge tu Pokemon (índice): ");

            // Verifica que el índice esté dentro del rango de la mano
            if (eleccion >= 0 && eleccion < jugador.getMano().size()) {
                Carta cartaElegida = jugador.getMano().get(eleccion);
                // instanceof verifica el tipo en tiempo de ejecución (polimorfismo)
                if (cartaElegida instanceof Pokemon) {
                    // Casteo seguro: como sabemos que es Pokemon, podemos convertirla
                    jugador.setPokemonActivo((Pokemon) cartaElegida);
                    jugador.getMano().remove(eleccion); // la saca de la mano (está en juego)
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

    /**
     * Imprime la mano del jugador con índices numerados (0, 1, 2...)
     * para que pueda elegir cartas por su posición en la lista.
     */
    private void mostrarMano(Jugador jugador) {
        for (int i = 0; i < jugador.getMano().size(); i++) {
            Carta carta = jugador.getMano().get(i);
            System.out.println(i + ". " + carta.getNombre() + " (" + carta.getTipo() + ")");
        }
    }

    // ─────────────────────────────────────────────────────────────
    // SISTEMA DE TIPOS (ventaja de daño x1.5)
    // ─────────────────────────────────────────────────────────────

    /**
     * Devuelve el multiplicador de daño según la tabla de tipos:
     *   Agua > Fuego, Fuego > Planta, Planta > Agua, Eléctrico > Agua
     * Cualquier otra combinación = x1.0 (daño normal)
     */
    private double multiplicadorTipo(String tipoAtacante, String tipoDefensor) {
        if (tipoAtacante.equals("Agua") && tipoDefensor.equals("Fuego")) return 1.5;
        if (tipoAtacante.equals("Fuego") && tipoDefensor.equals("Planta")) return 1.5;
        if (tipoAtacante.equals("Planta") && tipoDefensor.equals("Agua")) return 1.5;
        if (tipoAtacante.equals("Eléctrico") && tipoDefensor.equals("Agua")) return 1.5;
        return 1.0;
    }

    // ─────────────────────────────────────────────────────────────
    // FASE 2: LOS TURNOS (núcleo del combate)
    // ─────────────────────────────────────────────────────────────

    /**
     * Ejecuta un turno completo del jugador "activo" contra el "rival".
     *
     * @return true si el combate continúa, false si alguien ganó la partida
     */
    private boolean jugarTurno(Jugador activo, Jugador rival) {
        System.out.println("\n#################");
        System.out.println("Turno de " + activo.getNombre());

        // ── 1. Rellenar la mano hasta 5 cartas (si el mazo tiene) ──
        while (activo.getMano().size() < 5 && !activo.getMazo().getCartas().isEmpty()) {
            activo.robarCarta();
        }

        // ── 2. Condición de derrota: mazo vacío ──
        // Si el jugador activo no tiene cartas en el mazo, pierde la ronda
        if (activo.getMazo().getCartas().isEmpty()) {
            System.out.println("¡" + activo.getNombre() + " no tiene más cartas! GANA " + rival.getNombre() + ".");
            rival.incrementarRondasGanadas();
            System.out.println(rival.getNombre() + " tiene " + rival.getRondasGanadas() + " ronda(s) ganada(s).");
            if (rival.getRondasGanadas() >= 3) {
                System.out.println("¡" + rival.getNombre() + " HA GANADO LA PARTIDA!");
                return false;
            }
            return false; // termina el combate
        }

        // ── 3. Verificar que tenga un Pokémon activo ──
        // (por si quedó en null tras un KO anterior)
        if (activo.getPokemonActivo() == null) {
            System.out.println("¡" + activo.getNombre() + " no tiene Pokémon activo! Debe elegir uno.");
            elegirNuevoPokemon(activo);
            if (activo.getPokemonActivo() == null) {
                System.out.println("¡" + activo.getNombre() + " no pudo elegir Pokémon! GANA " + rival.getNombre() + ".");
                rival.incrementarRondasGanadas();
                return rival.getRondasGanadas() < 3;
            }
        }

        // ── 4. Mostrar estado y menú de acciones ──
        Pokemon pkmActivo = activo.getPokemonActivo();
        System.out.println("Tu Pokemon activo: " + pkmActivo.getNombre() + " (" + pkmActivo.getPs() + " PS)");
        System.out.println("¿Qué deseas hacer?");
        System.out.println("1. Atacar");
        System.out.println("2. Cambiar Pokemon");
        System.out.println("3. Usar carta Curativa (Entrenador/Pocion)");
        int opcion = leerEntero("Digita tu opción (1, 2 o 3): ");

        // ── OPCIÓN 1: ATACAR ──
        if (opcion == 1) {
            if (rival.getPokemonActivo() == null) {
                System.out.println("El rival no tiene Pokémon activo. No puedes atacar.");
            } else {
                Pokemon pkmRival = rival.getPokemonActivo();
                // Calcula el daño aplicando el multiplicador de tipos
                double mult = multiplicadorTipo(pkmActivo.getTipo(), pkmRival.getTipo());
                int dano = (int) (pkmActivo.getDanoAtaque() * mult); // (int) trunca el decimal

                System.out.println(pkmActivo.getNombre() + " ataca a " + pkmRival.getNombre() + "!");
                if (mult > 1.0) {
                    System.out.println("¡Es super efectivo! (x" + mult + ")");
                }
                // Resta el daño a los PS del rival
                pkmRival.setPs(pkmRival.getPs() - dano);
                // Math.max(0, ...) evita mostrar PS negativos
                System.out.println("El rival recibe " + dano + " de daño y le quedan "
                        + Math.max(0, pkmRival.getPs()) + " PS.");
            }

        // ── OPCIÓN 2: CAMBIAR POKÉMON ──
        } else if (opcion == 2) {
            if (activo.getMano().isEmpty()) {
                System.out.println("No tienes cartas en la mano para cambiar. Pierdes el turno.");
            } else {
                System.out.println("Tus cartas en la mano:");
                mostrarMano(activo);
                int numeroCambio = leerEntero("Elige el número de tu nuevo Pokemon: ");

                if (numeroCambio >= 0 && numeroCambio < activo.getMano().size()) {
                    Carta carta = activo.getMano().get(numeroCambio);
                    if (carta instanceof Pokemon) {
                        // REGLA: la mano tiene máximo 5 cartas.
                        // Si ya tiene 5, no puede devolver el activo → pierde el turno
                        if (activo.getMano().size() >= 5) {
                            System.out.println("Tu mano está llena (5 cartas). No puedes devolver el Pokémon activo. Pierdes el turno.");
                        } else {
                            // El Pokémon que deja el campo se cura a PS máximo y vuelve a la mano
                            pkmActivo.curarAlMaximo();
                            activo.getMano().add(pkmActivo);
                            // El nuevo Pokémon sale de la mano al campo
                            activo.setPokemonActivo((Pokemon) carta);
                            activo.getMano().remove(numeroCambio);
                            System.out.println("Devolviste a " + pkmActivo.getNombre()
                                    + " (curado a " + pkmActivo.getPs() + " PS).");
                            System.out.println("Ahora peleas con "
                                    + activo.getPokemonActivo().getGritoBatalla());
                        }
                    } else {
                        System.out.println("Esa carta no es un Pokémon. Pierdes el turno.");
                    }
                } else {
                    System.out.println("Número fuera de rango. Pierdes el turno.");
                }
            }

        // ── OPCIÓN 3: CARTA CURATIVA ──
        } else if (opcion == 3) {
            if (activo.getMano().isEmpty()) {
                System.out.println("No tienes cartas en la mano. Pierdes el turno.");
            } else {
                System.out.println("Tus cartas en la mano:");
                mostrarMano(activo);
                int indiceCarta = leerEntero("Elige la carta curativa: ");

                if (indiceCarta >= 0 && indiceCarta < activo.getMano().size()) {
                    Carta cartaElegida = activo.getMano().get(indiceCarta);
                    // Activable es la interfaz que implementa Entrenador (Pociones)
                    // Aquí se usa polimorfismo: se llama activar() sin saber si es
                    // Pocion o Julio Profe, cada uno sabe cuánto cura
                    if (cartaElegida instanceof Activable) {
                        ((Activable) cartaElegida).activar(activo); // aplica la curación
                        activo.getMano().remove(indiceCarta);       // la carta se gasta
                        // Roba una carta nueva como recompensa por usar la curativa
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

        // ── 5. Verificar si el Pokémon rival quedó debilitado (PS <= 0) ──
        if (rival.getPokemonActivo() != null && rival.getPokemonActivo().getPs() <= 0) {
            System.out.println("\n¡" + rival.getPokemonActivo().getNombre() + " se ha debilitado!");
            rival.setPokemonActivo(null); // el Pokémon debilitado sale del campo

            // El atacante gana la ronda
            activo.incrementarRondasGanadas();
            System.out.println("¡" + activo.getNombre() + " GANA LA RONDA! Rondas ganadas: "
                    + activo.getRondasGanadas());

            // Victoria de partida: 3 rondas ganadas
            if (activo.getRondasGanadas() >= 3) {
                System.out.println("¡" + activo.getNombre() + " HA GANADO LA PARTIDA!");
                return false;
            }

            // El rival debe elegir un nuevo Pokémon de su mano
            if (!rival.tienePokemonEnMano()) {
                // Si no tiene Pokémon en mano, intenta robar del mazo
                while (!rival.tienePokemonEnMano() && !rival.getMazo().getCartas().isEmpty()) {
                    rival.robarCarta();
                }
                // Si aun así no tiene, pierde la partida
                if (!rival.tienePokemonEnMano()) {
                    System.out.println(rival.getNombre() + " no tiene más Pokemones disponibles.");
                    System.out.println("¡" + activo.getNombre() + " GANA LA PARTIDA!");
                    return false;
                }
            }

            System.out.println("\n" + rival.getNombre() + ", debes elegir tu nuevo Pokemon activo:");
            elegirNuevoPokemon(rival);
        }

        return true; // el combate continúa
    }

    /**
     * Después de un KO, obliga al jugador a elegir un nuevo Pokémon de su mano.
     * Bucle hasta que elija un índice válido que corresponda a un Pokémon.
     */
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

    // ─────────────────────────────────────────────────────────────
    // FASE 3: BUCLE PRINCIPAL DEL COMBATE
    // ─────────────────────────────────────────────────────────────

    /**
     * Bucle principal: alterna turnos entre jugador1 y jugador2.
     * Continúa mientras combateActivo sea true y ambos tengan Pokémon activo.
     * Las pausas (escribir 1 + Enter) permiten que ambos jugadores vean
     * la pantalla antes de que juegue el otro.
     */
    public void iniciarCombate() {
        System.out.println("\n-- ¡INICIA EL COMBATE! --");
        boolean combateActivo = true;

        while (combateActivo
                && jugador1.getPokemonActivo() != null
                && jugador2.getPokemonActivo() != null) {

            // Turno del jugador 1
            combateActivo = jugarTurno(jugador1, jugador2);
            if (!combateActivo) break; // si alguien ganó, salir del bucle

            // Pausa para que el J2 vea lo que pasó
            leerEntero("\nEscribe 1 y presiona Enter para pasar al turno del rival: ");

            // Turno del jugador 2
            combateActivo = jugarTurno(jugador2, jugador1);
            if (!combateActivo) break;

            // Pausa antes de la siguiente ronda
            leerEntero("\nEscribe 1 y presiona Enter para iniciar la siguiente ronda: ");
        }

        System.out.println("\n-- FIN DEL COMBATE --");
        scanner.close(); // libera el recurso del Scanner
    }

    // ─────────────────────────────────────────────────────────────
    // PUNTO DE ENTRADA
    // ─────────────────────────────────────────────────────────────

    /**
     * main: crea la instancia de Juego y ejecuta las 2 fases:
     *   1. prepararPartida() → repartir cartas y elegir Pokémon inicial
     *   2. iniciarCombate()   → bucle de turnos hasta que alguien gane
     */
    public static void main(String[] args) {
        Juego partida = new Juego();
        partida.prepararPartida();
        partida.iniciarCombate();
    }
}
