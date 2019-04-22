

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Imagen {
	protected BufferedImage img;
	protected int altosup;
	protected int anchosup;
	protected int altoinf;
	protected int anchoinf;
	
	
	public Imagen(BufferedImage img, int altosup, int anchosup, int altoinf, int anchoinf) {
		this.img = img;
		this.altosup = altosup;
		this.anchosup = anchosup;
		this.altoinf = altoinf;
		this.anchoinf = anchoinf;
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
	
}