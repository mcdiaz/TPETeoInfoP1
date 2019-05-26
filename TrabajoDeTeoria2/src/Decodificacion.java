import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.PriorityQueue;
import java.util.Queue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;

public class Decodificacion {

	
	private Queue< Nodo > arbolHuf;
	private CabeceraHuf CH;
	private CabeceraRLC CR;
	private int inicancho, inicalto,ancho,alto;
	private byte[] cod;
	private BufferedImage img;
	
	
	private int[][] matriz;
	
	public Decodificacion() {
		this.arbolHuf = new PriorityQueue< Nodo >();
		matriz=new int[500][500];
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		File ruta=ventanita.getSelectedFile();
		
			try {
				byte[] inPut= Files.readAllBytes(ruta.toPath());
				//System.out.println(ruta.toPath());
				System.out.println(ruta.getName());
				System.out.println(inPut.length);
				
				ByteArrayInputStream bs= new ByteArrayInputStream(inPut);
				int inic=bs.available();
				System.out.println(inPut[0]);
				ObjectInputStream  os=new ObjectInputStream(bs);
				int fin=bs.available();

				/*for(int i=0;i<(inPut.length-(fin-inic));i++)
					inPut[i]=inPut[i+(fin-inic)];*/
				
				System.out.println(inPut.length);
				//System.out.println(bs.available());
				//System.out.println(bs.read());

				//bs.mark(bs.available());
				

				try {
					CH= (CabeceraHuf) os.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("alto : "+ CH.getAlto()+ " ancho: "+ CH.getAncho() + " simprob : " + CH.simProb.get(0).getS()+"\n");
				
				
				
				//img= new BufferedImage(2000, 2500, CH.getC());
				generarArbol();
				
				//System.out.println("raiz"+this.arbolHuf.element().getP()+"\n");

				
				os.close();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
	
	public void generarArbol()
	{
		for(int i=0; i<CH.simProb.size();i++) {//inicializamos el arbol
				Nodo sP=new Nodo(CH.simProb.get(i).getS(),CH.simProb.get(i).getP());
				this.arbolHuf.add(sP);
		}
		while(arbolHuf.size()>1)
		{
			Nodo n1=this.arbolHuf.poll();
			Nodo n2=this.arbolHuf.poll();
			Nodo padre=new Nodo(0,n1.getP()+n2.getP());
			padre.setIzq(n1);
			padre.setDer(n2);
			this.arbolHuf.add(padre);
		}
		System.out.println("raiz "+this.arbolHuf.element().getS() + " izq "+ this.arbolHuf.element().getIzq().getS() + " hoja " + this.arbolHuf.element().getIzq().getIzq().getS());
	}
	
	public void generarBloqueH()
	{
		recorrerArbol(this.arbolHuf.element(),this.img, this.inicalto,this.inicancho,this.alto,this.ancho,0);
				
	}
		
	private void recorrerArbol(Nodo element, BufferedImage img2, int inicalto2, int inicancho2, int alto2, int ancho2,
			int i) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		Decodificacion d = new Decodificacion();

	}

	
	

}
