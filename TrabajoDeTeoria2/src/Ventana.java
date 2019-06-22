import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ventana extends JFrame implements ActionListener{
	private JPanel panel; // crea un panel para la ventana
	private JButton boton1;
	private JButton boton2;
	private JButton boton3;
	private JTextField campoValor;
	
	public Ventana() {
		this.panel=new JPanel();// new de panel 
		panel.setLayout(null);
		this.setSize(250,250);//tamanio de la ventana
		this.colocarBoton();// pone botones
		this.colocarCajaDeTexto();//pone caja de texto
		this.getContentPane().add(panel);//agrega panel
		this.setVisible(true);//visivilidad de la ventana
	}
	
	private void colocarCajaDeTexto() {// creo caja de texto
		// TODO Auto-generated method stub
		campoValor=new JTextField("insertar umbral");
		campoValor.setBounds(100, 0, 100, 40);
		panel.add(campoValor);
	}

	private void colocarBoton() { //creo y agrego botones
		boton1=new JButton("Comprimir");
		boton1.setBounds(0, 0, 100, 40);
		boton1.addActionListener((ActionListener) this);
		panel.add(boton1);
		boton2=new JButton("Descomprimir");
		boton2.setBounds(0, 50, 100, 40);
		boton2.addActionListener((ActionListener) this);
		panel.add(boton2);
		boton3=new JButton("Canal");
		boton3.setBounds(0, 100, 100, 40);
		boton3.addActionListener((ActionListener) this);
		panel.add(boton3);
		
		
	}
	
	
	public void actionPerformed(ActionEvent e) { //llamado de accion
		String s;
		// TODO Auto-generated method stub
		Decodificacion d;
		if (e.getActionCommand().equals("Descomprimir"))
			d = new Decodificacion();
		else
			if(e.getActionCommand().equals("Comprimir")) {
				JFileChooser ventanita=new JFileChooser();
				ventanita.showOpenDialog(ventanita);
				String ruta=ventanita.getSelectedFile().getAbsolutePath();//obtiene la ruta del archivo selecionado
				Imagen imagen= new Imagen(ruta);
				imagen.Dividir();
				imagen.comprimir(Float.parseFloat(campoValor.getText()));
				
			}
			else
				if(e.getActionCommand().equals("Canal")) {
					Canal c = new Canal();
        			c.calcularRuido();
				}
	}
	public static void main(String[] args) {
		Ventana v1=new Ventana();
		v1.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

}
