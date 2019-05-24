
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Codificacion {
	
	private Queue< Nodo > arbolHuf;
	private CabeceraHuf CH;
	private CabeceraRLC CR;
	private BufferedImage img;
	private int ancho,alto;
	private ArrayList<DuplaSerial> simProb=new ArrayList<DuplaSerial>() ;
	private Hashtable<Integer,ArrayList<Character>> codHuff;
	
	/*private File binario;
	private FileWriter escribirBinario;*/
	
	private static String outPutFilePath="outPut.bin";
	byte[] arregloByte;
	private FileOutputStream fos;
	
	public Codificacion(BufferedImage img, int ancho, int alto) {
		this.arbolHuf = new PriorityQueue< Nodo >();
		this.img = img;
		this.ancho = ancho;
		this.alto = alto;
		this.CH=null;
		this.CR=null;
		this.codHuff=new Hashtable<Integer,ArrayList<Character>>(256);
		
	}



	public void inicArbolHuffman(float[] simbProb)
	{
		
		//crea cola de prioridad con los nodos que van a formar el arbol de huffman
		for(int i=0; i<simbProb.length;i++) {
			if(simbProb[i]!=0) {//solo las intensidades que aparecen en la imagen
				DuplaSerial dS=new DuplaSerial(i,simbProb[i]);
				Nodo sP=new Nodo(i,simbProb[i]);
				this.arbolHuf.add(sP);
				this.simProb.add(dS);
			}
		}
		CH=new CabeceraHuf(this.ancho, this.alto, this.simProb);
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
	
	public void codifHuffman(float[] simbProb)
	{
		List<Byte>result=new ArrayList<Byte>();
		ArrayList<Character> acumCod=new ArrayList<Character>();
		/*List<Byte>resultaux=new ArrayList<Byte>();
		List<Byte>resultverdadero=new ArrayList<Byte>();*/
		
		this.inicArbolHuffman(simbProb);
		this.generarArbol();//hasta aca venimos piola!!!
		int rgb=0;
		Color color;
		int r=0;
		byte buffer=0;
		int bufferPos=0;
		int bufferLength=8;
		int t=-1;
		this.generarCodigo(acumCod,this.arbolHuf.element(),t);
		ArrayList<Character> acumCodif=new ArrayList<Character>();
		for(int i=0;i<this.alto;i++) {
			for(int j=0;j<this.ancho;j++)
			{
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();
				acumCodif=this.codHuff.get(r);
				
				//System.out.println(r);
				codificarSecuencia(acumCodif,buffer,bufferPos,result,bufferLength);
				
				}
			}
		if(bufferPos<bufferLength && bufferPos!=0) {
			buffer=(byte)(buffer<<(bufferLength-bufferPos));
			result.add(buffer);
		}
		this.arregloByte= ConvertByteListToPrimitives(result);
		this.generarArchivo();
		
	}
	

	
	private void codificarSecuencia(ArrayList<Character> acumCodif, byte buffer, int bufferPos, List<Byte> result,int bufferLength) {
		// TODO Auto-generated method stub
		int i = 0;
		
		while (i < acumCodif.size() ) {
			// La operación de corrimiento pone un '0'
			buffer = (byte) (buffer << 1);
			bufferPos++;
			if (acumCodif.get(i) == '1') {
				buffer = (byte) (buffer | 1);
			}

			if (bufferPos == bufferLength) {
				result.add(buffer);
				buffer = 0;
				bufferPos = 0;
				bufferLength=8;
			}

			i++;
		}
		if(bufferPos<bufferLength && bufferPos!=0) {
			buffer=(byte)(buffer<<(bufferLength-bufferPos));
			bufferLength=bufferPos;
			bufferPos=0;
		}
	}



	private void generarArchivo() {
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(bs);
			os.writeObject(this.CH);
			os.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte[] bytesCH=bs.toByteArray();
		try {
			fos =new FileOutputStream(Codificacion.outPutFilePath);
			fos.write(bytesCH);
			fos.write(this.arregloByte);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



	private byte[] ConvertByteListToPrimitives(List<Byte> result) {
		byte[] arrOut=new byte[result.size()];
		for(int i=0;i<arrOut.length;i++)
			arrOut[i]=result.get(i);
		return arrOut;
	}



	private void generarCodigo(ArrayList<Character> acumCod,Nodo raiz,int i) 
	{
			System.out.println(raiz.getS() + "tam: " +acumCod.size());
			if((raiz.getDer()!=null) || (raiz.getIzq()!=null))//posee al menos un hijo 
			{	
				
				i++;
				if(raiz.getIzq()!=null) {//hijo izquierdo
					acumCod.add(i,'0');
					generarCodigo(acumCod,raiz.getIzq(),i);
					
					
				}
				if(raiz.getDer()!=null )//hijo derecho
				{
					if(i!=0)
						i--;
					acumCod.remove(i);
					//i++;
					acumCod.add(i, '1');
					generarCodigo(acumCod,raiz.getDer(),i);
					acumCod.remove(i);
				}
			}
			else //esHoja
				{
					if(raiz.getDer()==null && raiz.getIzq()==null)
					{
						//chocamos los cinco
						//acumCod.remove(i);
						//i--;
						this.codHuff.put(raiz.getS(),acumCod);
						
					}
					
			
				}
			}
	}
	


