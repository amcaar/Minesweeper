package buscaminas.sm;

import Buscaminas.sm.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import buscaminas.sm.controller.JuegoController;

/**
 * Boton de la interfaz de usuario que se corresponde con una casilla del
 * tablero de juego.
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public class BotonCasilla extends Button {

	public BotonCasilla(Context context, final JuegoController controlador,
			final int fila, final int columna) {
		super(context);
		this.setBackgroundResource(R.drawable.square_orange);
		this.setTypeface(null, Typeface.BOLD);
		this.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (!controlador.juegoTerminado())
					controlador.destaparCasilla(fila, columna);
			}
		});

		this.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View view) {
				controlador.establecerBandera(fila, columna);
				return true;
			}
		});

	}

	/**
	 * Determina el contenido gráfico de una casilla seleccionada por el
	 * usuario.
	 * 
	 * @param valor
	 *            Contenido numerico de la casilla destapada
	 */
	public void destapar(int valor) {
		if (valor == -1) {
			setIconoMina();
		} else if (valor == 0) {
			setIconoVacio();
		} else if (valor > 0) {
			setIconoValor(valor);
		}
	}

	/**
	 * Modifica el aspecto de la casilla colocando la imagen de una bomba.
	 */
	public void setIconoMina() {
		this.setBackgroundResource(R.drawable.square_bomb);
	}

	/**
	 * Modifica el aspecto de la casilla colocando la imagen de una casilla que
	 * carece de contenido.
	 */
	public void setIconoVacio() {
		this.setBackgroundResource(R.drawable.square_grey);
	}

	/**
	 * Determina el contenido gráfico de una casilla que contiene el numero de
	 * bombas adyacentes a la casilla. El color del texto dependera del valor
	 * numérico de la casilla.
	 * 
	 * @param valor
	 *            Contenido numerico de la casilla destapada
	 */
	public void setIconoValor(int valor) {
		this.setText(Integer.toString(valor));
		this.setBackgroundResource(R.drawable.square_grey);
		switch (valor) {
		case 1:
			this.setTextColor(Color.BLUE);
			break;
		case 2:
			this.setTextColor(Color.rgb(0, 100, 0));
			break;
		case 3:
			this.setTextColor(Color.RED);
			break;
		case 4:
			this.setTextColor(Color.rgb(85, 26, 139));
			break;
		case 5:
			this.setTextColor(Color.rgb(139, 28, 98));
			break;
		case 6:
			this.setTextColor(Color.rgb(238, 173, 14));
			break;
		case 7:
			this.setTextColor(Color.rgb(47, 79, 79));
			break;
		case 8:
			this.setTextColor(Color.rgb(71, 71, 71));
			break;
		}

	}

	/**
	 * Modifica el aspecto de la casilla colocando la imagen de una bandera o
	 * quitando la bandera.
	 * 
	 * @param establecida
	 *            Indica si la bandera ya se coloco en esa casilla
	 */
	public void setIconoBandera(boolean establecida) {
		if (establecida) {
			this.setBackgroundResource(R.drawable.square_flag);
		} else {
			this.setBackgroundResource(R.drawable.square_orange);
		}
	}

}
