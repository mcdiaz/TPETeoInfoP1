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
	private ArrayList<Imagencita> division;
	private File fileA;
	private File fileD;
	private FileWriter escribirA;
	private FileWriter escribirD;
	
	public Imagen(String ruta) {
		
		try {
			this.img = ImageIO.read(new File(ruta));
			 fileA=new File("Inciso A.txt");//preguntar que lo ponga en el proyecto?
			 escribirA=new FileWriter(fileA,true);
			 fileD=new File("Inciso D.txt");//preguntar que lo ponga en el proyecto?
			 escribirD=new FileWriter(fileD,true);
			 
		} catch (IOException e) {
			System.out.println(e.getMessage());
	}
		this.division=new ArrayList<Imagencita>();
	}
	
	
	
	
	
	public void Dividir()
	{ 	
		int i=0;
		int columSup=0;
		int filaSup=0;
		int alto=img.getHeight();
			int ancho=img.getWidth();
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
				Imagencita bloque=new Imagencita(i,j,columSup, filaSup,this.img);
				this.division.add(bloque);
				j=columSup+1;
			}
			i=filaSup+1;
		}
		Collections.sort(this.division, new Comparador());
	}
	
	public void calcularEntropias() {
		try {
		for(int i=0; i<this.division.size();i++)
			escribirA.write(
					"entropias de Bloque desde "+ this.division.get(i).getAltoInf()+
					" a "+ this.division.get(i).getAltoSup()+
					" y "+this.division.get(i).getAnchoInf() +
					" a "+ this.division.get(i).getAnchoSup()+
					": \n \t Entropia sin memoria: "+Float.toString((float) this.division.get(i).getEntropiaSM())+
					"\n \t Entropia con memoria: "+Float.toString((float) this.division.get(i).getEntropiaCM())+
					"\n"
					);
		escribirA.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public void getEscalaDeGrises() {
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
	}
	
	public void generarHistograma() {
		this.division.get(this.division.size()-1).crearHistograma("Mayor entropia");
		this.division.get(0).crearHistograma("Menor Entropia");
		float suma=(float) 0.0;
		for(int i=0;i<this.division.size();i++) {
			suma=suma+this.division.get(i).getEntropiaCM();
		}
		suma=suma/this.division.size()-1;
		int posicionAnt=0;
		float resultAnt=-1;
		float resultAct=0;
		for(int i=1;i<this.division.size()-1;i++) {
			resultAct=Math.abs(this.division.get(i).getEntropiaCM()-suma);
			if(resultAnt>=0 && (resultAct==0 || resultAct>resultAnt))
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
	
	public void generarIncisoC()
	{
		this.division.get(0).cargarMatrizCondicional("IncisoCMenorEntropia");
		this.division.get(this.division.size()-1).cargarMatrizCondicional("IncisoCMayorEntropia");
		
	}
	public void generarIncisoD() {
			try {
				escribirD.write(
						" Bloque desde "+ this.division.get(0).getAltoInf()+
						" a "+ this.division.get(0).getAltoSup()+
						" y "+this.division.get(0).getAnchoInf() +
						" a "+ this.division.get(0).getAnchoSup()+
						": \n \t Desvio: "+Float.toString((float) this.division.get(0).getDesvio())+
						"\n \t Media: "+Float.toString((float) this.division.get(0).getMedia())+
						"\n"+
						"MEDIA SIN SIMU:"+this.division.get(0).mediaSinSimu()+"desvio:"+this.division.get(0).desvioSinSimu()+"\n"+
						" Bloque desde "+ this.division.get(this.division.size()-1).getAltoInf()+
						" a "+ this.division.get(this.division.size()-1).getAltoSup()+
						" y "+this.division.get(this.division.size()-1).getAnchoInf() +
						" a "+ this.division.get(this.division.size()-1).getAnchoSup()+
						": \n \t Desvio: "+Float.toString((float) this.division.get(this.division.size()-1).getDesvio())+
						"\n \t Media: "+Float.toString((float) this.division.get(this.division.size()-1).getMedia())+"\n"+
						"MEDIA SIN SIMU:"+this.division.get(this.division.size()-1).mediaSinSimu()+"desvio:"+this.division.get(this.division.size()-1).desvioSinSimu()
						);
			escribirD.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}	
	}
	
	public static void main(String[] args)
	{
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();
		Imagen imagen= new Imagen(ruta);
		imagen.Dividir();
		//imagen.calcularEntropias();
		//imagen.generarHistograma();
		//imagen.generarIncisoC();
		imagen.generarIncisoD();
		
	}
	
	
	
	
}
