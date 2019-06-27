import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/*
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;*/

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Bloque {
	
	private  int altosup;
	private int anchosup;
	private int altoinf;
	private int anchoinf;
	private float entropiaSM;
	private float entropiaCM;
	private BufferedImage img;
	private double[] ocurrenciasD;
	private int[] ocurrencias;
	private float[] probabilidades;
	private int[][] mCondicional;
	private int[][] mConjunta;
	private int cantSimbolos;
	
	
	// PARTE 2
	private int numBloque;
	private Codificacion cH;
	private Codificacion cRLC;
	private float[][] mAcumulada;
	private float desvio;
	private float mediaConSimulacion;
	private float[] probAcum;
	private int primerSimb=0;
	

	
	public Bloque(int altoinf,int anchoinf,int anchosup,int altosup,BufferedImage img, int num) {
		this.numBloque=num;
		this.altosup = altosup;
		this.anchosup = anchosup;
		this.altoinf=altoinf;
		this.anchoinf=anchoinf;
		this.img=img;
		this.ocurrenciasD=new double[256];
		this.ocurrencias=new int[256];
		this.probabilidades=new float[256];
		this.cantSimbolos=(Math.abs(this.anchosup-this.anchoinf)+1)*(Math.abs(this.altosup-this.altoinf)+1);
		this.mCondicional=new int[256][256];
		this.mConjunta=new int[256][256];
		this.mAcumulada=new float[256][256];
		InicializoEn0();
		cargar();
		calcularDM();
		
		
	}
	

	public void crearHistograma(String cuadrado) {//crea histograma en formato .png con el nombre que se envie por parametro
		  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		  String numero="Histograma "+cuadrado+".PNG";
		  for(int i=0;i<this.ocurrenciasD.length;i++) {
			  if(this.ocurrenciasD[i]!=(double)0.0) {
				  dataset.setValue(this.ocurrenciasD[i], "ocurrencias", Integer.toString(i));
				  }}
		  JFreeChart chart= ChartFactory.createBarChart("Histograma", "valores de grises","ocurrencias", dataset, PlotOrientation.VERTICAL, true, true, false);
			try {
				ChartUtilities.saveChartAsPNG(new File(numero), chart, 6000, 2000);
			}catch(IOException e) {}
	}
	
	private boolean converge(float anterior,float actual) {//calculo de convergencia
		return ((float)Math.abs(anterior-actual)<0.0000000000001f);
	}
	
	private int generar(int c) {//generador de un valor de intensidad aleatorio
		float x= (float)Math.random();
		int f=0;
		while(f<256)
		{
			if( x < this.mAcumulada[f][c] )
			{
				return f;
			}
			f++;
		}
		return 0;
	}
	
	private void probAcum() 
	{
		this.probAcum = new float[256];
		for(int i=0;i<this.probabilidades.length;i++)
		{
				if(i==255)
					this.probAcum[i]=1f;
				else
					if(i==0)
						this.probAcum[i]=this.probabilidades[i];
					else
						this.probAcum[i]=this.probabilidades[i]+this.probAcum[i-1];
		}
	}
	
	private int generarPrimerSimb() {//generador de un valor de intensidad aleatorio
		float x= (float)Math.random();
		int f=0;
		while(f<256)
		{
			if( x < this.probAcum[f] )
			{
				return f;
			}
			f++;
		}
		return 0;
	}
	
	
	
	public void calcularDM() {//calculo de desvio y media por medio de simulacion montecarlo
		this.probAcum();
		float suma=0f;
		float mediaAct=0f;
		int tiradas=0;
		float mediaAnt=-1f;

		int aleat=generarPrimerSimb();
		this.primerSimb=aleat;
		float sumaDesvio=0f;
		float desvioAnt=-1f;
		float desvioAct=0f;
		while ((!converge(mediaAnt,mediaAct) && !converge(desvioAnt,desvioAct)) || tiradas<1000000) { //chequea que no converja
					 
					aleat=this.generar(aleat);
					
					suma=suma+(float)aleat;
					tiradas++;
					
					
					mediaAnt=mediaAct;
					mediaAct=(float)(Math.rint((suma/tiradas)*10000)/10000);
					
					sumaDesvio=(float)((float)(Math.rint(Math.pow(aleat-mediaAct, 2)*1000)/1000)+sumaDesvio);//calculo de desvio al cuadrado utilizando la clase math
					desvioAnt=desvioAct;
					desvioAct=(float)Math.sqrt(Math.rint(sumaDesvio/tiradas*1000)/1000);//uso de la clase math para sacar la raiz cuadrada
		}
		this.desvio=desvioAct;
		this.mediaConSimulacion=mediaAct;
		
	}
	
	private void InicializoEn0() {//inicializa todo en 0
		for(int i=0;i<this.ocurrencias.length;i++) {
			this.ocurrencias[i]=0;
			this.probabilidades[i]= 0f;
			this.ocurrenciasD[i]=0;
		}
		for(int k=0;k<256;k++) {
			for(int j=0;j<256;j++) {
				this.mAcumulada[k][j]= 0f;
				this.mConjunta[k][j]= 0;
				this.mCondicional[k][j]=0;
						}}
		}

	public int getCantSimbolos() {
		return cantSimbolos;
	}
	

	public int getPrimerSimb()
	{
		return this.primerSimb;
	}
	
	public float getDesvio() {return this.desvio;}
	public float getMedia() {return this.mediaConSimulacion;}
	
	public void cargarMatrizAcumulada() {//cargar matriz acumulada con sus probabilidades 
		float suma=0f;
		for(int f=0;f<256;f++) {		
			for(int c=0;c<256;c++) {
				if(f!=255) {
					if(this.ocurrencias[c]!=0) {
						if(f==0) {
							suma= (float)Math.rint(((this.mCondicional[f][c]/this.ocurrencias[c])*10000)/10000);
						}
						else {
							suma= (float)((Math.rint(((float)this.mCondicional[f][c]/(float)this.ocurrencias[c])*10000)/10000)+this.mAcumulada[f-1][c]);
							
							}
						suma=(float)(Math.rint(suma*10000)/10000);
						if(suma<=(float)0.9) {
							this.mAcumulada[f][c]=suma;
						}
						else this.mAcumulada[f][c]=1f;
						}
					else this.mAcumulada[f][c]=0f;
				}
				else
							this.mAcumulada[f][c]=1f;
			}
		
		}
		
	}
	
	private void cargar() {//cargamos los arreglos y las matrices dependiendo la escala de griss cuantas ocurrencias obtuvo 
		int rgb;
		Color color;
		int r;
		int antR=-1;
		for(int i=this.altoinf;i<=this.altosup;i++) {
			for(int j=this.anchoinf;j<=this.anchosup;j++)
			{
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();//numero de 0-255
				this.ocurrencias[r]=this.ocurrencias[r]+1;//calcula las ocurrencias
				this.ocurrenciasD[r]=this.ocurrenciasD[r]+1;
				if(antR>=0) {
					this.mCondicional[r][antR]=this.mCondicional[r][antR]+1;
					this.mConjunta[r][antR]=this.mConjunta[r][antR]+1;
				}
				antR=r;
			}
		}
		int i=0;
		while(i<this.ocurrencias.length || i<this.probabilidades.length) {
			this.probabilidades[i]=(float)(this.ocurrencias[i]/((float)this.cantSimbolos));
			i++;}
		this.cargarEntropiaSM();
		this.cargarEntropiaCondiconal();
		this.cargarMatrizAcumulada();
		
	}
	
	
	private void cargarEntropiaSM() {//calcula entropia sin memoria
		float suma=0;
		for(int i=0;i<this.probabilidades.length;i++)
			{if(this.probabilidades[i]!=0.0) {
				suma= (float)(suma+(this.probabilidades[i]*(Math.log10(this.probabilidades[i])/Math.log10(2f))));
			}
			}
		this.entropiaSM=-suma;}
	
	public float getEntropiaSM(){
		return this.entropiaSM;
		
	}
	
	public void cargarEntropiaCondiconal()
	{
		float sumaEntropiaSubJ;
		float suma=(float)0.0;
		for(int i=0; i<256; i++) {
			sumaEntropiaSubJ=(float)0.0;
			for(int j=0; j<256; j++)
			{
				if(this.ocurrencias[i]!=0 ) {
					float probCond =((float)this.mCondicional[j][i]/(float)this.ocurrencias[i]);
					if(probCond!=0.0) {
						sumaEntropiaSubJ=(float)((probCond)*(Math.log10(probCond)/Math.log10(2f))) + sumaEntropiaSubJ;
					}
				}
			}
			
			if(this.probabilidades[i]!=0.0) {
				suma=(this.probabilidades[i]*sumaEntropiaSubJ)+suma;
			}
		}
		
		this.entropiaCM=-suma;
	}

	public float getEntropiaCM(){
		return this.entropiaCM;}
	
	public int getAltoInf() {return this.altoinf;}
	public int getAltoSup() {return this.altosup;}
	public int getAnchoInf() {return this.anchoinf;}
	public int getAnchoSup() {return this.anchosup;}

	//PARTE 2
	public void comprimirHuffman(String ruta) {
		cH=new Codificacion(this.img, this.anchoinf, this.altoinf, this.anchosup, this.altosup,"h",this.numBloque,ruta);
		this.cH.codifHuffman(this.probabilidades);
		
	}
	
	public void comprimirRLC(String ruta) {
		cRLC=new Codificacion(this.img, this.anchoinf, this.altoinf, this.anchosup, this.altosup,"r",this.numBloque,ruta);
		this.cRLC.codifRLC();
	}
	
	

}