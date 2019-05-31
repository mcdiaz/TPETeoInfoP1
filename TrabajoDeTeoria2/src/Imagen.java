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
	private ArrayList<Bloque> division;
	//private File fileA;
//	private File fileD;
	//private FileWriter escribirA;
	//private FileWriter escribirD;
	private float Ht;
	
	public Imagen(String ruta) {//cargar datos
		
		try {
			this.img = ImageIO.read(new File(ruta));
			 //fileA=new File("Inciso A.txt");
			// escribirA=new FileWriter(fileA,true);
			// fileD=new File("Inciso D.txt");
			// escribirD=new FileWriter(fileD,true);
			 
		} catch (IOException e) {
			System.out.println(e.getMessage());
	}
		this.division=new ArrayList<Bloque>();
	}
	
	public void Dividir()//guarda la posicion de cada imagencita en el vector dividir
	{ 	
		int i=0;
		int columSup=0;
		int filaSup=0;
		int alto=img.getHeight();
			int ancho=img.getWidth();
		int num=0;
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
				Bloque bloque=new Bloque(i,j,columSup, filaSup,this.img,num);
				this.division.add(bloque);
				num++;
				j=columSup+1;
			}
			i=filaSup+1;
		}
		Collections.sort(this.division, new Comparador());
	}
	
	public void comprimir() {
		/*for(int i=0;i<this.division.size();i++)
			if(this.division.get(i).getEntropiaCM()<Ht)
				this.division.get(i).comprimirHuffman();
			else
				this.division.get(i).comprimirLlRC();*/
		this.division.get(10).comprimirHuffman();
		
		/*BufferedImage imgSalida = null;
		try {
			imgSalida = ImageIO.read(new File("marsSurface.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		this.division.get(0).entropCondSalida(imgSalida);*/
	}
	/*
	 public void setHt(float Ht) {
		 this.Ht=Ht;
	 }*/
	
	public static void main(String[] args) {
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();//obtiene la ruta del archivo selecionado
		Imagen imagen= new Imagen(ruta);
		imagen.Dividir();
		//imagen.setHt(Ht);// pedir por consola
		imagen.comprimir();

	}

}
