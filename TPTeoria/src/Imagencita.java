
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
	
	
	public Imagencita(int altoinf,int anchoinf,int anchosup,int altosup,BufferedImage img) {
		this.altosup = altosup;
		this.anchosup = anchosup;
		this.altoinf=altoinf;
		this.anchoinf=anchoinf;
		this.img=img;
		probabilidades=new float[256];
		cargarArreglo();
		cargarEntropiaSM();
	}


	private void cargarArreglo() {
		// TODO Auto-generated method stub
		int rgb;
		Color color;
		int r;
		
		for(int i=this.altoinf;i<=this.altosup;i++) {
			for(int j=this.anchoinf;j<=this.anchosup;j++)
			{
				
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();//numero de 0-255
				this.probabilidades[r]=this.probabilidades[r]+1f;//calcula las ocurrencias
				
			}
		}
		for(int i=0;i<this.probabilidades.length;i++) {
			this.probabilidades[i]=(float) (this.probabilidades[i]/(this.altosup*this.anchosup));
		}
	}


	/*public void getEscalaDeGrises(BufferedImage img) {
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
	}*/
	
	private void cargarEntropiaSM() {
		float suma=0;
		for(int i=0;i<this.probabilidades.length;i++)
			{if(this.probabilidades[i]!=0.0) {
				suma= (float) (suma+(this.probabilidades[i]*(Math.log10(this.probabilidades[i])/Math.log10(2f))));
		
				System.out.println("che esto es los logar: "+(Math.log10(this.probabilidades[i])/Math.log10(2f)));}
			}
		for(int i=0;i<this.probabilidades.length;i++)
			System.out.println(probabilidades[i]);
		this.entropiaSM=-suma;
	}
	
	
	public float getEntropiaSM(){
		return this.entropiaSM;
		
	}
	
	public int getAltoInf() {return this.altoinf;}
	public int getAltoSup() {return this.altosup;}
	public int getAnchoInf() {return this.anchoinf;}
	public int getAnchoSup() {return this.anchosup;}
	
	
}
