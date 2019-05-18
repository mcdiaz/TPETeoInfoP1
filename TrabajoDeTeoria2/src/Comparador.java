import java.util.Comparator;

public  class Comparador implements Comparator<Bloque>{
	public int compare(Bloque o1,Bloque o2) {
		// TODO Auto-generated method stub
		if(o1.getEntropiaCM()-o2.getEntropiaCM()>0)
			return 1;
		else 
			if(o1.getEntropiaCM()-o2.getEntropiaCM()<0)
				return -1;
			else
				return 0;
}
}
