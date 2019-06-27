import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;

public class Canal {

	private Imagen imgEntrada;
	private Imagen imgSalida;

	
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
		  if(this.vectorDeArchivos.get(0).getName() == "marsSurface.bmp")
		  {  this.imgEntrada = new Imagen(this.vectorDeArchivos.get(0).getAbsolutePath());
		
			this.imgSalida = new Imagen(this.vectorDeArchivos.get(1).getAbsolutePath());
		  }
		  else
		  {  this.imgSalida = new Imagen(this.vectorDeArchivos.get(0).getAbsolutePath());
			
			this.imgEntrada = new Imagen(this.vectorDeArchivos.get(1).getAbsolutePath());
		  }
			  
	}
	
	public void calcularRuido()
	{	JFileChooser ventanita=new JFileChooser();
		ventanita.showSaveDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();
		File carpeta=new File(ruta+".txt");
		try {
			FileWriter escribir=new FileWriter(carpeta,true);
			float ruido=this.imgEntrada.ruidoCanal(this.imgSalida);
			float perdida=0;
					//this.imgSalida.ruidoCanal(this.imgEntrada);
			String s="El ruido de las dos imagenes es de: "+ruido+"/n"+ "La perdida de las dos imagenes es de: "+perdida;
			System.out.println(s);
			escribir.write(s);
			escribir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		Canal c = new Canal();
		c.calcularRuido();
	}
	
	

}
