package buscaminas.sm.controller;

import buscaminas.sm.model.Fase;

/**
 * Observador del estado del juego, que permite notificar cuando se destapa una 
 * casilla y cuando se termina la partida y con que estado.
 * 
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public interface EstadoObserver {
    
    /**
     * Notifica el final de una partida, indicando si ha sido con victoria o 
     * con derrota.
     * 
     * @param faseFinal Fase en que ha terminado la partida
     */
    public void finPartida(Fase faseFinal);
    
    /**
     * Notifica que se ha destapado una casilla, indicando sus coordenadas y 
     * el valor contenido en ella.
     * 
     * @param x Fila de la casilla
     * @param y Columna de la casilla
     * @param valor Valor numerico de la casilla
     */
    public void casillaDestapada(int x, int y, int valor);
    
    /**
     * Notifica que se ha establecido o eliminado una bandera de una casilla, 
     * indicando las coordenadas.
     * 
     * @param x Fila de la bandera
     * @param y Columna de la bandera
     * @param establecida Estado de la bandera
     */
    public void banderaEstablecida(int x, int y, boolean establecida);
    
    /**
     * Indica cuantas banderas se pueden poner.
     * 
     * @param banderasRestantes Banderas que aun se pueden establecer
     */
    public void banderasRestantes(int banderasRestantes);
    
    
}
