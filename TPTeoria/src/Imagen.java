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
	{
		for()
	}
}
