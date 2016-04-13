package buscaminas.sm.model;

/**
 * Una casilla de la cuadricula del buscaminas. Una casilla puede contener una
 * mina o no, y en este ultimo caso tiene un cierto numero de minas adyacentes
 * que pueda variar de 0 a 8. Las casillas empiezan todas tapadas y cuando el
 * usuario las selecciona pasan a estar destapadas. Si el usuario sospecha que
 * en una casilla hay una bomba puede colocar una bandera sobre ella.
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public class Casilla {

	/**
	 * Si la casilla se ha destapado ya o no.
	 */
	private boolean destapada;

	/**
	 * Si se ha establecido una bandera en la casilla o no.
	 */
	private boolean bandera;

	/**
	 * Contenido de la casilla. Si el valor es -1 significa que contiene una
	 * mina, y si es mayor indica el numero de minas adyacentes a la casilla.
	 */
	private int valor;

	/**
	 * Coordenada X de la casilla.
	 */
	private int x;

	/**
	 * Coordenada Y de la casilla.
	 */
	private int y;

	/**
	 * Crea una casilla ubicada en las coordenadas indicadas.
	 * 
	 * @param x
	 *            Coordenada X de la casilla
	 * @param y
	 *            Coordenada Y de la casilla
	 */
	public Casilla(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean isDestapada() {
		return destapada;
	}

	/**
	 * Marca como destapada la casilla.
	 */
	public void destapar() {
		this.destapada = true;
	}

	public boolean isBandera() {
		return bandera;
	}

	/**
	 * Coloca una bandera si no hay ninguna, y la quita ya se hubiese
	 * establecido.
	 */
	public void conmutarBandera() {
		this.bandera = !this.bandera;
	}

	public int getValor() {
		return valor;
	}

	/**
	 * Aumenta en uno la cantidad de minas adyacentes a la casilla.
	 */
	public void incrementarValor() {
		this.valor++;
	}

	public boolean isMina() {
		return valor == -1;
	}

	/**
	 * Coloca una mina en la casilla, estableciendo su valor a -1.
	 */
	public void colocarMina() {
		valor = -1;
	}

	/**
	 * Muestra las coordenadas y el valor de la casilla.
	 */
	@Override
	public String toString() {
		return String.format("[%d, %d] %d", x, y, getValor());
	}
}
