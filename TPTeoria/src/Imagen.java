import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;




public class Imagen {
	private BufferedImage img;
	private ArrayList<Imagencita> division;
	private File file;
	private FileWriter escribir;
	
	public Imagen(String ruta) {
		
		try {
			this.img = ImageIO.read(new File(ruta));
			 file=new File("file.txt");//preguntar que lo ponga en el proyecto?
			 escribir=new FileWriter(file,true);
			 
		} catch (IOException e) {
			System.out.println(e.getMessage());
	}
		this.division=new ArrayList<Imagencita>();
	}
	
	
	
	
	
	public void Dividir()
	{ 	
		int i=0;
		int columSup=0;
		int filaSup=0;
		int alto=img.getHeight();
			int ancho=img.getWidth();
		while(i<alto) {
			int j=0;
			if(Math.abs(alto-i)>=500)
				filaSup=i+499;//ancho de 500
			else
				filaSup=alto;
			while(j<ancho) {
				if(Math.abs(ancho-j)>=500)
					columSup=j+499;//ancho de 500
				else {
					columSup=ancho;//ancho menor a 500 --> el limite superior de ancho de imagencita = limite superior del ancho original
				}
				Imagencita bloque=new Imagencita(i,j,columSup, filaSup,this.img);
				this.division.add(bloque);
				j=columSup+1;
			}
			i=filaSup+1;
		}
		Collections.sort(this.division, new Comparador());
	}
	
	public void calcularEntropias() {
		try {
		for(int i=0; i<this.division.size();i++)
			escribir.write(
					"entropias de Bloque desde "+ this.division.get(i).getAltoInf()+
					" a "+ this.division.get(i).getAltoSup()+
					" y "+this.division.get(i).getAnchoInf() +
					" a "+ this.division.get(i).getAnchoSup()+
					": \n \t Entropia sin memoria: "+Float.toString((float) this.division.get(i).getEntropiaSM())+
					"\n \t Entropia con memoria: "+Float.toString((float) this.division.get(i).getEntropiaCM())+
					"\n"
					);
		escribir.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public void getEscalaDeGrises() {
		Color color;
		int r;
		int g;
		int b;
		int rgb;
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
			 rgb = img.getRGB(x, y);
			 color = new Color(rgb, true);
			 r = color.getRed();
			 g = color.getGreen();
			 b = color.getBlue();
			 System.out.println(r + "," + g + "," + b + "," + img.getHeight() + "," + img.getWidth());
			 }
			
		}
	}
	public void generarHistograma() {
		this.division.get(this.division.size()-1).crearHistograma();
		//this.division.get(0).crearHistograma();
	}
	
	
	public static void main(String[] args)
	{
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();
		Imagen imagen= new Imagen(ruta);
		imagen.Dividir();
		imagen.calcularEntropias();
		imagen.generarHistograma();
		
	}
	
	
	
	
}
