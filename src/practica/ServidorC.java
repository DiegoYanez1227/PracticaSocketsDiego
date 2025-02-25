package practica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServidorC {
	    private static final int PUERTO = 5000;
	    private static final int NUM_HILOS = 4;
	    private static BlockingQueue<Tarea> colaTareas = new LinkedBlockingQueue<>();
	    
	    public static void main(String[] args) {
	        new Servidor().ejecutar();
	    }

	    public void ejecutar() {
	        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
	            System.out.println("Servidor iniciado en el puerto " + PUERTO);
	            
	            // Iniciar los hilos procesadores de tareas
	            for (int i = 0; i < NUM_HILOS; i++) {
	                new ProcesadorTareas(i).start();
	            }

	            // Aceptar conexiones de clientes
	            while (true) {
	                Socket socketCliente = servidor.accept();
	                System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress());
	                new ManejadorCliente(socketCliente).start();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    // Clase interna para manejar las conexiones de los clientes
	    private static class ManejadorCliente extends Thread {
	        private Socket socket;

	        public ManejadorCliente(Socket socket) {
	            this.socket = socket;
	        }

	        public void run() {
	            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                 PrintWriter pw = new PrintWriter(socket.getOutputStream(), true)) {

	                String mensaje;
	                while ((mensaje = br.readLine()) != null) {
	                    System.out.println("Tarea recibida: " + mensaje);
	                    int idTarea = mensaje.hashCode(); // Generar un ID único basado en el contenido
	                    colaTareas.put(new Tarea(idTarea, mensaje, pw));
	                    System.out.println("Tarea encolada con ID: " + idTarea);
	                }
	            } catch (IOException | InterruptedException e) {
	                System.err.println("Error en el cliente: " + e.getMessage());
	            }
	        }
	    }

	    // Clase interna para procesar tareas en segundo plano
	    private static class ProcesadorTareas extends Thread {
	        private int idHilo;

	        public ProcesadorTareas(int idHilo) {
	            this.idHilo = idHilo;
	        }

	        public void run() {
	            while (true) {
	                try {
	                    Tarea tarea = colaTareas.take(); // Esperar hasta que haya una tarea
	                    System.out.println("Hilo " + idHilo + " procesando tarea: " + tarea.getExpresion());
	                    double resultado = evaluarExpresion(tarea.getExpresion());
	                    tarea.getWriter().println("Resultado: " + resultado);
	                    System.out.println("Hilo " + idHilo + " completó la tarea con resultado: " + resultado);
	                } catch (InterruptedException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

	    // Clase interna para representar una tarea
	    private static class Tarea {
	        private int id;
	        private String expresion;
	        private PrintWriter writer;

	        public Tarea(int id, String expresion, PrintWriter writer) {
	            this.id = id;
	            this.expresion = expresion;
	            this.writer = writer;
	        }

	        public String getExpresion() {
	            return expresion;
	        }

	        public PrintWriter getWriter() {
	            return writer;
	        }
	    }

	    // Método para evaluar expresiones matemáticas simples
	    private static double evaluarExpresion(String expresion) {
	        try {
	            String[] partes = expresion.split(" ");
	            if (partes.length != 3) return Double.NaN;

	            double num1 = Double.parseDouble(partes[0]);
	            String operador = partes[1];
	            double num2 = Double.parseDouble(partes[2]);

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

