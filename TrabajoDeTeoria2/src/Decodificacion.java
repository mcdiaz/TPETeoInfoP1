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
	private int ancho,alto;
	
	
	
	private int[][] matriz;
	
	public Decodificacion() {
		this.arbolHuf = new PriorityQueue< Nodo >();
		matriz=new int[500][500];
		JFileChooser ventanita=new JFileChooser();
		ventanita.showOpenDialog(ventanita);
		File ruta=ventanita.getSelectedFile();
		
			try {
				byte[] inPut= Files.readAllBytes(ruta.toPath());
				ByteArrayInputStream bs= new ByteArrayInputStream(inPut);
				ObjectInputStream  os=new ObjectInputStream(bs);
				try {
					CH= (CabeceraHuf) os.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("alto : "+ CH.getAlto()+ " ancho: "+ CH.getAncho() + " simprob : " + CH.simProb.get(0).getS()+"\n");
				
				
				//generarArbol();
				
				//System.out.println("raiz"+this.arbolHuf.element().getP()+"\n");

				
				os.close();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
	
	public void generarArbol()
	{
		while(arbolHuf.size()>1)
		{
			Nodo n1=this.arbolHuf.poll();
			Nodo n2=this.arbolHuf.poll();
			Nodo padre=new Nodo(0,n1.getP()+n2.getP());
			padre.setIzq(n1);
			padre.setDer(n2);
			this.arbolHuf.add(padre);
		}
	}
		
	public static void main(String[] args) {
		Decodificacion d = new Decodificacion();

	}

	
	

}
