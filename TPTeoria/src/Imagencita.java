
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
	
	
	
	private File file;
	private FileWriter escribir;
	
	
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
		file=new File("MatrizAcumulada.txt");//preguntar que lo ponga en el proyecto?
		 try {
			escribir=new FileWriter(file,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargar();
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
				if(antR>0) {
					this.mCondicional[antR][r]=this.mCondicional[antR][r]+1f;
					this.mConjunta[antR][r]=this.mConjunta[antR][r]+1;
				}
				antR=r;
			}
		}
		int i=0;
		while(i<this.ocurrencias.length || i<this.probabilidades.length) {
			this.probabilidades[i]=(float) (this.ocurrencias[i]/(this.cantSimbolos));
			i++;}
		
		cargarEntropiaSM();
		cargarEntropiaCM();
		
	}


	public void cargarMatrizAcumulada() {
		// TODO Auto-generated method stub
		float suma=0;
		for(int f=0;f<256;f++) {
			//if(this.ocurrencias[f]!=0 ) {
			for(int c=0;c<256;c++) {
				if(this.ocurrencias[c]!=0 ) {
						if(f==0) {
							suma=(float) (this.mCondicional[f][c]/this.ocurrencias[c]);
						}
						else {
							suma= (float) (this.mCondicional[f][c]/this.ocurrencias[c])+this.mAcumulada[f-1][c];}
						this.mAcumulada[f][c]=suma;
			
			
						try {
							escribir.write(this.mAcumulada[f][c]+"\t");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				else this.mAcumulada[f][c]=0;
				}
			try {
				escribir.write("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		//}
	}
		try {
			escribir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/*public void getEscalaDeGrises(BufferedImage img) {
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
	}*/
	
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
	
	public float getEntropiaCM(){
		return this.entropiaCM;
		
	}
	
	public int getAltoInf() {return this.altoinf;}
	public int getAltoSup() {return this.altosup;}
	public int getAnchoInf() {return this.anchoinf;}
	public int getAnchoSup() {return this.anchosup;}

	public void crearHistograma(String cuadrado) {
		  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		  String numero="Histograma "+cuadrado+".PNG";
		  for(int i=0;i<this.ocurrencias.length;i++) {
			  
			  if(this.ocurrencias[i]!=(double)0) {
				  
				  dataset.setValue(this.ocurrencias[i], "ocurrencias", Integer.toString(i));
				  }}
		  JFreeChart chart= ChartFactory.createBarChart("Histograma", "valores de grises","ocurrencias", dataset, PlotOrientation.VERTICAL, true, true, false);
			try {
				ChartUtilities.saveChartAsPNG(new File(numero), chart, 6000, 2000);
			}catch(IOException e) {}
		 
		
	}
	
	
	
	
}
