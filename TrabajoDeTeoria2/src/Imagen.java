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
	private File fileD;
	private FileWriter escribirD;
	private File fileA;
	private FileWriter escribirA;
	//PARTE 2
	private float Ht;
	private int[][] mConjEntSal= new int[256][256];
	//private float[][] mCondEntSal= new float[256][256];
	private int[] margEnt=new int[256];
	private int[] margSal=new int[256];


	
	public Imagen(String ruta) {//cargar datos
		
		try {
			
			this.img = ImageIO.read(new File(ruta));
			fileA=new File("Inciso A.txt");
			 escribirA=new FileWriter(fileA,true);
			fileD=new File("Inciso D.txt");
			 escribirD=new FileWriter(fileD,true);
		} catch (IOException e) {
			System.out.println(e.getMessage());
	}
		this.division=new ArrayList<Bloque>();

	}
	
	public int getAltura()
	{
		return this.img.getHeight();
	}
	
	public int getAncho()
	{
		return this.img.getWidth();
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
	
	public void calcularEntropias() { //inserta las entropias de todas las subimagenes recorriendo todo el vector de divisiones
		try {
		for(int i=0; i<this.division.size();i++)
			escribirA.write(
					"Entropias de Bloque desde "+ this.division.get(i).getAltoInf()+
					" a "+ this.division.get(i).getAltoSup()+
					" y "+this.division.get(i).getAnchoInf() +
					" a "+ this.division.get(i).getAnchoSup()+
					": \n  \t Entropia sin memoria: "+Float.toString((float) this.division.get(i).getEntropiaSM())+
					"\n  \t Entropia con memoria: "+Float.toString((float) this.division.get(i).getEntropiaCM())+
					"\n \n"
					);
		escribirA.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public void generarHistograma() { // genera los 3 histogramas pedidos por la catedra
		this.division.get(this.division.size()-1).crearHistograma("Mayor entropia");
		this.division.get(0).crearHistograma("Menor Entropia");
		float suma=(float) 0.0;
		for(int i=0;i<this.division.size();i++) {   //suma de todas las entropias
			suma=suma+this.division.get(i).getEntropiaCM();// recorriendo el vector division
		}
		suma=suma/this.division.size()-1;
		int posicionAnt=0;
		float resultAnt=-1;
		float resultAct=0;
		for(int i=1;i<this.division.size()-1;i++) {
			resultAct=Math.abs(this.division.get(i).getEntropiaCM()-suma);
			if(resultAnt>=0 && (resultAct==0 || resultAct>resultAnt))    //obtenemos la entropia mas cercana
			{
				resultAnt=resultAct;
				posicionAnt=i;
				break;
			}
			resultAnt=resultAct;
			posicionAnt=i;
		}
		this.division.get(posicionAnt).crearHistograma("Entropia promedio");
	}
	
	
	public void generarDesvioYMedia() {//genera en un archivo los devios y valor medio de la entropia mayor y menor y los inserta en un archivo
		try {
			escribirD.write(
					" Bloque desde "+ this.division.get(0).getAltoInf()+
					" a "+ this.division.get(0).getAltoSup()+
					" y "+this.division.get(0).getAnchoInf() +
					" a "+ this.division.get(0).getAnchoSup()+
					": \n \t Desvio: "+Float.toString((float) this.division.get(0).getDesvio())+
					"\n \t Media: "+Float.toString((float) this.division.get(0).getMedia())+
					"\n"+
					
					" Bloque desde "+ this.division.get(this.division.size()-1).getAltoInf()+
					" a "+ this.division.get(this.division.size()-1).getAltoSup()+
					" y "+this.division.get(this.division.size()-1).getAnchoInf() +
					" a "+ this.division.get(this.division.size()-1).getAnchoSup()+
					": \n \t Desvio: "+Float.toString((float) this.division.get(this.division.size()-1).getDesvio())+
					"\n \t Media: "+Float.toString((float) this.division.get(this.division.size()-1).getMedia())+"\n"
					
					);
		escribirD.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
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
		float h=(float) 3.6;
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
			this.margSal[i]=0;
		}
	}
	
	public int intensidad(int colum, int fila)
	{
		int rgbS = this.img.getRGB(colum, fila);
		Color colorS = new Color(rgbS, true);
		int rS = colorS.getRed();
		return rS;
	}
	
	public void cargarDatosRuido(Imagen imgSalida)
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
	public float ruidoCanal(Imagen imgSalida) 
	{
		cargarDatosRuido(imgSalida);
		float sumaEntropiaSubJ;
		float suma=(float)0.0;
		float probMargColum;
		for(int columna=0; columna<256; columna++) {
			sumaEntropiaSubJ=(float)0.0;
			if(this.margEnt[columna]!=0 ) {
				for(int fila=0; fila<256; fila++)
				{
					float probCond=(float)0.0;
				
					
					probCond =((float)this.mConjEntSal[fila][columna]/(float)this.margEnt[columna]);
						if(probCond!=(float)0.0)
							sumaEntropiaSubJ=(float)((probCond)*(Math.log10(probCond)/Math.log10(2f))) + sumaEntropiaSubJ;
				}
				probMargColum=(float)this.margEnt[columna]/((float)(this.img.getHeight())*(float)(this.img.getWidth()));
				suma=(probMargColum*sumaEntropiaSubJ)+suma;
				
			}
			
			
		}
		return -suma;
	}
	
	public void cargarDatosPerdida(Imagen imgSalida)
	{
		this.inicMat();
		int rgbE;
		Color colorE;
		int intensidadE;
		int intensidadS;
		for(int fila=0;fila<imgSalida.getAltura();fila++) {//cuento ocurrencias para cada imagen
			for(int colum=0;colum<imgSalida.getAncho();colum++)
			{
				rgbE = this.img.getRGB(colum, fila);
				colorE = new Color(rgbE, true);
				intensidadE = colorE.getRed();//numero de 0-255
				
				intensidadS = imgSalida.intensidad(colum, fila);
				
				this.mConjEntSal[intensidadS][intensidadE]=this.mConjEntSal[intensidadS][intensidadE] + 1;
				this.margSal[intensidadS]=this.margSal[intensidadS] + 1;
			}
		}
	}
	
	public float perdidaCanal(Imagen imgSalida) 
	{
		cargarDatosPerdida(imgSalida);
		float sumaEntropiaSubJ;
		float suma=(float)0.0;
		float probMargColum;
		for(int fila=0; fila<256; fila++) {
			sumaEntropiaSubJ=(float)0.0;
			if(this.margSal[fila]!=0 ) 
			{
				for(int columna=0; columna<256; columna++)
				{
					float probCond=(float)0.0;
				
					
					probCond =((float)this.mConjEntSal[fila][columna]/(float)this.margSal[fila]);
						if(probCond!=(float)0.0)
							sumaEntropiaSubJ=(float)((probCond)*(Math.log10(probCond)/Math.log10(2f))) + sumaEntropiaSubJ;
				}
				probMargColum=(float)this.margSal[fila]/((float)(imgSalida.getAltura())*(float)(imgSalida.getAncho()));
				suma=(probMargColum*sumaEntropiaSubJ)+suma;
				
			}
			
			
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
	//	imagen.calcularEntropias();
	//	imagen.generarDesvioYMedia();
	//	imagen.generarHistograma();
		
	}
   

}