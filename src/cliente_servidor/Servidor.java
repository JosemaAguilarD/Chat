package cliente_servidor;


import javax.swing.*;


import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
			
			String nick,ip,mensaje;
			
			ArrayList <String> listaIp=new ArrayList<String>();
			
			PaqueteEnvio paquete_recibido;			
			
			//EL WHILE HACE QUE SE MANTENGA CONSTANTE EN ESCUCHA
			while(true) {
			//LE DECIMOS QUE ACEPTE LAS CONEXIONES QUE VIENEN DEL EXTERIOR
			Socket misocket=servidor.accept();
			
			//CREAMOS EL FLUJO DE DATOS QUE RECIBE, LO CASTEAMOS Y GUARDAMOS EN UN OBJETO IGUAL
			ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());
			paquete_recibido=(PaqueteEnvio) paquete_datos.readObject(); 
			
			nick=paquete_recibido.getNick();
			ip=paquete_recibido.getIp();
			mensaje = paquete_recibido.getMensaje();
			
			if(!mensaje.equals(" Online")) {
			//Agregamos el texto al JtextArea
			areatexto.append("\n"+nick+": "+mensaje+" para "+ip);
//------------------------------------------------------------------
			//Hacemos un puente y el puerto de entrada será el 9090
			Socket enviaDestinatario=new Socket(ip,9090);
			
			//Le decimos que vamos a enviar el paquete por este socket, tenemos que llenar el paqueteReenvio
			ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());
			//Llenamos el paquete
			paqueteReenvio.writeObject(paquete_recibido);
			
			paqueteReenvio.close();
			
			enviaDestinatario.close();
			misocket.close();

			} else {
				//----------------------------DETECTA ONLINE-----------------------------------------------------------
				//ALMACENAMOS LA DIRECCIÓN DEL CLIENTE QUE NOS ACABAMOS DE CONECTAR CON getInetAddress()
				InetAddress localizacion = misocket.getInetAddress();
				
				//CON getHostAddress OBTENEMOS SU IP
				String ipRemota=localizacion.getHostAddress();
				
				System.out.println("Online " + ipRemota);
				
				//AGREGANDO LAS IPS AL ARRAYLIST
				listaIp.add(ipRemota);
				
				//Enviamos en el paquete el arraylist para que el cliente llene el combo de ips
				paquete_recibido.setIps(listaIp);
				
				for(String z:listaIp) {
					System.out.println("Arrray: "+z);
					
					Socket enviaDestinatario=new Socket(z,9090);
					
					ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());

					paqueteReenvio.writeObject(paquete_recibido);
					
					paqueteReenvio.close();
					
					enviaDestinatario.close();
					misocket.close();
				}
	//------------------------------------------------------------------------------------------------------
			}
			//CERRAMOS LA CONEXIÓN
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private	JTextArea areatexto;

}
