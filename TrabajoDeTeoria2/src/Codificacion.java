
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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
		List<Byte>resultaux=new ArrayList<Byte>();
		List<Byte>resultverdadero=new ArrayList<Byte>();
		
		this.inicArbolHuffman(simbProb);
		this.generarArbol();
		byte byteBuffered=0;
		int bufferedPos=0;
		
		int rgb = 0;
		Color color;
		int r;
		int bufferedLength=8;
		boolean encontrado;
		for(int i=0; i<this.alto;i++)
			for(int j=0;j<this.ancho;j++) {
				rgb = this.img.getRGB(j, i);
				color = new Color(rgb, true);
				r = color.getRed();
				encontrado=false;
				this.generarCodigo(resultaux,result, byteBuffered, bufferedPos, bufferedLength, this.arbolHuf.element(), r, encontrado);
				//result.addAll(result);////////////////CAMBIE PORQUE ME PARECIA RARO QUE RESULT SE VUELVA A SI MISMO//////////////////////
				
			}
		this.arregloByte= ConvertByteListToPrimitives(result);
		this.generarArchivo();
		
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



	private void generarCodigo(List<Byte> resultaux,List<Byte> result,byte ByteBuffered,int bufferedPos, int bufferedLength, Nodo raiz,int intensidad, boolean encontrado) 
	{
		
		if(!encontrado) {///////////////////ACA ESTABA EL ERROR estaba el !encontro y nunca entraba//////////////////////////
			if((bufferedPos-1)==bufferedLength) {
				result.add(ByteBuffered);
				bufferedPos=0;
				ByteBuffered=0;
			}
			if((raiz.getDer()!=null) || (raiz.getIzq()!=null))//posee al menos un hijo ///Aca saque los distintos de afuera y se los puse adentro
			{	
				
				if(!(resultaux.isEmpty())) {
					for(int i=0;i<resultaux.size();i++)
						{resultaux.remove(i);}
					if(!(result.isEmpty()))
						resultaux.addAll(result);
				}
				ByteBuffered=(byte)(ByteBuffered << 1);
				bufferedPos++;
				if(raiz.getIzq()!=null) {//hijo izquierdo
					
					generarCodigo(result,result,ByteBuffered,bufferedPos,bufferedLength,raiz.getIzq(),intensidad, encontrado);
				}
				if(raiz.getDer()!=null && !encontrado)//hijo derecho
				{
					
					
					ByteBuffered=(byte)(ByteBuffered | 1);
					
					generarCodigo(resultaux,resultaux,ByteBuffered,bufferedPos,bufferedLength,raiz.getDer(),intensidad, encontrado);
					if(encontrado)
					{
						if(!(result.isEmpty())) {
							for(int i=0;i<result.size();i++)
								{result.remove(i);}
							if(!(resultaux.isEmpty()))
								result.addAll(resultaux);
						}
					}
				}
			}
			else //esHoja
				{
					if(!encontrado && raiz.getDer()==null && raiz.getIzq()==null && intensidad==raiz.getS())
					{
						//chocamos los cinco
						bufferedPos--;
						encontrado=true;
						if(bufferedLength!=bufferedPos){
							ByteBuffered= (byte)(ByteBuffered << (bufferedLength - bufferedPos));//lo corre 
							bufferedLength= (bufferedLength - bufferedPos);
							bufferedPos=0;
						}
					}
					
			
				}
		}
	}
	

}
