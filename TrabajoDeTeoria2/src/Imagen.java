import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Imagen {

	private BufferedImage img;
	private ArrayList<Bloque> division;
	//PARTE 2
	private float Ht;
	private int[][] mConjEntSal= new int[256][256];
	private float[][] mCondEntSal= new float[256][256];
	private int[] margEnt=new int[256];
	private float entropiaCondSal;

	
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

		
		JFileChooser ventanita=new JFileChooser();
		ventanita.showSaveDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();
		File carpeta=new File(ruta);
		carpeta.mkdir();
		
		File archivo=new File(ruta+"\\tamanio.txt");
		try {
			FileWriter escribir=new FileWriter(archivo,true);
			escribir.write(this.img.getWidth()+"-"+this.img.getHeight()+"-"+this.img.getType());
			escribir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		float h=(float) 3.85;
		for(int i=0;i<this.division.size();i++)
			
			if(this.division.get(i).getEntropiaCM()>h)
				this.division.get(i).comprimirHuffman(ruta);
			else
				this.division.get(i).comprimirRLC(ruta);
		
	}

	
	public void inicMat()//inicializa matrices que necesito
	{
		for(int k=0;k<256;k++) {//inicializo en 0 la matriz conjunta para ambas imagenes
			for(int j=0;j<256;j++) {
				this.mConjEntSal[k][j]= 0;
				//this.mCondEntSal[k][j]= (float)0.0;
						}}
		
		for(int i=0;i<256;i++) {
			this.margEnt[i]=0;
		}
	}
	
	public int intensidad(int colum, int fila)
	{
		int rgbS = this.img.getRGB(colum, fila);
		Color colorS = new Color(rgbS, true);
		int rS = colorS.getRed();
		return rS;
	}
	
	public void cargarMatArr(Imagen imgSalida)
	{
		this.inicMat();
		int rgbE;
		Color colorE;
		int intensidadE;
		
		int intensidadS;
		for(int fila=0;fila<this.img.getHeight();fila++) {//cuento ocurrencias para cada imagen
			for(int colum=0;colum<this.img.getWidth();colum++)
			{
				rgbE = this.img.getRGB(colum, fila);
				colorE = new Color(rgbE, true);
				intensidadE = colorE.getRed();//numero de 0-255
				
				intensidadS = imgSalida.intensidad(colum, fila);
				
				this.mConjEntSal[intensidadS][intensidadE]=this.mConjEntSal[intensidadS][intensidadE] + 1;
				this.margEnt[intensidadE]=this.margEnt[intensidadE] + 1;
			}
		}
		
		
	}
	
	/////RUIDO/////////
	public float entropCondSalida(Imagen imgSalida) 
	{
		cargarMatArr(imgSalida);
		float sumaEntropiaSubJ;
		float suma=(float)0.0;
		float probMargColum;
		for(int columna=0; columna<256; columna++) {
			sumaEntropiaSubJ=(float)0.0;
			for(int fila=0; fila<256; fila++)
			{
				float probCond=0;
				if(this.margEnt[columna]!=0 ) {
					
					probCond =((float)this.mConjEntSal[fila][columna]/(float)this.margEnt[columna]);
					if(probCond!=0)
					sumaEntropiaSubJ=(float)((probCond)*(Math.log10(probCond)/Math.log10(2f))) + sumaEntropiaSubJ;
					
					
				}
				
			}
			
			probMargColum=(float)this.margEnt[columna]/((float)(this.img.getHeight())*(float)(this.img.getWidth()));
				suma=(probMargColum*sumaEntropiaSubJ)+suma;
				
			
		}
		return -suma;
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