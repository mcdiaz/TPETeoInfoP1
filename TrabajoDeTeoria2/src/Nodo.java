
public class Nodo implements Comparable<Nodo>{

	private int s;
	private float p;
	private Nodo izq;
	private Nodo der;
	
	public Nodo(int s, float p)
	{
		this.s=s;
		this.p=p;
		this.izq=null;
		this.der=null;
	}
	
	
	public int getS() {
		return s;
	}


	public void setS(int s) {
		this.s = s;
	}


	public float getP() {
		return p;
	}


	public void setP(float p) {
		this.p = p;
	}


	public Nodo getIzq() {
		return izq;
	}


	public void setIzq(Nodo izq) {
		this.izq = izq;
	}


	public Nodo getDer() {
		return der;
	}


	public void setDer(Nodo der) {
		this.der = der;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	@Override
	public int compareTo(Nodo n1) {
		  if(this.getP() > n1.getP()) {
	            return 1;
	        } else if (this.getP() < n1.getP()) {
	            return -1;
	        } else {
	            return 0;
	        }
	}

}
