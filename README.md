# Pokemon Proyect - Simulador Pokemon TCG Pocket (Genetic Apex)

## Miniproyecto 1 - Fundamentos de Programacion Orientada a Eventos
Universidad del Valle, Sede Tulua

Simulador por consola de un duelo Pokemon TCG Pocket: dos jugadores con mazos de 19 cartas, turnos por consola, cartas de entrenador (curativas), ventajas de tipo, y tres condiciones de victoria.

## Autores
- Andres Felipe Moreno Alfonso - 2569340 - Andreino15
- Jose David Palacios Vergara - 2569246 - bydavidp/Dajop12

## Estructura del proyecto

```
src/
├── Carta.java        Clase abstracta base (nombre, tipo)
├── Pokemon.java      Hereda de Carta: PS, daño, grito de batalla
├── Entrenador.java   Hereda de Carta, implementa Activable: pociones curativas
├── Activable.java    Interfaz para cartas que se pueden "usar" en turno
├── Mazo.java         Crea y baraja las 19 cartas del mazo
├── Jugador.java      Mano, mazo, Pokémon activo, rondas ganadas
└── Juego.java        Controlador: turnos, combate, condiciones de victoria
```

## Cómo compilar y ejecutar

```bash
javac -d bin src/*.java
java -cp bin Juego
```

## Cómo se juega

1.Ingresa el nombre de cada jugador.
2. Se reparten 5 cartas a cada uno. Si tu mano no tiene Pokémon, robas automáticamente hasta tener uno.
3. Cada jugador elige su Pokémon activo (por índice).
4. En tu turno eliges una acción:
   - **1. Atacar** — tu Pokémon hace daño al rival (con ventaja de tipo x1.5 si aplica).
   - **2. Cambiar Pokémon** — devuelves el activo a la mano (curado al máximo) y sacas otro. Solo si tu mano tiene menos de 5 cartas.
   - **3. Carta curativa** — usas una Pocion (+20 PS) o Julio Profe (+10 PS), la descartas y robas una carta.
5. Gana la ronda quien deje al Pokémon rival con 0 PS o menos.
6. Gana la partida quien gane 3 rondas.

## Condiciones de victoria

- **3 rondas ganadas**: el primer jugador en ganar 3 rondas gana la partida.
- **Mazo vacío**: si al inicio de tu turno tu mazo está vacío, tu rival gana la ronda.
- **Sin Pokémon disponibles**: si tu Pokémon se debilita y no tienes más Pokémon en mano ni en el mazo, tu rival gana la partida.

## Ventajas de tipo (daño x1.5)

| Atacante | vs Defensor |
|----------|-------------|
| Agua     | Fuego       |
| Fuego    | Planta      |
| Planta   | Agua        |
| Eléctrico| Agua        |

Cualquier otra combinación hace daño normal (x1.0).
