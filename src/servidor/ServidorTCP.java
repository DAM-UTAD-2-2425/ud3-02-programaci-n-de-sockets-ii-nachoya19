package servidor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * TODO: Complementa esta clase para que acepte conexiones TCP con clientes para
 * recibir un boleto, generar la respuesta y finalizar la sesion
 */
public class ServidorTCP {
	private String[] respuesta;
	private int[] combinacion;
	private int reintegro;
	private int complementario;
	private Socket socketCliente;
	private ServerSocket socketServidor;
	private BufferedReader entrada;
	private PrintWriter salida;

	/**
	 * Constructor
	 */
	public ServidorTCP(int puerto) {
		this.socketCliente = null;
		this.socketServidor = null;
		this.entrada = null;
		this.salida = null;
		this.respuesta = new String[9];
		this.respuesta[0] = "Boleto inv�lido - N�meros repetidos";
		this.respuesta[1] = "Boleto inv�lido - n�meros incorretos (1-49)";
		this.respuesta[2] = "6 aciertos";
		this.respuesta[3] = "5 aciertos + complementario";
		this.respuesta[4] = "5 aciertos";
		this.respuesta[5] = "4 aciertos";
		this.respuesta[6] = "3 aciertos";
		this.respuesta[7] = "Reintegro";
		this.respuesta[8] = "Sin premio";
		try {
			socketServidor = new ServerSocket(puerto);
			System.out.println("Esperando conexion...");
			socketCliente = socketServidor.accept();
			System.out.println("Conexion acceptada: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + puerto);
			System.exit(-1);
		}
		generarCombinacion();
		imprimirCombinacion();
	}

	/**
	 * @return Debe leer la combinacion de numeros que le envia el cliente Lee la
	 *         combinacion de numeros ingresada
	 */
	public String leerCombinacion() {
		String respuesta = "";
		try {
			respuesta = entrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respuesta;
	}

	/**
	 * @return Debe devolver una de las posibles respuestas configuradas Recorremos
	 *         el la combinación y la analizamos para devolver los casos
	 *         correspondientes
	 */
	public String comprobarBoleto() {
		int contador = 0;
		boolean numeros = false;
		boolean reintegroCondicion = false;
		for (int i = 0; i <= respuesta.length - 1; i++) {
			if (leerCombinacion().equals(combinacion[i])) {
				contador++;
			}
			if (Integer.valueOf(respuesta[i]) < 0 || Integer.valueOf(respuesta[i]) >= 50) {
				numeros = true;

			}
			if (Integer.valueOf(respuesta[i]) == this.reintegro) {
				reintegroCondicion = true;

			}

		}
		if (numeros) {
			return respuesta[1];
		} else if (leerCombinacion().equals(combinacion)) {
			return respuesta[2];
		} else if (contador == 5) {
			return respuesta[4];
		} else if (contador == 4) {
			return respuesta[5];
		} else if (contador == 3) {
			return respuesta[6];
		} else if (reintegroCondicion) {
			return respuesta[7];
		}

		return respuesta[8];
	}

	/**
	 * @param respuesta se debe enviar al ciente
	 * Le enviamos al cliente el resultado
	 */
	public void enviarRespuesta(String respuesta) {
		salida.println(comprobarBoleto());
	}

	/**
	 * Cierra el servidor
	 * Cerramos el servidor
	 */
	public void finSesion() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
			socketServidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");
	}

	/**
	 * Metodo que genera una combinacion. NO MODIFICAR
	 */
	private void generarCombinacion() {
		Set<Integer> numeros = new TreeSet<Integer>();
		Random aleatorio = new Random();
		while (numeros.size() < 6) {
			numeros.add(aleatorio.nextInt(49) + 1);
		}
		int i = 0;
		this.combinacion = new int[6];
		for (Integer elto : numeros) {
			this.combinacion[i++] = elto;
		}
		this.reintegro = aleatorio.nextInt(49) + 1;
		this.complementario = aleatorio.nextInt(49) + 1;
	}

	/**
	 * Metodo que saca por consola del servidor la combinacion
	 */
	private void imprimirCombinacion() {
		System.out.print("Combinaci�n ganadora: ");
		for (Integer elto : this.combinacion)
			System.out.print(elto + " ");
		System.out.println("");
		System.out.println("Complementario:       " + this.complementario);
		System.out.println("Reintegro:            " + this.reintegro);
	}

}
