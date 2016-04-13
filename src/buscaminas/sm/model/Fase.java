package buscaminas.sm.model;

/**
 * Define las distintas fases en las que se puede encontrar la partida.
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public enum Fase {
	/**
	 * Aun no se ha destapado ninguna casilla
	 */
	INICIO,
	/**
	 * Se ha destapado alguna casilla
	 */
	JUGANDO,
	/**
	 * Se han destapado todas las casillas sin activar minas
	 */
	VICTORIA,
	/**
	 * Se ha destapado una casilla de una bomba
	 */
	DERROTA
}
