
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.*;
import org.jfree.chart.plot.PlotOrientation;





public class Imagencita {
	private  int altosup;
	private int anchosup;
	private int altoinf;
	private int anchoinf;
	private float entropiaSM;
	private float entropiaCM;
	private BufferedImage img;
	private double[] estacionario;
	private float[] probabilidades;
	private float[][] mCondicional;
	private float[][] mConjunta;
	private int cantSimbolos;
	
	
	public Imagencita(int altoinf,int anchoinf,int anchosup,int altosup,BufferedImage img) {
		this.altosup = altosup;
		this.anchosup = anchosup;
		this.altoinf=altoinf;
		this.anchoinf=anchoinf;
		this.img=img;
		this.estacionario=new double[256];
		this.probabilidades=new float[256];
		this.cantSimbolos=(Math.abs(this.anchosup-this.anchoinf)+1)*(Math.abs(this.altosup-this.altoinf)+1);
		this.mCondicional=new float[256][256];
		this.mConjunta=new float[256][256];
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
				this.estacionario[r]=this.estacionario[r]+1;//calcula las ocurrencias
				if(antR>0) {
					this.mCondicional[antR][r]=this.mCondicional[antR][r]+1f;
					this.mConjunta[antR][r]=this.mConjunta[antR][r]+1;
				}
				antR=r;
			}
		}
		int i=0;
		while(i<this.estacionario.length || i<this.probabilidades.length) {
			this.probabilidades[i]=(float) (this.estacionario[i]/(this.cantSimbolos));
			i++;}
		
		cargarEntropiaSM();
		cargarEntropiaCM();
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
			{if(this.estacionario[j]!=0.0) {
				float probCond= (float) ((this.mCondicional[i][j])/(this.estacionario[j]));
				if(probCond!=0.0) {
				suma= (float) (suma+(probCond*(Math.log10(probCond)/Math.log10(2f))));}
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

	public void crearHistograma() {
		/*HistogramDataset dataset=new HistogramDataset();
		dataset.setType(HistogramType.FREQUENCY);
		dataset.addSeries("Histogram",this.estacionario,10);
		dataset.addChangeListener();
		JFreeChart chart= ChartFactory.createHistogram("Histogram","ocurrencias", "valores de grises", dataset, PlotOrientation.VERTICAL, true, true, false);
		try {
			ChartUtilities.saveChartAsPNG(new File("Histograma.PNG"), chart, 500, 500);
		}catch(IOException e) {}*/
		
		
		
		  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		  for(int i=0;i<this.estacionario.length;i++) {
			  System.out.println(Integer.toHexString(i)+"\n");
			  if(this.estacionario[i]!=(double)0) {
				  String numero="color: ";
				  dataset.setValue(this.estacionario[i], "ocurrencias", Integer.toHexString(i));
				  }}
		  JFreeChart chart= ChartFactory.createBarChart("Histograma", "valores de grises","ocurrencias", dataset, PlotOrientation.VERTICAL, true, true, false);
			try {
				ChartUtilities.saveChartAsPNG(new File("Histograma.PNG"), chart, 1000, 1000);
			}catch(IOException e) {}
		 
		
	}
	
	
	
	
}
