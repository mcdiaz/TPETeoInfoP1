import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
	//private float[][] mAcumulada;
	private int cantSimbolos;
	
	
	// PARTE 2
	private int numBloque;
	private Codificacion cH;
	private Codificacion cRLC;
	

	
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
		InicializoEn0();
		cargar();
		
		
	}
	
	private void InicializoEn0() {//inicializa todo en 0
		for(int i=0;i<this.ocurrencias.length;i++) {
			this.ocurrencias[i]=0;
			this.probabilidades[i]= 0f;
			this.ocurrenciasD[i]=0;
		}
		for(int k=0;k<256;k++) {
			for(int j=0;j<256;j++) {
				this.mConjunta[k][j]= 0;
				this.mCondicional[k][j]=0;
						}}
		}

	public int getCantSimbolos() {
		return cantSimbolos;
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
	
	public void cargarEntropiaCondiconal()//Se tiene que seguir probando
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
	
	
	
/////////////////////////////////////////////// ruido ///////////////////////////////////////////////////////	
	
	public void entropCondSalida(BufferedImage imgSalida) 
	{
		int[][] mConjEntSal= new int[256][256];
		float[][] mCondEntSal= new float[256][256];
		
		float[] margEnt=new float[256];
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
	

}