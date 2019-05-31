import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Vector;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class Decodificacion {

	
	private int TYPE_INT_RGB = 1;
	private Queue< Nodo > arbolHuf;
	private CabeceraHuf CH;
	private CabeceraRLC CR;
	private int inicancho, inicalto,ancho,alto;
	private byte[] cod;//contiene codigo del archivo
	private int[] codR;
	private BufferedImage img;
	private char[] secRestaurada;
	
	private int[][] matriz;
	
	private Vector<File> vectorDeArchivos=new Vector<File>();
	
	public Decodificacion() {
		
		this.arbolHuf = new PriorityQueue< Nodo >();
		matriz=new int[500][500];
		this.img=new BufferedImage(2000,2500,this.TYPE_INT_RGB);
		
		
		//////////////////////////abre consolita para selecionar carpeta////////////
		JFileChooser ventanita=new JFileChooser();
		 ventanita.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		ventanita.showOpenDialog(ventanita);
		File ruta=ventanita.getSelectedFile();
		
		//////////////////////guarda los archivos en un vector /////////////////////////////////
		  for (File ficheroEntrada : ruta.listFiles()) {
			  
		            this.vectorDeArchivos.add(ficheroEntrada);
		        }
		      
		        
		  for(int r=0;r<this.vectorDeArchivos.size();r++) {
			 
			 //System.out.println("agarra archivo:"+this.vectorDeArchivos.get(r).toPath());
			try {
				
				byte[] inPut= Files.readAllBytes(this.vectorDeArchivos.get(r).toPath());
				//System.out.println(this.vectorDeArchivos.get(r).toPath());
				
				//System.out.println(inPut.length);
				
				ByteArrayInputStream bs= new ByteArrayInputStream(inPut);
				//int inic=bs.available();
				//System.out.println(inPut[0]);
				ObjectInputStream  os=new ObjectInputStream(bs);
				//System.out.println(inPut.length);
				//System.out.println(bs.available());
				//System.out.println(bs.read());

				//bs.mark(bs.available());
				String nomArch=this.vectorDeArchivos.get(r).getName();
				System.out.println(nomArch);
				char tipoCod='0';
				for(int i=0;i<nomArch.length();i++) {
					if(nomArch.charAt(i)=='-')
					{
						tipoCod=nomArch.charAt(i+1);
					}
				}
				
				if(tipoCod=='h') {
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
				}
				
				else
					if(tipoCod=='r') {
						try {
							CR= (CabeceraRLC) os.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						codR=new int[bs.available()];//cambiarlo para que lea enteros
						for(int i=0;i<codR.length;i++)
							{codR[i]=(int)bs.read();
							
							}
						//System.out.println("Esto es la constante:"+CR.getC());
						this.generarImagenConRlc();
						//System.out.println("tamanio de codR:"+codR.length);
					}
				//System.out.println("alto : "+ CH.getAlto()+ " ancho: "+ CH.getAncho() + " simprob : " + CH.simProb.get(0).getS()+"\n");
				
				//System.out.println("obtengo el valor de pintar imagen:"+CR.getC());
				//System.out.println("pase:");
				
				
				//generarArbol();//DESCOMENTAR//////////////////////
				
				//System.out.println("raiz"+this.arbolHuf.element().getP()+"\n");

				
				os.close();
				
				try {
					ImageIO.write(this.img,"bmp",new File("foto.bmp"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(codR[0]+" "+codR[1]);
		  }
		}
	
		  
		  
		  
		  
		  
	public void generarImagenConRlc() {
		int i=0;
		int columna=CR.inicancho;
		int fila=CR.inicalto;
		/*System.out.println(CR.inicancho+" "+CR.ancho);
		System.out.println(CR.inicalto+" "+CR.alto);*/
		while(i<this.codR.length) {
			int simbolo=this.codR[i];
			
			i++;
			int cantidad=this.codR[i];
			//System.out.println("Esto es el simbolo:"+ simbolo+" "+"su cantidad es:"+cantidad);
			while(fila<=CR.alto) {
				//System.out.println("Fila: "+ fila);
				while(columna<=CR.ancho && cantidad >0 ) {
					//System.out.println("Columna: "+ columna);
							Color c = new Color(simbolo,simbolo,simbolo);
							//System.out.println("color: "+ c.getRGB());
							this.img.setRGB(fila,columna, c.getRGB());
							cantidad--;
							columna++;
						}
					if(columna>CR.ancho && cantidad>0) {
						fila++;
						columna=CR.inicancho;
					}
					if(cantidad==0)
						break;
				}
			i++;
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
		//int inicMsj=-1;
		//boolean encontro=false;
		int posMsj=0;
		//Nodo raizaux=this.arbolHuf.element();
		System.out.println(this.cod.length*8);
		for(int fila=0;fila<500;fila++) {
			
			for(int colum=0;colum<500;colum++) {
				//if(inicMsj<secRestaurada.length-1) {
					//System.out.println("x: "+img.getMinX());
					//recorrerArbol(this.arbolHuf.element(),inicMsj,i,j,encontro,c);
				Nodo raizaux=this.arbolHuf.element();
				
					while(posMsj<this.secRestaurada.length)
					{
						
						if(raizaux.getDer()!=null || raizaux.getIzq()!=null)
						{
							
							if(raizaux.getIzq()!=null && this.secRestaurada[posMsj]=='0')
								raizaux=raizaux.getIzq();
							else
								if(raizaux.getDer()!=null && this.secRestaurada[posMsj]=='1')
									raizaux=raizaux.getDer();
						}
						else
						{
							s=raizaux.getS();
							c=new Color(s,s,s);
							this.img.setRGB(colum,fila, c.getRGB());
							break;
						}
						posMsj++;
						
					}
					//System.out.println("posMSj: "+posMsj+ "fila: "+fila+"colum: "+colum);
				//}
				
			}
		}
		System.out.println(this.secRestaurada.length);
		try {
			ImageIO.write(this.img,"bmp",new File("foto3.bmp"));
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
			//System.out.println("pos: "+posMsj);
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
		{		
				int s=element.getS();
				c=new Color(s,s,s);
				this.img.setRGB(colum,fila, c.getRGB());
				encontro=true;
				//System.out.println(secRestaurada.length);
				int length=this.secRestaurada.length-posMsj;
				char[] auxsec=new char[length];
				int g=0;
				for(int k=posMsj+1;k<this.secRestaurada.length;k++)
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
		//d.generarImagenConRlc();
	}

	
	

}
