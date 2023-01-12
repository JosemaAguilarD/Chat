package cliente_servidor;


import javax.swing.*;

import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;

public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
			
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}

class MarcoCliente extends JFrame{


	public MarcoCliente(){
		
		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		
		//CON ESTO HACEMOS QUE AL ABRIR LA VENTANA SE EJECUTE EL MÉTODO WINDOWOPENED
		addWindowListener(new EnvioOnline());
		}

}
//Envío de Señal Online
class EnvioOnline extends WindowAdapter{
	public void windowOpened(WindowEvent e) {
		try {
			Socket miSocket=new Socket("192.168.1.3",9999);
			
			PaqueteEnvio datos=new PaqueteEnvio();
			datos.setMensaje(" Online");
			 
			ObjectOutputStream paquete_datos=new ObjectOutputStream(miSocket.getOutputStream());
			
			paquete_datos.writeObject(datos);
			
			miSocket.close();
		}	
		catch(Exception e2){
			
		}
	}
}

class LaminaMarcoCliente extends JPanel implements Runnable{


	public LaminaMarcoCliente(){
		//PREGUNTANDOLE EL NICK
		String nick_usuario=JOptionPane.showInputDialog("Nick: ");
		
		JLabel n_nick = new JLabel("Nick: ");
		add(n_nick);

		nick = new JLabel();
		nick.setText(nick_usuario);
		add(nick);
	
		JLabel texto=new JLabel("-CHAT-");
		
		add(texto);
		
		//LLENANDO EL COMBO IP
		ip = new JComboBox();
//		ip.addItem("Usuario 1");
//		ip.addItem("Usuario 2");
//		ip.addItem("Usuario 3");
//		ip.addItem("192.168.1.3");
		add(ip);
		
		campoChat=new JTextArea(12,20);
		
		add(campoChat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		EnviaTexto	mievento = new EnviaTexto();
		miboton.addActionListener(mievento);
		
		add(miboton);	
		
		Thread miHilo = new Thread(this);
		miHilo.start();
	}
	
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
//			System.out.println(campo1.getText());
			
			campoChat.append("\n"+ nick.getText()+": " + campo1.getText());
		
			try {
				
				Socket misocket=new Socket("192.168.1.3",9999);
				
				PaqueteEnvio datos = new PaqueteEnvio();
				
				datos.setNick(nick.getText());
				
				datos.setIp(ip.getSelectedItem().toString());
				
				datos.setMensaje(campo1.getText());
				
				//CREAMOS EL FLUJO DE DATOS PARA ENVIAR, ESCRIBIMOS UN OBJETO  
				ObjectOutputStream paquete_datos = new ObjectOutputStream(misocket.getOutputStream());
				paquete_datos.writeObject(datos);
				
				misocket.close();
						
//				DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());
//				
//				//Escribe en el flujo lo que hay en el campo1, circulará por el socket y 
//				//el socket se está dirigiendo a ese servidor con esa ip y el puerto 9999
//				flujo_salida.writeUTF(campo1.getText());
//				
//				//Cerrando el flujo
//				flujo_salida.close();
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println(e1.getMessage());
			}
		}
			
		
	}
	

	private JTextField campo1;
	
	private JComboBox ip;
	
	private JLabel nick;
	
	private JButton miboton;
	
	private JTextArea campoChat;
	
	@Override
	public void run() {

		try {
			ServerSocket servidorCliente=new ServerSocket(9090);
			
			Socket cliente;
			
			PaqueteEnvio paqueteRecibido;
			while(true) {
				//Para que acepte todas las conexiones del exterior
				cliente = servidorCliente.accept();
				
				ObjectInputStream flujoEntrada=new ObjectInputStream(cliente.getInputStream());
				
				paqueteRecibido=(PaqueteEnvio) flujoEntrada.readObject();
				
				if(!paqueteRecibido.getMensaje().equals(" Online")) {
					campoChat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje());

				}
				else {
					
				campoChat.append("\n"+paqueteRecibido.getIps());
				ArrayList<String> IpsMenu=new ArrayList<String>();
				
				IpsMenu=paqueteRecibido.getIps();
					
				ip.removeAllItems();
				
				for(String z:IpsMenu) {
					ip.addItem(z);
				}
				
				}
				}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
	
class PaqueteEnvio implements Serializable{

		private String nick, ip, mensaje;
		private ArrayList<String> Ips;

		public ArrayList<String> getIps() {
			return Ips;
		}

		public void setIps(ArrayList<String> ips) {
			Ips = ips;
		}

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getMensaje() {
			return mensaje;
		}

		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		
	}

	
	
