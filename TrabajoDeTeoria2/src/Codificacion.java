
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Codificacion {
	
	private Queue< Nodo > arbolHuf;
	private Cabecera C;
	private BufferedImage img;
	private int ancho,alto;
	
	
	public Codificacion( Cabecera c, BufferedImage img, int ancho, int alto) {
		this.arbolHuf = new PriorityQueue< Nodo >();
		C = c;
		this.img = img;
		this.ancho = ancho;
		this.alto = alto;
	}



	public void inicArbolHuffman(float[] simbProb)
	{
		//crea cola de prioridad con los nodos que van a formar el arbol de huffman
		for(int i=0; i<simbProb.length;i++) {
			if(simbProb[i]!=0) {//solo las intensidades que aparecen en la imagen
				Nodo sP=new Nodo(i,simbProb[i]);
				arbolHuf.add(sP);
			}
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
	
	public void codifHuffman(float[] simbProb)
	{
		List<Byte>result=new ArrayList<Byte>();
		
		this.inicArbolHuffman(simbProb);
		this.generarArbol();
		this.generarCodigo(result);
		
		
	}
	

	
	private void generarCodigo(List<Byte>result,byte ByteBuffered,int bufferedPos,Nodo raiz,int f,int c) {
			if(f<this.alto)
				
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
