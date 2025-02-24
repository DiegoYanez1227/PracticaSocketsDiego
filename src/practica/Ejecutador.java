package practica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Ejecutador {

	public static void main(String[] args) {

		Servidor servidor=new Servidor();
		servidor.ejecutar();
		//lanzador(args);
		
		Cliente c1= new Cliente(1);
		Cliente c2= new Cliente(2);
		Cliente c3= new Cliente(3);
		
		c1.start();
		c2.start();
		c3.start();


	}

	private static void lanzador(String[] args) {
		Scanner sc=null;
		Process proceso;

		ProcessBuilder pb= new ProcessBuilder(args);
		
		OutputStream os = null;
		OutputStreamWriter osw = null;
		PrintWriter pw=null;
		
		InputStream is= null;
		InputStreamReader isr= null;
		BufferedReader br= null;

		String linea;
		System.out.println("Dame el numero del que quieres calcular el doble");
		int numero=sc.nextInt();
		

		try {
			//el scanner lo necesitamos para enviale datos al hijo
			sc=new Scanner(System.in);
			proceso= pb.start();
			
			//Lee la entrada estandar del padre y la vuelca en el buffer que leera el hijo
			os=proceso.getOutputStream();
			osw= new OutputStreamWriter(os);
			pw= new PrintWriter(osw);
			
			// se lo estamos pasando al hijo desde aqui 
			pw.println(numero);
			
			
			is=proceso.getInputStream();
			isr=new InputStreamReader(is);
			br = new BufferedReader(isr);


			while((linea=br.readLine())!=null) {
				System.out.println(linea);
			}

		} catch (IOException e) {

		}finally {
			try {
				br.close();
				isr.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
