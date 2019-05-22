import java.io.Serializable;
import java.util.ArrayList;

public class CabeceraHuf implements Serializable{
	public int ancho,alto;
	public ArrayList<DuplaSerial> simProb=new ArrayList<DuplaSerial>() ;
	
	public CabeceraHuf(int ancho, int alto, ArrayList<DuplaSerial> simProb)
	{
		this.ancho=ancho;
		this.alto=alto;
		this.simProb=simProb;
	}
	
	
	
}
