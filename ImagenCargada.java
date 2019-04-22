
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class ImagenCargada extends Imagen{
	private Vector<Imagen> divisiones;
	
	public ImagenCargada(BufferedImage img,int altosup,int anchosup,int altoinf, int anchoinf) {
		super(img,altosup,anchosup,altoinf,anchoinf);
		try {
			this.img = ImageIO.read(new File("C:\\Users\\Carolina\\Documents\\GitHub\\TPETeoInfoP1\\marsSurface.bmp"));
			 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		divisiones=new Vector<Imagen>();
		img.get
	}
	public void Dividir()
	{
		
	}
}
