package buscaminas.sm.model;

/**
 * Niveles de dificultad del juego, para cada uno de los cuales se modifican las
 * dimensiones y el porcentaje de minas del tablero.
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public enum Dificultad {
	/**
	 * Emplea el tablero de menores dimensiones y con menos minas.
	 */
	FACIL,
	/**
	 * Emplea un tablero de dimensiones y cantidad de minas intermedias.
	 */
	NORMAL,
	/**
	 * Emplea el tablero de mayores dimensiones y con mayor porcentaje de minas.
	 */
	DIFICIL

}
