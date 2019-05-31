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
	private ArrayList<Bloque> division;
	//PARTE 2
	private float Ht;
	private int[][] mConjEntSal= new int[256][256];
	private float[][] mCondEntSal= new float[256][256];
	private float[] margEnt=new float[256];
	
	public Imagen(String ruta) {//cargar datos
		
		try {
			this.img = ImageIO.read(new File(ruta));
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
			if(this.division.get(i).getEntropiaCM()<this.Ht)
				this.division.get(i).comprimirHuffman();
			else
				this.division.get(i).comprimirRLC();*/
		//this.division.get(6).comprimirRLC();
		//this.division.get(6).comprimirHuffman();
		/*BufferedImage imgSalida = null;
		try {
			imgSalida = ImageIO.read(new File("marsSurface.bmp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		this.division.get(0).entropCondSalida(imgSalida);*/
		
		
		JFileChooser ventanita=new JFileChooser();
		ventanita.showSaveDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();
		System.out.println(ruta);
		File carpeta=new File(ruta);
		carpeta.mkdir();
		
		
		for(int i=0;i<this.division.size();i++)
			
			this.division.get(i).comprimirHuffman(ruta);
	}
	/*
	 public void setHt(float Ht) {
		 this.Ht=Ht;
	 }*/
	
	public void entropCondSalida(BufferedImage imgSalida) 
	{
		
		//float[] margSal=new float[256];
		
		for(int k=0;k<256;k++) {//inicializo en 0 la matriz conjunta para ambas imagenes
			for(int j=0;j<256;j++) {
				mConjEntSal[k][j]= 0;
				mCondEntSal[k][j]= 0;
						}}
		
		for(int i=0;i<margEnt.length;i++) {
			margEnt[i]=0f;
			//margSal[i]= 0f;
		}
		
		int rgbE;
		Color colorE;
		int rE;
		int rgbS;
		Color colorS;
		int rS;
		for(int i=this.altoinf;i<=this.altosup;i++) {//cuento ocurrencias para cada imagen
			for(int j=this.anchoinf;j<=this.anchosup;j++)
			{
				rgbE = this.img.getRGB(j, i);
				colorE = new Color(rgbE, true);
				rE = colorE.getRed();//numero de 0-255
				rgbS = imgSalida.getRGB(j, i);
				colorS = new Color(rgbS, true);
				rS = colorS.getRed();
				
				mConjEntSal[rS][rE]=mConjEntSal[rS][rE]+1;
				
			}
		}
		//rE es X
		//rS es y
		
		for(int colum=0;colum<256;colum++) {
			for(int fila=0;fila<256;fila++) {
				
				margEnt[colum]=(float)(mConjEntSal[colum][fila]/(500*500))+margEnt[colum];
				}
			}
		
	
		
		for(int colum=0;colum<256;colum++) {
			for(int fila=0;fila<256;fila++) {
				if(margEnt[colum]!=0)
				mCondEntSal[colum][fila]=(float)((mConjEntSal[colum][fila]/(500*500))/margEnt[colum]);
			}
		}
		///////hice esto para chequiar que la suma de columna de 1////////////////
		float[] arr=new float[256];
		for(int colum=0;colum<256;colum++) {
			for(int fila=0;fila<256;fila++) {
				arr[colum]=arr[colum]+mCondEntSal[colum][fila];}
					
			}
			for(int colum=0;colum<256;colum++) {
				for(int fila=0;fila<256;fila++) {
					System.out.println(arr[colum]);}
		}
		
		
		
	}
	
	
	public static void main(String[] args) {
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();//obtiene la ruta del archivo selecionado
		Imagen imagen= new Imagen(ruta);
		imagen.Dividir();
		imagen.comprimir();

	}

}