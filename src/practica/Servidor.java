package practica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
			String mensaje1=leerCadenas(socket1);
			tarea(socket1);
			Socket socket2= aceptarCliente(serverSocket);
			tarea(socket2);
			Socket socket3= aceptarCliente(serverSocket);
			tarea(socket3);
			
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

	private String leerCadenas(Socket socket) throws IOException {
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine(); 
    }
	

	private void enviarCadenas(Socket socket ,String mensaje) throws IOException {
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(mensaje);
    }

	private void tarea(Socket socket) {
	    String mensaje;
	    try {
	        while ((mensaje = leerCadenas(socket)) != null) {
	            double resultado = evaluarOperacion(mensaje);
	            enviarCadenas(socket, "Resultado: " + resultado);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	private double evaluarOperacion(String mensaje) {
	    try {
	        mensaje = mensaje.replaceAll("\\s+", ""); 


	        String operador = "";
	        for (char c : mensaje.toCharArray()) {
	            if (c == '+' || c == '-' || c == '*' || c == '/') {
	                operador = String.valueOf(c);
	                break;
	            }
	        }

	      
	        if (operador.isEmpty()) return Double.NaN;

	        String[] partes = mensaje.split("\\" + operador);
	        if (partes.length != 2) return Double.NaN;

	        double num1 = Double.parseDouble(partes[0]);
	        double num2 = Double.parseDouble(partes[1]);

	        return switch (operador) {
	            case "+" -> num1 + num2;
	            case "-" -> num1 - num2;
	            case "*" -> num1 * num2;
	            case "/" -> (num2 != 0) ? num1 / num2 : Double.NaN;
	            default -> Double.NaN;
	        };
	    } catch (Exception e) {
	        return Double.NaN; 
	    }
	}

}
