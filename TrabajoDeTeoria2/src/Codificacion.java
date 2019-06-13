import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
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
	private Hashtable<Integer,char[]> codHuff;
	private int inicancho,inicalto;
	//private byte buffer=0;
	//private int bufferLength=8;
	
	public String outPutFilePath;
	byte[] arregloByte;
	private FileOutputStream fos;
	List<Integer> codResultRLC= new ArrayList<Integer>();

	List<Byte>codResultHuf=new ArrayList<Byte>();
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
	
	public int getCantBytesRLC()
	{
		return this.codResultRLC.size()*8;
	}
	
	public int getCantBytesHuf()
	{
		return this.codResultHuf.size();
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
		this.codResultRLC=result;
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
		this.CH=new CabeceraHuf(this.inicancho,this.inicalto,this.ancho, this.alto, this.simProb, this.img.TYPE_INT_RGB);
		
	}
	
	public void generarArbol(float[] simbProb)
	{
		this.inicArbolHuffman(simbProb);
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
		
		ArrayList<Character> acumCod=new ArrayList<Character>();
		
		this.generarArbol(simbProb);
		int rgb=0;
		Color color;
		
		List<Byte>result=new ArrayList<Byte>();
		int r=0;
		int bufferPos=0;
		byte buffer=0;
		int bufferLength = 8;
		
		//chequear codigos - cond prefijo
		this.generarCodigo(acumCod,this.arbolHuf.element());
		for(int i=this.inicalto;i<=this.alto;i++) {
			for(int j=this.inicancho;j<=this.ancho;j++)
			{
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();
				//
				//codificarSecuencia(this.codHuff.get(r),bufferPos,result);
				int posCod=0;
				while (posCod < this.codHuff.get(r).length) {
					// La operación de corrimiento pone un '0'
					buffer = (byte) (buffer << 1);//fuerzo que haya 0, aunque son todos 0's en el buffer
					//lo que importa de buffer es el orden de los bits adentro de el, no el numero que forma
					bufferPos++;
					if (this.codHuff.get(r)[posCod] == '1') {
						buffer = (byte) (buffer | 1);//pisa la posicion y fuerza que haya un 1 ( 0 0 0 0 0 0 0 1  )
					}

					if (bufferPos == bufferLength) {//bufferlength es de tamanio 8 por la cantidad de bits en un byte
						result.add(buffer);
						buffer = 0;//reinicia buffer
						bufferPos = 0;//reinicia contador
					}

					posCod++;
				}
				
				
				}
			}
		if ((bufferPos < bufferLength) && (bufferPos != 0)) {//mientras haya simbolos por decodificar
			buffer = (byte) (buffer << (bufferLength - bufferPos));//corro 7 lugares y me quedo con la primera posicion
			result.add(buffer);
		}
		System.out.println("valor result: "+result.get(0));
		ConvertByteListToPrimitives(result);
		this.codResultHuf=result;
		this.generarArchivoHuf();
		
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
			fos =new FileOutputStream(this.rutaDeAcceso+outPutFilePath);
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
						char[] arr=new char[acumCod.size()];
						for(int j=0;j<acumCod.size();j++)
							arr[j]=acumCod.get(j);
						this.codHuff.put(raiz.getS(),arr);
					}
					
			
				}
			}
	}