package practica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread{
	static Scanner sc= new Scanner(System.in);
	Socket socket= new Socket();
	
	private int id;

	
	public Cliente(int id ) {
		this.id=id;
	}

	public void run () {
		menu();
	}

	public void menu () {
		conectarAlServidor(socket);
		
		int opcion;
		do {
			System.out.println("Cliente "+id);
			System.out.println("Opciones:");
			System.out.println("0. Salir");
			System.out.println("1. Enviar una tarea al servidor");
			System.out.println("2. Recibir el resultado");
			System.out.println("Introduce una opcion");
			opcion= sc.nextInt();
			if(opcion==1) {
				enviarTarea();
			}else if(opcion ==2){
				recibirResultado();
			}else if(opcion ==0){
				System.out.println("El Cliente numero:"+id+" ha decido salir");
			}else {
				System.out.println("El Cliente numero:"+id+" no ha introducido ninguna de las opciones. \n Selecciona una opcion valida por favor");
			}
		}while(opcion!=0);
	}
	
	public synchronized void enviarTarea() {
		
		try {
			System.out.println("Introduce la tarea que le quieras dar al servidor");
			String mensajeEnviar=sc.nextLine();
			enviarCadenas(socket, mensajeEnviar);
			System.out.println("Se ha enviado correctamente la peticion del Cliente");
			join();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void recibirResultado() {
		try {
			join();
			String mensajeRecibido=leerCadenas(socket);
			System.out.println("El cliente ha leido el resultado:"+mensajeRecibido);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void conectarAlServidor(Socket socket) {
		
		try {
			System.out.println("Creando el socket Cliente "+id);
			System.out.println("Estableciendo conexion");
			InetSocketAddress addr = new InetSocketAddress("localhost",5000);
			socket.connect(addr);
			System.out.println("Se ha conectado correctamente");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String leerCadenas(Socket socket) throws IOException {
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String mensajeRecibido;
		
		while((mensajeRecibido=br.readLine())!=null){
			System.out.println("Se ha recibido un resultado");
		}
		br.close();
		isr.close();
		is.close();
		
		return mensajeRecibido;
	}
	
	private static void enviarCadenas(Socket socket, String mensajeEnviar) throws IOException {
		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		PrintWriter pw = new PrintWriter(osw, true);
		
		pw.println(mensajeEnviar);
		
		pw.close();
		osw.close();
		os.close();
	}
	
}
