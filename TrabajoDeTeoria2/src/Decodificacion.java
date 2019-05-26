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
	private byte[] cod;//contiene codigo del archivo
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
				//System.out.println(inPut.length);
				
				ByteArrayInputStream bs= new ByteArrayInputStream(inPut);
				//int inic=bs.available();
				//System.out.println(inPut[0]);
				ObjectInputStream  os=new ObjectInputStream(bs);
				
				
				
				
				//System.out.println(inPut.length);
				//System.out.println(bs.available());
				//System.out.println(bs.read());

				//bs.mark(bs.available());
				

				try {
					CH= (CabeceraHuf) os.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				cod=new byte[bs.available()];
				for(int i=0;i<cod.length;i++)
				{cod[i]=(byte)bs.read();
				System.out.println(cod[i]);
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
		
	private void recorrerArbol(Nodo element, BufferedImage img2, int inicalto2, int inicancho2, int alto2, int ancho2,int i) {
		// TODO Auto-generated method stub
		
	}
	
	/*private static char[] decodeSequence() {//SE USA PARA PASAR EL ARREGLO DE BYTE COD A UN ARREGLO DE CHAR[], ASI LEEMOS BIT A BIT PARA RECORRERLO EN EL ARBOL
		char[] restoredSequence = new char[sequenceLength];//se deb mandar cuantos datos va a haber adentro, no se debe tener asi como una constante adentro
		
		try {
			byte[] inputSequence = Files.readAllBytes(new File(outputfilepath).toPath());//arreglo de byte que ya tiene cada uno			
			int globalIndex = 0;//indice en toda la secuencia			
			byte mask = (byte) (1 << (bufferLength - 1)); // mask: 10000000 - bufferlength siempre va a ser 8
			int bufferPos = 0;//indice dentro del buffer
			int i = 0;//indice en la lista de byte
			while (globalIndex < sequenceLength) //longitud de secuencia puede ser distinta de la secuencia de inputSequence
			{
				byte buffer = inputSequence[i];			
				while (bufferPos < bufferLength) {
					
					if ((buffer & mask) == mask) {
						restoredSequence[globalIndex] = '1';
					} else {
						restoredSequence[globalIndex] = '0';
					}
					
					buffer = (byte) (buffer << 1);//para ver el segundo bit
					bufferPos++;
					globalIndex++;
					
					if (globalIndex == sequenceLength) {//si ya se procesaron todos los datos - tiene que cortar y no seguir
						break;
					}
				}
				i++;
				bufferPos = 0;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return restoredSequence;
	}*/

	public static void main(String[] args) {
		Decodificacion d = new Decodificacion();

	}

	
	

}
