import java.io.Serializable;

public class CabeceraRLC implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ancho,alto,inicancho,inicalto,c;
	public CabeceraRLC(int inicancho,int inicalto, int ancho,int alto,int c)
	{
		this.alto=alto;
		this.ancho=ancho;
		this.inicalto=inicalto;
		this.inicancho=inicancho;
		this.c=c;
	}
	
	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
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
	
	
	
}
