package buscaminas.sm.controller;

import java.util.List;

import buscaminas.sm.model.EstadoJuego;
import buscaminas.sm.model.Fase;

/**
 * Gestiona la funcionalidad principal del juego:
 * <ul>
 * <li>Gestionar los observadores del estado del juego.</li>
 * <li>Destapar las casillas.</li>
 * </ul>
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public class JuegoController {

	/**
	 * Filas del tablero de juego.
	 */
	public static final int JUEGO_FILAS = 11;

	/**
	 * Columnas del tablero de juego.
	 */
	public static final int JUEGO_COLUMNAS = 9;

	/**
	 * Porcentaje de minas sobre el total de casillas del tablero de juego.
	 */
	private static final int JUEGO_PORCENTAJE = 15;

	/**
	 * Estado completo del juego, que representa el tablero y el estado de todas
	 * sus casillas.
	 */
	private EstadoJuego juego;

	/**
	 * Genera una partida con dimensiones fijas 11x9 y un 15% de minas.
	 */
	public JuegoController() {
		generarPartida();
	}

	/**
	 * Crea una partida nueva. En caso de que ya se hubiese trabajado con una,
	 * se hace que sus observadores pasen a serlo de la partida nueva.
	 */
	public final void generarPartida() {
		List<EstadoObserver> observers = null;
		if (juego != null)
			observers = juego.getEstadoObservers();
		juego = new EstadoJuego(JUEGO_FILAS, JUEGO_COLUMNAS, JUEGO_PORCENTAJE);
		if (observers != null) {
			for (EstadoObserver o : observers)
				addEstadoObserver(o);
		}
	}

	public boolean addEstadoObserver(EstadoObserver o) {
		return juego.addEstadoObserver(o);
	}

	public boolean removeEstadoObserver(EstadoObserver o) {
		return juego.removeEstadoObserver(o);
	}

	/**
	 * Destapa una casilla del tablero de juego.
	 * 
	 * @param x
	 *            Fila de la casilla
	 * @param y
	 *            Columna de la casilla
	 */
	public void destaparCasilla(int x, int y) {
		juego.destaparCasilla(x, y);
	}

	/**
	 * Establece una bandera en una casilla del tablero de juego.
	 * 
	 * @param x
	 *            Fila de la casilla
	 * @param y
	 *            Columna de la casilla
	 */
	public void establecerBandera(int x, int y) {
		juego.establecerBandera(x, y);
	}

	/**
	 * Comprueba si el juego ha terminado, sea con victoria o con derrota.
	 * 
	 * @return Si el juego ha terminado
	 */
	public boolean juegoTerminado() {
		return juego.getFase() == Fase.DERROTA
				|| juego.getFase() == Fase.VICTORIA;
	}

}
