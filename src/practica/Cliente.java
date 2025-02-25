package practica;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente extends Thread {
    private static final Scanner sc = new Scanner(System.in);
    private static final Object lock = new Object(); // Objeto de bloqueo compartido
    private Socket socket;
    private int id;

    public Cliente(int id) {
        this.id = id;
    }

    public synchronized void run() {
        conectarAlServidor();
     
            try {
                enviarTarea();
                recibirResultado();
                lock.notifyAll(); 
            } catch (Exception e) {
                System.err.println("Error en cliente " + id + ": " + e.getMessage());
            }
        
    }

    private synchronized void enviarTarea() {
        try {
            System.out.print("Cliente " + id + " - Introduce la tarea (Ejemplo: 5 + 3): ");
            String mensajeEnviar = sc.nextLine();
           
                enviarCadenas(mensajeEnviar);
                System.out.println("Cliente " + id + " - Petición enviada correctamente.");
                lock.wait(); 
            
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al enviar la tarea en Cliente " + id + ": " + e.getMessage());
        }
    }

    private synchronized void recibirResultado() {
        try {
                String mensajeRecibido = leerCadenas();
                System.out.println("Cliente " + id + " - Resultado recibido: " + mensajeRecibido);
                lock.notifyAll(); 
        } catch (IOException e) {
            System.err.println("Error al recibir el resultado en Cliente " + id + ": " + e.getMessage());
        }
    }

    private void conectarAlServidor() {
        try {
            System.out.println("Cliente " + id + " - Creando socket...");
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 5000));
            System.out.println("Cliente " + id + " - Conectado al servidor.");
        } catch (IOException e) {
            System.err.println("Cliente " + id + " - No se pudo conectar al servidor: " + e.getMessage());
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
