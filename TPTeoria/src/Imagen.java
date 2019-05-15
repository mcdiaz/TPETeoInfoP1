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
		this.division=new ArrayList<Imagencita>();
	}
	
	
	
	
	
	public void Dividir()//guarda la posicion de cada imagencita en el vector dividir
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
	
	public void generarIncisoC()//hace llamado al cargar matriz condicional de la entropia mayor y menor
	{
		this.division.get(0).cargarMatrizCondicional("IncisoCMenorEntropia");
		this.division.get(this.division.size()-1).cargarMatrizCondicional("IncisoCMayorEntropia");
		
	}
	public void generarIncisoD() {//genera en un archivo los devios y valor medio de la entropia mayor y menor y los inserta en un archivo
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
	
	public static void main(String[] args)
	{
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		String ruta=ventanita.getSelectedFile().getAbsolutePath();//obtiene la ruta del archivo selecionado
		Imagen imagen= new Imagen(ruta);
		imagen.Dividir();
		imagen.calcularEntropias();
		imagen.generarHistograma();
		imagen.generarIncisoC();
		imagen.generarIncisoD();
		
	}
	
	
}
