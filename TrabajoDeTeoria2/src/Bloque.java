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
	private float[][] mAcumulada;
	private int cantSimbolos;
	// PARTE 2
	private Codificacion CH;
	private Decodificacion DH;
	

	
	public Bloque(int altoinf,int anchoinf,int anchosup,int altosup,BufferedImage img) {
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
		codificacion=new codificacion(this.probabilidades);
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
						}}}

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

	public void comprimirHuffman() {
		// TODO Auto-generated method stub
		
	}

	

}