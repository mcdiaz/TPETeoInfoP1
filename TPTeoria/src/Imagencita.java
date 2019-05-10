
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.PlotOrientation;





public class Imagencita {
	private  int altosup;
	private int anchosup;
	private int altoinf;
	private int anchoinf;
	private float entropiaSM;
	private float entropiaCM;
	private BufferedImage img;
	private double[] ocurrencias;
	private float[] probabilidades;
	private float[][] mCondicional;
	private float[][] mConjunta;
	private float[][] mAcumulada;
	private int cantSimbolos;
	private float desvio;
	private float mediaConSimulacion;
	private float mediaSin;
	private float desvioSin;
	
	
	
	private File fileC;
	private FileWriter escribirC;
	private File fileMedia;
	private FileWriter escribirMedia;
	private File fileMediaCuadrada;
	private FileWriter escribirMediaCuadrada;
	
	
	public Imagencita(int altoinf,int anchoinf,int anchosup,int altosup,BufferedImage img) {
		this.altosup = altosup;
		this.anchosup = anchosup;
		this.altoinf=altoinf;
		this.anchoinf=anchoinf;
		this.img=img;
		this.ocurrencias=new double[256];
		this.probabilidades=new float[256];
		this.cantSimbolos=(Math.abs(this.anchosup-this.anchoinf)+1)*(Math.abs(this.altosup-this.altoinf)+1);
		this.mCondicional=new float[256][256];
		this.mConjunta=new float[256][256];
		this.mAcumulada=new float[256][256];
		fileMedia=new File("Medias.txt");
		fileMediaCuadrada=new File("Medias cuadradas.txt");
		InicializoEn0();
		cargar();
		calcularDM();
	}
	


	private void InicializoEn0() {
		for(int i=0;i<this.ocurrencias.length;i++) {
			this.ocurrencias[i]=0.0;
			this.probabilidades[i]= 0f;
		}
		for(int k=0;k<256;k++) {
			for(int j=0;j<256;j++) {
				this.mAcumulada[k][j]= 0f;
				this.mConjunta[k][j]= 0f;
				this.mCondicional[k][j]=0f;
				//System.out.println("acum"+this.mAcumulada[k][j]);
						}}
		
	}



	public int getCantSimbolos() {
		return cantSimbolos;
	}


