import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Canal {

	private Imagen imgEntrada;
	private Imagen imgSalida;
	private int[][] mCondicional;
	private int[][] mConjunta;
	
	private Vector<File> vectorDeArchivos=new Vector<File>();
	
	public Canal()
	{
		
			JFileChooser ventanita=new JFileChooser();
			ventanita.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			ventanita.showOpenDialog(ventanita);
			File ruta=ventanita.getSelectedFile();
		
		  for (File ficheroEntrada : ruta.listFiles()) {
			  
		            this.vectorDeArchivos.add(ficheroEntrada);
		        }
		this.imgEntrada = new Imagen(this.vectorDeArchivos.get(0).toPath().toString());
		
			this.imgSalida = new Imagen(this.vectorDeArchivos.get(1).toPath().toString());
		
	}
	
	public void 
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Canal c = new Canal();
		
	}

}
