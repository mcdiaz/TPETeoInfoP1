import java.io.Serializable;
import java.util.ArrayList;

public class CabeceraHuf implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	public int ancho,alto;
	public ArrayList<DuplaSerial> simProb=new ArrayList<DuplaSerial>() ;
	
	public CabeceraHuf(int ancho, int alto, ArrayList<DuplaSerial> simProb)
	{
		this.ancho=ancho;
		this.alto=alto;
		this.simProb=simProb;
	}
	
	
	
}
