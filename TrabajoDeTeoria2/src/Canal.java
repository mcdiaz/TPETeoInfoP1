import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Canal {

	private Imagen imgEntrada;
	private Imagen imgSalida;
	private int[][] mCondicional;
	private int[][] mConjunta;
	
	public Canal(String ruta)
	{
		
			this.imgEntrada = new Imagen(ruta);
		
			this.imgSalida = new Imagen(ruta);
		
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
