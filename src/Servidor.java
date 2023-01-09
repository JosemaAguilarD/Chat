

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

//para que esté constantemente a la escucha, implementamos la interface Runnable
class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){
		
		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);
		
		//Haciendo el hilo 
		Thread mihilo=new Thread(this);
		mihilo.start();
		
		}
	

	@Override
	public void run() {
//		System.out.println("Estoy a la escucha");
		
		try {
			//LE DECIMOS QUE ESTÉ A LA ESCUCHA Y QUE HABRA ESE PUERTO
			ServerSocket servidor=new ServerSocket(9999);
			
			while(true) {
			//LE DECIMOS QUE ACEPTE LAS CONEXIONES QUE VIENEN DEL EXTERIOR
			Socket misocket=servidor.accept();
			
			//LE DECIMOS PORQUE SOCKET VIAJA LA INFORMACIÓN DE ENTRADA, QUE ES EL QUE ESTÁ ACEPTANDO TODAS LAS CONEXIONES
			//Y LEEMOS LO QUE HAY EN EL FLUJO DE DATOS
			DataInputStream flujo_entrada=new DataInputStream(misocket.getInputStream());
			
			//ACÁ TENEMOS ALMACENADO LO QUE VIAJA POR EL FLUJO DE DATOS QUE VIENE DEL CLIENTE
			String mensaje_texto=flujo_entrada.readUTF();
			
			//Agregamos el texto al JtextArea
			areatexto.append("\n"+mensaje_texto);

			//CERRAMOS LA CONEXIÓN
			misocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private	JTextArea areatexto;

}