	private void cargar() {
		// TODO Auto-generated method stub
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
				if(antR>=0) {
					/*this.mCondicional[antR][r]=this.mCondicional[antR][r]+1f;
					this.mConjunta[antR][r]=this.mConjunta[antR][r]+1f;
					*/
					this.mCondicional[r][antR]=this.mCondicional[r][antR]+1f;
					this.mConjunta[r][antR]=this.mConjunta[r][antR]+1f;
				}
				antR=r;
			}
		}
		int i=0;
		while(i<this.ocurrencias.length || i<this.probabilidades.length) {
			this.probabilidades[i]=(float) (this.ocurrencias[i]/(this.cantSimbolos));
			i++;}
		
		this.cargarEntropiaSM();
		this.cargarEntropiaCM();
		this.cargarMatrizAcumulada();
		
		
	}


	public void cargarMatrizAcumulada() {
		// TODO Auto-generated method stub
		float suma=0f;
		for(int f=0;f<256;f++) {		
			for(int c=0;c<256;c++) {
				if(f!=255) {
					
				if(this.ocurrencias[c]!=0.0 ) {
						if(f==0) {
							suma= (float) (Math.rint(this.mCondicional[f][c]/this.ocurrencias[c]*10000)/10000);
							
						}
						else {
							suma= (float) (Math.rint((this.mCondicional[f][c]/this.ocurrencias[c])*10000)/10000)+this.mAcumulada[f-1][c];
						}
						this.mAcumulada[f][c]=suma;
				}
				else this.mAcumulada[f][c]=0f;}
			else
			 {
					this.mAcumulada[f][c]=1f;
					//System.out.println(this.mAcumulada[f][c]);
				}
			}
		}	
	}
	
	
	public void cargarMatrizCondicional(String nombre) {
		// TODO Auto-generated method stub
		fileC=new File(nombre+".txt");
		 try {
			escribirC=new FileWriter(fileC,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 try {
			escribirC.write("Probabilidades condicionales de las intensidades: \n");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int f=0;f<256;f++) {
			for(int c=0;c<256;c++) {
				if(this.ocurrencias[c]!=0.0 ) {
						try {
							escribirC.write(c+" dado "+f+": "+Math.rint(this.mCondicional[f][c]/this.ocurrencias[c]*10000)/10000+"; \t");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				else this.mCondicional[f][c]=0;
				}
			try {
				escribirC.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
		try {
			escribirC.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	private void cargarEntropiaSM() {
		float suma=0;
		for(int i=0;i<this.probabilidades.length;i++)
			{if(this.probabilidades[i]!=0.0) {
				suma= (float) (suma+(this.probabilidades[i]*(Math.log10(this.probabilidades[i])/Math.log10(2f))));
			}
			}
		this.entropiaSM=-suma;
	
}
	
	
	public float getEntropiaSM(){
		return this.entropiaSM;
		
	}
	
	public void cargarEntropiaCM() {
		float suma=0;
		for(int i=0; i<256; i++) {
			for(int j=0; j<256; j++)
			{if(this.ocurrencias[j]!=0.0) {
				float probConj= (float) ((this.mConjunta[i][j])/(this.cantSimbolos-1));
				if(probConj!=0.0) {
				suma= (float) (suma+(probConj*(Math.log10(probConj)/Math.log10(2f))));}
				}
			}
		}
		this.entropiaCM=-suma;
	}
	
	/*public void cargarEntropiaCondiconal()//Se tiene que seguir probando
	{
		float sumaEntropiaSubJ=0;
		float suma=0;
		for(int i=0; i<256; i++) {
			for(int j=0; j<256; j++)
			{
				if(this.ocurrencias[i]!=0.0 && this.ocurrencias[j]!=0.0 && this.ocurrencias[i]!=0.0) {
					float probCond = (float) (this.mCondicional[j][i]/this.ocurrencias[i]);
				sumaEntropiaSubJ= (float) (sumaEntropiaSubJ+((probCond)*(Math.log10(probCond)/Math.log10(2f))));
				}
			}
			
			if(this.probabilidades[i]!=0.0) {
				suma=(this.probabilidades[i]*sumaEntropiaSubJ)+suma;
			}
		}
		
		this.entropiaCM=-suma;
	}*/

	
	
	public float getEntropiaCM(){
		return this.entropiaCM;
		
	}
	
	public int getAltoInf() {return this.altoinf;}
	public int getAltoSup() {return this.altosup;}
	public int getAnchoInf() {return this.anchoinf;}
	public int getAnchoSup() {return this.anchosup;}
	public float getDesvio() {return this.desvio;}
	public float getMedia() {return this.mediaConSimulacion;}

	public void crearHistograma(String cuadrado) {
		  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		  String numero="Histograma "+cuadrado+".PNG";
		  for(int i=0;i<this.ocurrencias.length;i++) {
			  
			  if(this.ocurrencias[i]!=(double)0.0) {
				  
				  dataset.setValue(this.ocurrencias[i], "ocurrencias", Integer.toString(i));
				  }}
		  JFreeChart chart= ChartFactory.createBarChart("Histograma", "valores de grises","ocurrencias", dataset, PlotOrientation.VERTICAL, true, true, false);
			try {
				ChartUtilities.saveChartAsPNG(new File(numero), chart, 6000, 2000);
			}catch(IOException e) {}
		 
		
	}
	private boolean converge(float anterior,float actual) {
		if(Math.abs(anterior-actual)<0.001f)
			return true;
		return false;
	}
	
	private int generar(int c) {
		float x= (float) Math.random();
		for(int f=0;f<256;f++)	{

			if(this.ocurrencias[f]!=0.0 && x<this.mAcumulada[f][c] )
					return f;
				}
		//System.out.println("hay 0:");
		return 0;
	}
	
	public void calcularDM() {
		/*try {
			this.escribirMedia=new FileWriter(fileMedia,true);
			this.escribirMediaCuadrada=new FileWriter(fileMediaCuadrada,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		float suma=0f;

		float mediaAct=0f;
		float tiradas=0f;
		float mediaAnt=-1;

		int f=0;
		for(int i=0;i<this.ocurrencias.length;i++) {
			if(this.ocurrencias[i]!=0.0) {
				f=i;
				break;
				}
			}
		float sumaDesvio=0f;
		float desvioAnt=-1;
		float desvioAct=0f;
		while ((!converge(mediaAnt,mediaAct) && !converge(desvioAnt,desvioAct)) || tiradas<100000) {
					 f=this.generar(f);
					// System.out.println(f+"\t");
					suma=suma+f;
					tiradas++;
					
				
					mediaAnt=mediaAct;
					mediaAct=(float)(suma/tiradas);
					
					sumaDesvio=(float) Math.pow(f-mediaAct, 2)+sumaDesvio;
					desvioAnt=desvioAct;
					desvioAct=(float) Math.sqrt(sumaDesvio/tiradas);
					
			/*try {
				escribirMedia.write(mediaAct+"\n");
				escribirMediaCuadrada.write(mediaCuadradaAct+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		this.desvio=desvioAct;
		this.mediaConSimulacion=mediaAct;
		/*try {
			escribirMedia.close();
			escribirMediaCuadrada.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	public float mediaSinSimu() {
		double suma=0;
		for(int i=0;i<this.ocurrencias.length;i++)
			if(this.ocurrencias[i]!=0.0)	
				suma=suma+(this.ocurrencias[i]*i);
		return (float) (suma/this.cantSimbolos);
	}
	
	public float desvioSinSimu() {
		double suma=0;
		for(int i=0;i<this.ocurrencias.length;i++)
			if(this.ocurrencias[i]!=0.0)	
				suma=suma+(this.ocurrencias[i]*Math.pow(i, 2));
		return (float) Math.sqrt((suma/this.cantSimbolos)-Math.pow(this.mediaSinSimu(), 2));
	}
	
}
