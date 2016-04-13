package buscaminas.sm.model;

import buscaminas.sm.controller.EstadoObserver;
import java.util.LinkedList;
import java.util.List;

/**
 * Estado general del tablero de juego del buscaminas, que agrupa todas las
 * casillas.
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public class EstadoJuego {

	/**
	 * Porcentaje de casillas del total que tienen una mina.
	 */
	private float porcentajeMinas;

	/**
	 * Numero de minas establecidas.
	 */
	private int minasTotales;

	/**
	 * Numero de banderas establecidas hasta el momento (puede variar durante la
	 * partida).
	 */
	private int banderasEstablecidas;

	/**
	 * Fase en la que se encuentra el juego (jugando, terminado...).
	 */
	private Fase fase;

	/**
	 * Casillas del tablero de juego.
	 */
	private Casilla[][] casillas;

	/**
	 * Observadores del estado del juego.
	 */
	private List<EstadoObserver> observadores;

	/**
	 * Crea una representacion del estado del juego, generando el tablero de
	 * casillas y los argumentos básicos para colocar las minas.
	 * 
	 * @param x
	 *            Filas del tablero
	 * @param y
	 *            Columnas del tablero
	 * @param porcentajeMinas
	 *            Porcentaje de minas sobre el total de casillas
	 */
	public EstadoJuego(int x, int y, float porcentajeMinas) {
		if (x < 0 || y < 0)
			throw new IllegalArgumentException(
					"Las dimensiones de una matriz deben ser positivas");
		this.casillas = new Casilla[x][y];
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				casillas[i][j] = new Casilla(i, j);
			}
		}
		this.porcentajeMinas = porcentajeMinas;
		this.minasTotales = (int) (x * y * porcentajeMinas / 100);
		this.banderasEstablecidas = 0;
		this.fase = Fase.INICIO;
		this.observadores = new LinkedList<EstadoObserver>();
	}

	public Fase getFase() {
		return fase;
	}

	public List<EstadoObserver> getEstadoObservers() {
		return observadores;
	}

	public boolean addEstadoObserver(EstadoObserver o) {
		return observadores.add(o);
	}

	public boolean removeEstadoObserver(EstadoObserver o) {
		return observadores.remove(o);
	}

	/**
	 * <p>
	 * Cuando se intenta destapar una casilla, se comprueba que la fase de la
	 * partida es una donde se acepta que se destapen. Si es asi, se comprueba
	 * que la casilla existe y que no se ha destapado ya.
	 * </p>
	 * <p>
	 * Si en la fase del juego que se encuentra aun no se habia destapado
	 * ninguna casilla, se generan las minas y se calculan los valores de minas
	 * adyacentes a cada casilla, y se notifica el numero de banderas que se
	 * pueden colocar (coincide con el de las minas).
	 * </p>
	 * <p>
	 * A continuacion se destapa la casilla y se notifica a los observadores su
	 * valor. Si es una mina se ha perdido la partida, se destapa todo el
	 * tablero y se notifica al jugador. Si no es una mina, en caso de no tener
	 * ninguna adyacente se van destapando de manera recursiva hasta que se
	 * llega a alguna que tenga minas cercanas.
	 * </p>
	 * <p>
	 * Tras el destape, se comprueba si se ha ganado la partida y en tal caso se
	 * notifica al jugador.
	 * </p>
	 * 
	 * @param x
	 *            Fila de la casilla a destapar
	 * @param y
	 *            Columna de la casilla a destapar
	 * 
	 * @throws IllegalArgumentException
	 *             Si se intenta destapar una casilla cuando se ha terminado la
	 *             partida
	 */
	public void destaparCasilla(int x, int y) {
		if (fase != Fase.INICIO && fase != Fase.JUGANDO)
			throw new IllegalStateException(
					"No puede jugar una partida finalizada");

		if (!existeCasilla(x, y)) {
			return;
		}

		Casilla casilla = casillas[x][y];
		if (casilla.isDestapada())
			return;
		if (fase == Fase.INICIO) {
			// Colocar las bombas y establecer el valor de las adyacencias
			initCuadricula(x, y);
			// Notificar cuantas banderas se pueden poner aun
			for (EstadoObserver o : observadores) {
				o.banderasRestantes(getBanderasRestantes());
			}
			fase = Fase.JUGANDO;
		}

		casilla.destapar();

		// Notificar a los observadores del valor de la casilla destapado
		for (EstadoObserver o : observadores) {
			o.casillaDestapada(x, y, casilla.getValor());
		}

		if (casilla.isMina()) {
			fase = Fase.DERROTA;
			// Destapar todas las casillas
			destaparTableroCompleto();
			// Notificar a los observadores de que se ha perdido la partida
			for (EstadoObserver o : observadores) {
				o.finPartida(fase);
			}
			return;
		}

		// Si la casilla no tiene ninguna mina alrededor,
		// destapar todas las adyacentes
		if (casilla.getValor() == 0) {
			for (int fila = x - 1; fila <= x + 1; fila++) {
				for (int columna = y - 1; columna <= y + 1; columna++) {
					// Si la casilla adyacente no existe, continuar con otra
					if ((x == fila && y == columna)
							|| !existeCasilla(fila, columna))
						continue;
					// Si la casilla adyacente no es mina y
					// no se ha destapado ya, destaparla
					Casilla adyacente = casillas[fila][columna];
					if (!adyacente.isMina() && !adyacente.isDestapada())
						destaparCasilla(fila, columna);
				}
			}
		}

		if (comprobarVictoria()) {
			fase = Fase.VICTORIA;
			// Notificar a los observadores de que se ha ganado la partida
			for (EstadoObserver o : observadores) {
				o.finPartida(fase);
			}
		}

	}

	/**
	 * Obtiene el numero de banderas que se pueden colocar, que se corresponden
	 * con el total de minas menos las banderas que ya se hayan colocado.
	 * 
	 * @return Las banderas que se pueden colocar
	 */
	public int getBanderasRestantes() {
		return minasTotales - banderasEstablecidas;
	}

	/**
	 * <p>
	 * Cuando se intenta colocar una bandera, se comprueba que la fase de la
	 * partida es una donde se acepta que se coloquen. Si es asi, se comprueba
	 * que la casilla existe y que no se ha destapado ya.
	 * </p>
	 * <p>
	 * Si ya se hubiesen establecido todas las banderas posibles y se intentase
	 * colocar otra, o si ya se hubiesen quitado todas y se intentase quitar
	 * otra mas, no se permitiria la accion.
	 * </p>
	 * <p>
	 * En caso de que no se produzca ninguna situacion anomala, se conmuta el
	 * estado de la bandera (si hubiese se quita, si no hubiese se coloca), y se
	 * notifica la accion realizada y el numero de banderas que se sigue
	 * permitiendo colocar.
	 * </p>
	 * 
	 * @param x
	 *            La fila de la bandera
	 * @param y
	 *            La columna de la bandera
	 */
	public void establecerBandera(int x, int y) {
		if (fase != Fase.INICIO && fase != Fase.JUGANDO)
			throw new IllegalStateException(
					"No puede colocar una bandera en una partida finalizada");

		if (!existeCasilla(x, y)) {
			return;
		}

		Casilla casilla = casillas[x][y];
		if (casilla.isDestapada())
			return;

		// Comprobar la validez de colocar o quitar una bandera mas
		if (banderasEstablecidas == minasTotales && !casilla.isBandera())
			return;
		if (banderasEstablecidas == 0 && casilla.isBandera())
			return;

		casilla.conmutarBandera();
		if (casilla.isBandera())
			banderasEstablecidas++;
		else
			banderasEstablecidas--;

		// Notificar que se ha establecido/eliminado la bandera,
		// y cuantas se pueden poner aun
		for (EstadoObserver o : observadores) {
			o.banderaEstablecida(x, y, casilla.isBandera());
			o.banderasRestantes(getBanderasRestantes());
		}

	}

	/**
	 * Destapa todas las casillas del tablero (que no lo estuviesen ya) y
	 * notifica a los observadores del estado del juego de todos los valores de
	 * estas.
	 */
	private void destaparTableroCompleto() {
		for (int i = 0; i < casillas.length; i++) {
			for (int j = 0; j < casillas[0].length; j++) {
				Casilla casilla = casillas[i][j];
				if (!casilla.isDestapada())
					casilla.destapar();
				for (EstadoObserver o : observadores) {
					o.casillaDestapada(i, j, casilla.getValor());
				}
			}
		}
	}

	/**
	 * <p>
	 * Coloca las minas en la cuadricula hasta llegar al total solicitado,
	 * excluyendo la casilla donde se haya realizado la primera pulsacion. La
	 * posibilidad de establecer una mina en una casilla se calcula de manera
	 * aleatoria.
	 * </p>
	 * <p>
	 * Una vez colocada una mina, todas las casillas a su alrededor incrementan
	 * en uno el valor de minas adyacentes.
	 * </p>
	 * 
	 * @param x
	 *            La fila de la primera casilla pulsada
	 * @param y
	 *            La columna de la primera casilla pulsada
	 */
	private void initCuadricula(int x, int y) {
		int minasEstablecidas = 0;
		while (minasEstablecidas < minasTotales) {
			for (int i = 0; i < casillas.length; i++) {
				for (int j = 0; j < casillas[0].length; j++) {
					if (i != x && y != j) {
						double establecerMina = Math.random() * 100;
						if (!casillas[i][j].isMina()
								&& establecerMina <= porcentajeMinas) {
							casillas[i][j].colocarMina();
							// Aumentar el valor de las casillas adyacentes
							for (int fila = i - 1; fila <= i + 1; fila++) {
								for (int columna = j - 1; columna <= j + 1; columna++) {
									// Si la casilla adyacente no existe,
									// continuar con otra
									if ((i == fila && j == columna)
											|| !existeCasilla(fila, columna))
										continue;
									// Si la casilla adyacente no es mina,
									// incrementar su valor
									Casilla adyacente = casillas[fila][columna];
									if (!adyacente.isMina())
										adyacente.incrementarValor();
								}
							}
							minasEstablecidas++;
						}
					}
					// Si ya se han establecido todas las minas, terminar
					if (minasEstablecidas == minasTotales)
						return;
				}
			}
		}
	}

	/**
	 * Comprueba que la casilla ubicada en ciertas coordenadas es valida, es
	 * decir, que las coordenadas se encuentran entre 0 y el maximo de
	 * filas/columnas.
	 * 
	 * @param x
	 *            La fila de la casilla
	 * @param y
	 *            La columna de la casilla
	 * @return Si la casilla se encuentra en un rango valido
	 */
	private boolean existeCasilla(int x, int y) {
		return x >= 0 && x < casillas.length && y >= 0
				&& y < casillas[0].length;
	}

	/**
	 * Comprueba si se ha ganado la partida destapando todas las casillas que no
	 * son bombas.
	 * 
	 * @return Si se ha ganado o no
	 */
	private boolean comprobarVictoria() {
		for (int i = 0; i < casillas.length; i++) {
			for (int j = 0; j < casillas[0].length; j++) {
				Casilla c = casillas[i][j];
				if (!c.isMina() && !c.isDestapada()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Devuelve el contenido de todas las casillas del tablero.
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < casillas.length; i++) {
			for (int j = 0; j < casillas[0].length; j++) {
				buffer.append(String.format("%d ", casillas[i][j].getValor()));
			}
			buffer.append("\n");
		}
		return buffer.toString();
	}

}
