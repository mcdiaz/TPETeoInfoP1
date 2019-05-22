
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
	
	public Nodo()
	{
		
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
		
		/*if(this.izq!=null) {
			Nodo aux=new Nodo(this.izq.getS(),this.izq.getP());
			if(this.izq.getDer()!=null) 
				aux.setDer(this.izq.getDer());
			
			if(this.izq.getIzq()!=null) 
				aux.setIzq(this.izq.getIzq());
			
			return aux;
		}
		else 
			return null;*/
		return this.izq;
		
	}


	public void setIzq(Nodo izq) {
		
		if(izq!=null) {
			Nodo aux=new Nodo(izq.getS(),izq.getP());
			if(izq.getDer()!=null) 
				aux.setDer(izq.getDer());
			
			if(izq.getIzq()!=null) 
				aux.setIzq(izq.getIzq());
			
			this.izq = aux;}
		//this.izq=izq;

			
		
		
	}


	public Nodo getDer() {
		
		if(this.der!=null) {
			Nodo aux=new Nodo(this.der.getS(),this.der.getP());
			if(this.der.getDer()!=null) 
				aux.setDer(this.der.getDer());
			
			if(this.der.getIzq()!=null) 
				aux.setIzq(this.der.getIzq());
			
			return aux;

		}
		else
			return null;
		//return this.der;
	}


	public void setDer(Nodo der) {
		/*if(der!=null) {
			Nodo aux=new Nodo(der.getS(),der.getP());
			if(der.getDer()!=null) 
				aux.setDer(der.getDer());
			
			if(der.getIzq()!=null) 
				aux.setIzq(der.getIzq());
			
			this.der = aux;
		}*/
		this.der=der;
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
