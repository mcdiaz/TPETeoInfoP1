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

import javax.swing.JFileChooser;

public class Codificacion {
	
	private Queue< Nodo > arbolHuf;
	private CabeceraHuf CH;
	private CabeceraRLC CR;
	private BufferedImage img;
	private int ancho,alto;
	private ArrayList<DuplaSerial> simProb=new ArrayList<DuplaSerial>() ;
	private Hashtable<Integer,char[]> codHuff;
	private int inicancho,inicalto;
	private byte buffer=0;
	private int bufferLength=8;
	
	/*private File binario;
	private FileWriter escribirBinario;*/
	
	public String outPutFilePath;
	byte[] arregloByte;
	private FileOutputStream fos;
	
	private String rutaDeAcceso;
	
	
	public Codificacion(BufferedImage img, int inicancho, int inicalto, int ancho, int alto,String cod,int num,String ruta) {
		this.arbolHuf = new PriorityQueue< Nodo >();
		this.img = img;
		this.ancho = ancho;
		this.alto = alto;
		this.inicancho=inicancho;
		this.inicalto=inicalto;
		this.CH=null;
		this.CR=null;
		this.rutaDeAcceso=ruta;
		String aux= Integer.toString(num)+ "-"+ cod;
		//System.out.println(aux);
		if(cod.equals("h"))
		{
			outPutFilePath="\\"+aux+".bin";
			this.codHuff=new Hashtable<Integer,char[]>(256);
		}
		else
		{
			outPutFilePath="\\"+aux+".txt";
		}
		
	}

	
	
	///////////////////// codificacion RLC ////////////////////////
	public void codifRLC()
	{
		List<Integer> result= new ArrayList<Integer>();
		int rgb=0;
		Color color;
		int r=0;
		int ant=-1;
		int acum=0;
		for(int i=this.inicalto;i<=this.alto;i++) {
			for(int j=this.inicancho;j<=this.ancho;j++)
			{
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();
					if(ant==-1) {
						ant=r;
						acum=1;
					}
					else
						if(r==ant)
							acum++;
						else
							if(r!=ant){
								result.add(ant);
								result.add(acum);
								acum=1;
								ant=r;
					}
				}
			}
		result.add(ant);
		result.add(acum);
		
		this.CR=new CabeceraRLC(this.inicancho,this.inicalto,this.ancho,this.alto,img.TYPE_INT_RGB);
		this.generarArchivoRLC(result);
	}

	
	////////////////////////// gernera archivo para RLC///////////////////////
	public void generarArchivoRLC(List<Integer> result)
	{
		
		ByteArrayOutputStream bs=new ByteArrayOutputStream();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(bs);
			os.writeObject(this.CR);
			os.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		byte[] bytesCH=bs.toByteArray();
		try {
			fos =new FileOutputStream(this.rutaDeAcceso+outPutFilePath);
			fos.write(bytesCH);
			for(int i=0;i<result.size();i++)
				fos.write(result.get(i));
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
 ///////////////////////////////////////// arbol huffman ////////////////////////////////
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
		//System.out.println(img.TYPE_INT_RGB);
		this.CH=new CabeceraHuf(this.inicancho,this.inicalto,this.ancho, this.alto, this.simProb, img.TYPE_INT_RGB);
		
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
		//this.buffer=0;
		int bufferPos=0;
		//int bufferLength=8;
		this.generarCodigo(acumCod,this.arbolHuf.element());
		//System.out.println("prob "+simbProb[232]);
		//ArrayList<Character> acumCodif=new ArrayList<Character>();
		for(int i=this.inicalto;i<=this.alto;i++) {
			for(int j=this.inicancho;j<=this.ancho;j++)
			{
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();
				//System.out.println(r+"/n");
				//System.out.println("colum "+j +" fila "+ i+" tam "+ this.codHuff.get(r).length);
				codificarSecuencia(this.codHuff.get(r),bufferPos,result);
				
				}
			}
		if(bufferPos<this.bufferLength && bufferPos!=0) {
			this.buffer=(byte)(this.buffer<<(this.bufferLength-bufferPos));
			result.add(this.buffer);
		}
		System.out.println("valor result: "+result.get(0));
		ConvertByteListToPrimitives(result);
		this.generarArchivoHuf();
		
	}
	

	
	private void codificarSecuencia(char[] acumCodif, int bufferPos, List<Byte> result) {
		// TODO Auto-generated method stub
		int i = 0;
		//System.out.println(acumCodif.length);
		while (i < acumCodif.length) {
			// La operación de corrimiento pone un '0'
			//buffer = (byte) (buffer << 1);
			bufferPos++;
			if (acumCodif[i] == '1') {
				this.buffer = (byte) (this.buffer << 1);
				this.buffer = (byte) (this.buffer | 1);
			}
			else
				{this.buffer = (byte) (buffer << 1);}

			if (bufferPos == this.bufferLength) {
				result.add(this.buffer);
				this.buffer = 0;
				//buffer = (byte) (buffer << 1);
				bufferPos = 0;
				this.bufferLength=8;
			}
			
			i++;
		}
		if(bufferPos<bufferLength && bufferPos!=0) {
			//System.out.println("bufferLength: "+this.bufferLength +"buferPos: "+ bufferPos);
			int corrimiento=this.bufferLength-bufferPos;
			//System.out.println(corrimiento);
			this.buffer=(byte)(this.buffer<<(corrimiento));
			this.bufferLength=(corrimiento);
			
			bufferPos=0;
		}
	}



	private void generarArchivoHuf() {
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
			fos =new FileOutputStream(outPutFilePath);
			fos.write(bytesCH);
			fos.write(this.arregloByte);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



	public void ConvertByteListToPrimitives(List<Byte> result) {
		this.arregloByte=new byte[result.size()];
		for(int i=0;i<this.arregloByte.length;i++)
			this.arregloByte[i]=result.get(i);
		
	}



	private void generarCodigo(ArrayList<Character> acumCod,Nodo raiz) 
	{
			
			if((raiz.getDer()!=null) || (raiz.getIzq()!=null))//posee al menos un hijo 
			{	
				
				
				if(raiz.getIzq()!=null) {//hijo izquierdo
					acumCod.add('0');
					generarCodigo(acumCod,raiz.getIzq());
					acumCod.remove(acumCod.size()-1);
					
					
				}
				if(raiz.getDer()!=null )//hijo derecho
				{
					
					acumCod.add('1');
					generarCodigo(acumCod,raiz.getDer());
					acumCod.remove(acumCod.size()-1);
				}
			}
			else //esHoja
				{
					if(raiz.getDer()==null && raiz.getIzq()==null)
					{
						//chocamos los cinco
						//acumCod.remove(i);
						//i--;
						char[] arr=new char[acumCod.size()];
						for(int j=0;j<acumCod.size();j++)
							arr[j]=acumCod.get(j);
						//System.out.println("cargo primer simbolo");*/
						this.codHuff.put(raiz.getS(),arr);
					}
					
			
				}
			}
	}