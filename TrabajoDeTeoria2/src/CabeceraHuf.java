import java.awt.image.ColorModel;
import java.io.Serializable;
import java.util.ArrayList;

public class CabeceraHuf implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ancho,alto,inicancho,inicalto;
	public ArrayList<DuplaSerial> simProb=new ArrayList<DuplaSerial>() ;
	public int c;
	
	public CabeceraHuf(int inicancho,int inicalto,int ancho,int alto, ArrayList<DuplaSerial> simProb, int c) {
		super();
		this.ancho = ancho;
		this.alto = alto;
		this.inicancho = inicancho;
		this.inicalto = inicalto;
		this.simProb = simProb;
		this.c=c;
	}
	
	public int getAncho() {
		return ancho;
	}

	public void setAncho(int ancho) {
		this.ancho = ancho;
	}

	public int getAlto() {
		return alto;
	}

	public void setAlto(int alto) {
		this.alto = alto;
	}

	public ArrayList<DuplaSerial> getSimProb() {
		return simProb;
	}

	public void setSimProb(ArrayList<DuplaSerial> simProb) {
		this.simProb = simProb;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public int getInicancho() {
		return inicancho;
	}

	public void setInicancho(int inicancho) {
		this.inicancho = inicancho;
	}

	public int getInicalto() {
		return inicalto;
	}

	public void setInicalto(int inicalto) {
		this.inicalto = inicalto;
	}

	
	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	
	

	
	
	
}
