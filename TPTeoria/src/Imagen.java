import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.lang.Class;

public class Imagen {
	private BufferedImage img;
	private Vector<Imagencita> division;
	File file;
	FileWriter escribir;
	
	public Imagen() {
		this.img=img;
		
		try {
			
			//Image imgexterna = new ImageIcon(getClass().getResource("marsSurface.bmp")).getImage();
			//this.img = ImageIO.read(imgexterna);
			this.img = ImageIO.read(new File("marsSurface.bmp"));
			//this.img = ImageIO.read(new File(getClass().getResource("marsSurface.bmp").getFile()));
			//this.img = ImageIO.read(new File("C:\\Users\\hp\\Documents\\GitHub\\TPETeoInfoP1\\TPTeoria\\marsSurface.bmp"));
			 file=new File("C:\\Users\\hp\\Documents\\GitHub\\TPETeoInfoP1\\TPTeoria\\file.txt");
			 escribir=new FileWriter(file,true);
			 
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
				else {
					columSup=ancho;//ancho menor a 500 --> el limite superior de ancho de imagencita = limite superior del ancho original
				}
				Imagencita bloque=new Imagencita(i,j,columSup, filaSup,this.img);
				this.division.add(bloque);
				j=columSup+1;
			}
			i=filaSup+1;
		}
	}
	
	public void calcularEntropias() {
		try {
		for(int i=0; i<this.division.size();i++)
			escribir.write("entropiaSM de Bloque desde:"+ this.division.get(i).getAltoInf() +" "+ this.division.get(i).getAltoSup()+" y "+this.division.get(i).getAnchoInf() +" "+ this.division.get(i).getAnchoSup()+": "+Float.toString((float) this.division.get(i).getEntropiaSM())+"\n");
			
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
	
	public static void main(String[] args)
	{
		Imagen imagen= new Imagen();
		imagen.Dividir();
		imagen.calcularEntropias();
		
	}
	
	
	
	
}
