
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

public class Imagencita {
	private  int altosup;
	private int anchosup;
	private int altoinf;
	private int anchoinf;
	private float entropiaSM;
	private float entropiaCM;
	private BufferedImage img;
	private float[] probabilidades;
	
	
	public Imagencita(int altoinf,int anchoinf,int altosup,int anchosup,BufferedImage img) {
		this.altosup = altosup;
		this.anchosup = anchosup;
		this.altoinf=altoinf;
		this.anchoinf=anchoinf;
		this.img=img;
		probabilidades=new float[256];
		CargarArreglo();
	}


	private void CargarArreglo() {
		// TODO Auto-generated method stub
		int rgb;
		Color color;
		int r;
		for(int i=anchoinf;i<anchosup;i++)
			for(int j=altoinf;j<altosup;i++)
			{
				rgb = img.getRGB(i, j);
				color = new Color(rgb, true);
				r = color.getRed();//numero de 0-255
				probabilidades[r]=probabilidades[r]+1;
				
			}
		for(int i=0;i<probabilidades.length;i++) {
			probabilidades[i]=probabilidades[i]/(this.altosup*this.anchosup);
		}
	}


	public void getEscalaDeGrises(BufferedImage img) {
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
	
	public void getEntropiaSM(BufferedImage img){
		
		
	}
	
}
