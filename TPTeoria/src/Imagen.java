import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Imagen {
	private BufferedImage img;
	private Vector<Imagencita> division;
	
	public Imagen(BufferedImage img) {
		this.img=img;
		try {
			this.img = ImageIO.read(new File("C:\\Users\\Carolina\\Documents\\GitHub\\Teoria de la informacion\\marsSurface.bmp"));
			 
		} catch (IOException e) {
			System.out.println(e.getMessage());
	}
		division=new Vector<Imagencita>();
	}
	public void Dividir()
	{ int i=0;
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
				else
					columSup=ancho;//ancho menor a 500 --> el limite superior de ancho de imagencita = limite superior del ancho original
				Imagencita bloque=new Imagencita(i,j,columSup, filaSup,img);
				division.add(bloque);
				j=columSup+1;
			}
			i=filaSup+1;
		}
	}
	

	
	
	
	
	
}
