package cliente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TODO: Complementa esta clase para que genere la conexi�n TCP con el servidor
 * para enviar un boleto, recibir la respuesta y finalizar la sesion
 * Añadimos todo lo correspondiente para poder realizar la conexion con el servidorr
 */
public class ClienteTCP {

	private Socket socketCliente = null;
	private BufferedReader entrada = null;
	private PrintWriter salida = null;

	/**
	 * Constructor
	 */
	public ClienteTCP(String ip, int puerto) {
		try {
			socketCliente = new Socket(ip, puerto);
			System.out.println("Conexión establecida: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.err.printf("Imposible conectar con ip:%s / puerto:%d", ip, puerto);
			System.exit(-1);
		}
	}

	/**
	 * @param combinacion que se desea enviar
	 * @return respuesta del servidor con la respuesta del boleto
	 * 
	 * Enviamos la combinacion y devolvemos la respuesta del servidor
	 */
	public String comprobarBoleto(int[] combinacion) {
		salida.println(combinacion);
		String msg = "";
		try {
			msg = entrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * Sirve para finalizar la la conexi�n de Cliente y Servidor
	 * Terminamos la conexion
	 */
	public void finSesion() {
		salida.println("FIN");
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Cliente Terminado");
	}

}
