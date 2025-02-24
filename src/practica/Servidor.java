package practica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	


	public void ejecutar() {
		PrintWriter pw= null;
		BufferedReader br= null;
		try {
			ServerSocket serverSocket= new ServerSocket(5000);
			System.out.println("conexion del servidor creada");
			
			Socket socket1= aceptarCliente(serverSocket);
			leerCadenas(socket1);	
			
			Socket socket2= aceptarCliente(serverSocket);
			leerCadenas(socket2);
			
			Socket socket3= aceptarCliente(serverSocket);
			leerCadenas(socket3);
			
			
			String linea;
			while((linea=br.readLine())!=null) {
				System.out.println("Servidor: "+ linea);
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			
			try {
				pw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Socket aceptarCliente(ServerSocket serverSocket) throws IOException {
		Socket socket = serverSocket.accept();
		System.out.println("Hola cliente.");
		return socket;
	}

	private static void leerCadenas(Socket socket) throws IOException {
		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		PrintWriter pw = new PrintWriter(osw, true);
		
		pw.println();
		
		pw.close();
		osw.close();
		os.close();
	}

}
