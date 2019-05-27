import java.awt.Color;
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

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Decodificacion {

	
	private Queue< Nodo > arbolHuf;
	private CabeceraHuf CH;
	private CabeceraRLC CR;
	private int inicancho, inicalto,ancho,alto;
	private byte[] cod;//contiene codigo del archivo
	private BufferedImage img;
	private char[] secRestaurada;
	
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
				//System.out.println(cod[i]);
				}
				//System.out.println("alto : "+ CH.getAlto()+ " ancho: "+ CH.getAncho() + " simprob : " + CH.simProb.get(0).getS()+"\n");
				
				System.out.println(CH.getC());
				
				img= new BufferedImage(500, 500, CH.getC());
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
		this.secRestaurada = decodificarSecuencia();
		Color c = null;
		int s=0;
		int inicMsj=-1;
		boolean encontro=false;
		for(int i=img.getMinY();i<img.getHeight();i++) {
			for(int j=img.getMinX();j<img.getWidth();j++) {
				//if(inicMsj<secRestaurada.length-1) {
					System.out.println("x: "+img.getMinX());
					recorrerArbol(this.arbolHuf.element(),inicMsj,i,j,encontro,c);
					
					
				//}
				
			}
		}
		try {
			ImageIO.write(this.img,"bmp",new File("foto.bmp"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
		
	private void recorrerArbol(Nodo element,int posMsj,int fila, int colum,boolean encontro, Color c) {
		// TODO Auto-generated method stub
		//Color color;
	if(!encontro) {
		if(element.getDer()!=null || element.getIzq()!=null)	
		{
			posMsj++;
			System.out.println("pos: "+posMsj);
			if(posMsj<this.secRestaurada.length)
			{	if(element.getIzq()!=null && this.secRestaurada[posMsj]=='0') {
				
					recorrerArbol(element.getIzq(),posMsj,fila, colum,encontro,c);
				}
			else
				if(element.getDer()!=null && this.secRestaurada[posMsj]=='1') {
					recorrerArbol(element.getDer(),posMsj,fila, colum,encontro,c);
				}
			}
		}
		else
		{		int s=element.getS();
				c=new Color(s,s,s);
				this.img.setRGB(colum,fila, c.getRGB());
				encontro=true;
				int length=this.secRestaurada.length-posMsj;
				char[] auxsec=new char[length];
				int g=0;
				for(int k=posMsj;k<this.secRestaurada.length;k++)
				{
					auxsec[g]=this.secRestaurada[k];
					g++;
				}
				this.secRestaurada=auxsec;
			
		}
		}
}
	
	private char[] decodificarSecuencia() {//SE USA PARA PASAR EL ARREGLO DE BYTE COD A UN ARREGLO DE CHAR[], ASI LEEMOS BIT A BIT PARA RECORRERLO EN EL ARBOL
		
			char[] secRestaurada = new char[this.cod.length*8];//se deb mandar cuantos datos va a haber adentro, no se debe tener asi como una constante adentro
			int indicesec=0;//indice en toda la secuencia	
			int bufferLength=8;
			byte mask = (byte) (1 << (bufferLength - 1)); // mask: 10000000 - bufferlength siempre va a ser 8
			int bufferPos = 0;//indice dentro del buffer
			int i = 0;//indice en la lista de byte
			while (indicesec < secRestaurada.length) //longitud de secuencia puede ser distinta de la secuencia de inputSequence
			{
				byte buffer = this.cod[i];			
				while (bufferPos < bufferLength) {
					
					if ((buffer & mask) == mask) {
						secRestaurada[indicesec] = '1';
					} else {
						secRestaurada[indicesec] = '0';
					}
					
					buffer = (byte) (buffer << 1);//para ver el segundo bit
					bufferPos++;
					indicesec++;
					
					if (indicesec == secRestaurada.length) {//si ya se procesaron todos los datos - tiene que cortar y no seguir
						break;
					}
				}
				i++;
				bufferPos = 0;
			}
			return secRestaurada;
		
	}

	public static void main(String[] args) {
		Decodificacion d = new Decodificacion();
		d.generarBloqueH();
	}

	
	

}
