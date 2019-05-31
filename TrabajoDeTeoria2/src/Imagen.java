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
			
			this.division.get(i).comprimirRLC(ruta);
	}
	/*
	 public void setHt(float Ht) {
		 this.Ht=Ht;
	 }*/
	
	public void inicMat()
	{
		for(int k=0;k<256;k++) {//inicializo en 0 la matriz conjunta para ambas imagenes
			for(int j=0;j<256;j++) {
				this.mConjEntSal[k][j]= 0;
				this.mCondEntSal[k][j]= 0;
						}}
		
		for(int i=0;i<margEnt.length;i++) {
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
		int rE;
		
		int rS;
		for(int fila=0;fila<this.img.getHeight();fila++) {//cuento ocurrencias para cada imagen
			for(int colum=0;colum<this.img.getWidth();colum++)
			{
				rgbE = this.img.getRGB(colum, fila);
				colorE = new Color(rgbE, true);
				rE = colorE.getRed();//numero de 0-255
				
				rS = imgSalida.intensidad(colum, fila);
				
				this.mConjEntSal[rS][rE]=this.mConjEntSal[rS][rE]+1;
				
			}
		}
		//rE es X
		//rS es y
		
		for(int fila=0;fila<256;fila++) {//CALCULA MARGINAL DE LOS VALORES DE ENTRADA
			for(int colum=0;colum<256;colum++) {
				
				this.margEnt[fila]=(this.mConjEntSal[colum][fila])+this.margEnt[fila];
				//System.out.println(this.margEnt[colum]);
				}
			}
		
	
		
		for(int fila=0;fila<256;fila++) {//CALCULA CONDICIONAL
			for(int colum=0;colum<256;colum++) {
				if(this.margEnt[fila]!=0)
				{
					float probCond=(float)(this.mConjEntSal[colum][fila]/this.margEnt[fila]);
					this.mCondEntSal[colum][fila]=probCond;
				//System.out.println("conj: "+this.mConjEntSal[fila][colum]+" marg: "+this.margEnt[colum]+" cond : "+this.mConjEntSal[fila][colum]/this.margEnt[colum]);
				}
			}
		}
		
		
		
		float[] arr=new float[256];
		
		for(int colum=0;colum<256;colum++) {
			for(int fila=0;fila<256;fila++) {
				arr[colum]=arr[colum]+mCondEntSal[fila][colum];}
					
			}
			for(int colum=0;colum<256;colum++) {
				
					System.out.println(arr[colum]);
					}
	}
	
	
	public float entropCondSalida(Imagen imgSalida) 
	{
		this.cargarMatArr( imgSalida);
		
		///////hice esto para chequiar que la suma de columna de 1////////////////
		/*float[] arr=new float[256];
		for(int colum=0;colum<256;colum++) {
			for(int fila=0;fila<256;fila++) {
				arr[colum]=arr[colum]+mCondEntSal[colum][fila];}
					
			}
			for(int colum=0;colum<256;colum++) {
				for(int fila=0;fila<256;fila++) {
					System.out.println(arr[colum]);}
		}*/
		
		
		float[] MargConj=new float[256];
		//CALCULA MARGINAL DE LOS VALORES DE ENTRADA
			for(int colum=0;colum<256;colum++) {
				MargConj[colum]=(float)this.margEnt[colum]/(this.img.getHeight()*this.img.getWidth());
				//System.out.println(this.margEnt[colum]);
		
			}
			
			
	
			//////////////////////////////////////////////////////////////
			
			float sumaEntropiaSubJ;
			float suma=(float)0.0;
			for(int colum=0; colum<256; colum++) {
				sumaEntropiaSubJ=(float)0.0;
				for(int fila=0; fila<256; fila++)
				{
					if(this.mCondEntSal[fila][colum]!=0)
						sumaEntropiaSubJ=(float)((this.mCondEntSal[fila][colum])*( (Math.log10(this.mCondEntSal[fila][colum])) / Math.log10(2f)) ) + sumaEntropiaSubJ;
						//System.out.println(sumaEntropiaSubJ);
					
				}
				
					//System.out.println(sumaEntropiaSubJ);
					suma=(MargConj[colum]*sumaEntropiaSubJ)+suma;
				
			}
			
			return this.entropiaCondSal=-suma;
		
		
		
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