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
    static Scanner sc = new Scanner(System.in);
    private Socket socket;
    private int id;

    public Cliente(int id) {
        this.id = id;
    }

    public void run() {
        conectarAlServidor();
        menu();
    }

    private void menu() {
        int opcion;
        do {
            System.out.println("Cliente " + id);
            System.out.println("Opciones:");
            System.out.println("0. Salir");
            System.out.println("1. Enviar una tarea al servidor");
            System.out.println("2. Recibir el resultado");
            System.out.print("Introduce una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    enviarTarea();
                    break;
                case 2:
                    recibirResultado();
                    break;
                case 0:
                    System.out.println("El Cliente número " + id + " ha decidido salir.");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtalo de nuevo.");
            }
        } while (opcion != 0);
    }

    private void enviarTarea() {
        try {
            System.out.print("Introduce la tarea (Ejemplo: 5 + 3): ");
            String mensajeEnviar = sc.nextLine();
            enviarCadenas(mensajeEnviar);
            System.out.println("Se ha enviado correctamente la petición.");
        } catch (IOException e) {
            System.err.println("Error al enviar la tarea: " + e.getMessage());
        }
    }

    private void recibirResultado() {
        try {
            String mensajeRecibido = leerCadenas();
            System.out.println("Resultado recibido: " + mensajeRecibido);
        } catch (IOException e) {
            System.err.println("Error al recibir el resultado: " + e.getMessage());
        }
    }

    private void conectarAlServidor() {
        try {
            System.out.println("Creando socket para Cliente " + id + "...");
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 5000));
            System.out.println("Conexión establecida con el servidor.");
        } catch (IOException e) {
            System.err.println("No se pudo conectar al servidor: " + e.getMessage());
        }
    }

    private void enviarCadenas(String mensaje) throws IOException {
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        pw.println(mensaje);
    }

    private String leerCadenas() throws IOException {
        InputStream is = socket.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        return br.readLine(); 
    }
	
}
