

import javax.swing.*;

import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

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
		}

		
}

class LaminaMarcoCliente extends JPanel{
	
	public LaminaMarcoCliente(){
		
		nick = new JTextField(5);
		add(nick);
	
		JLabel texto=new JLabel("-CHAT-");
		
		add(texto);
		
		ip = new JTextField(8);
		add(ip);
		
		campoChat=new JTextArea(12,20);
		
		add(campoChat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");
		EnviaTexto	mievento = new EnviaTexto();
		miboton.addActionListener(mievento);
		
		add(miboton);	
		
	}
	
	private class EnviaTexto implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
//			System.out.println(campo1.getText());
			try {
				
				Socket misocket=new Socket("192.168.1.3",9999);
				
				DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());
				
				//Escribe en el flujo lo que hay en el campo1, circulará por el socket y 
				//el socket se está dirigiendo a ese servidor con esa ip y el puerto 9999
				flujo_salida.writeUTF(campo1.getText());
				
				//Cerrando el flujo
				flujo_salida.close();
				
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
	

	private JTextField campo1, nick, ip;
	
	private JButton miboton;
	
	private JTextArea campoChat;
	
}