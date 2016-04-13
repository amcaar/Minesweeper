package buscaminas.sm;

import Buscaminas.sm.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import buscaminas.sm.controller.EstadoObserver;
import buscaminas.sm.controller.JuegoController;
import buscaminas.sm.model.Fase;

/**
 * Tablero que representa el estado completo del juego.
 * 
 * @author Amanda Calatrava Arroyo
 * @author Cristina Yenyxe Gonzalez Garcia
 */
public class Tablero extends Activity implements EstadoObserver {

	// Atributos de la clase
	private ImageButton botonInicio;
	private ImageButton botonSalida;
	private TextView contadorBanderas;

	private TableLayout campoMinas;

	private BotonCasilla botones[][];
	private int tamañoBoton = 24;
	private int espaciado = 2;

	private int filas = JuegoController.JUEGO_FILAS;
	private int columnas = JuegoController.JUEGO_COLUMNAS;

	private JuegoController controlador = new JuegoController();

	/**
	 * Metodo principal encargado de poner en marcha la aplicacion
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		contadorBanderas = (TextView) findViewById(R.id.Crono);

		// Especificamos una fuente concreta para el tiempo y las mimas
		// restantes
		Typeface lcdFont = Typeface.createFromAsset(getAssets(),
				"fonts/digital-7.ttf");
		contadorBanderas.setTypeface(lcdFont);

		botonInicio = (ImageButton) findViewById(R.id.Smiley);
		botonInicio.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// Inicializar el tablero tras recibir un click sobre el boton
				// smile
				dibujarCampoMinas();
				controlador.generarPartida();
			}
		});

		botonSalida = (ImageButton) findViewById(R.id.Exit);
		botonSalida.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				// Salir de la aplicacion
				alertaSalir();
			}
		});

		controlador.addEstadoObserver(this);
		avisoGrafico("Haz clic sobre el smiley para empezar a jugar", 2000,
				true, false);
	}

	/**
	 * <p>
	 * Muestra en la interfaz gráfica la matriz de casillas que componen el
	 * juego.
	 * </p>
	 * <p>
	 * La colocación se realiza en la zona reservada en el xml para ello.
	 * </p>
	 */

	private void dibujarCampoMinas() {
		if (campoMinas != null)
			campoMinas.removeAllViews();
		campoMinas = (TableLayout) findViewById(R.id.CampoMinas);
		// Inicializar matriz de botonCasilla
		botones = new BotonCasilla[filas][columnas];
		for (int f = 0; f < filas; f++) {
			for (int c = 0; c < columnas; c++) {
				botones[f][c] = new BotonCasilla(this, controlador, f, c);
			}
		}
		// Inicializar la parte grafica
		for (int f = 0; f < filas; f++) {
			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new LayoutParams(
					(tamañoBoton + 2 * espaciado) * columnas, tamañoBoton + 2
							* espaciado));

			for (int c = 0; c < columnas; c++) {
				botones[f][c].setLayoutParams(new LayoutParams(tamañoBoton + 2
						* espaciado, tamañoBoton + 2 * espaciado));
				botones[f][c].setPadding(espaciado, espaciado, espaciado,
						espaciado);
				tableRow.addView(botones[f][c]);
			}
			campoMinas.addView(tableRow, new TableLayout.LayoutParams(
					(tamañoBoton + 2 * espaciado) * columnas, tamañoBoton + 2
							* espaciado));
		}
	}

	/**
	 * Crea un aviso grafico y lo muestra por pantalla
	 * 
	 * @param mensaje
	 *            Cadena de texto que se desea mostrar al usuario
	 * @param milisegundos
	 *            Tiempo que permanecera en pantalla el mensaje
	 * @param smile
	 *            Indica si el mensaje es el inicial
	 * @param cool
	 *            Indica si el mensaje es consecuencia de haber ganado la
	 *            partida
	 */

	private void avisoGrafico(String mensaje, int milisegundos, boolean smile,
			boolean cool) {
		// Creamos el dialogo
		Toast dialog = Toast.makeText(getApplicationContext(), mensaje,
				Toast.LENGTH_LONG);
		// Lo colocamos en pantalla
		dialog.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout dialogView = (LinearLayout) dialog.getView();
		// Creamos la imagen que va a ir en el mensaje (en funcion del tipo de
		// mensaje que vayamos a sacar)
		ImageView coolImage = new ImageView(getApplicationContext());

		if (smile) { // Mensaje inicial
			coolImage.setImageResource(R.drawable.smile);
		}

		else if (cool) { // Mensaje cuando ganas
			coolImage.setImageResource(R.drawable.cool);
		}

		else { // Mensaje cuando pierdes
			coolImage.setImageResource(R.drawable.sad);
		}
		// Añadimos el cuadro de dialogo, fijamos la duracion en pantalla y lo
		// hacemos visible
		dialogView.addView(coolImage, 0);
		dialog.setDuration(milisegundos);
		dialog.show();
	}

	/**
	 * Muestra un aviso al usuario para elegir si abandonar o no la aplicacion
	 */

	private void alertaSalir() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("¿Seguro que deseas salir?").setCancelable(false)
				.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Tablero.this.finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	// ////////////////////////////////////////////////////////////////////////
	// /// Metodos necesarios que implementan la interfaz JuegoController /////
	// ////////////////////////////////////////////////////////////////////////

	public void finPartida(Fase faseFinal) {
		if (faseFinal == Fase.VICTORIA) {
			avisoGrafico("¡¡Felicidades, has ganado!", 4000, false, true);
		} else if (faseFinal == Fase.DERROTA) {
			avisoGrafico("Has perdido, vuelve a intentarlo", 4000, false, false);
		}
	}

	public void casillaDestapada(int x, int y, int valor) {
		botones[x][y].destapar(valor);
	}

	public void banderaEstablecida(int x, int y, boolean establecida) {
		botones[x][y].setIconoBandera(establecida);
	}

	public void banderasRestantes(int banderasRestantes) {
		contadorBanderas.setText(Integer.toString(banderasRestantes));
	}

}