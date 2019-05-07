import java.util.Comparator;

public  class Comparador implements Comparator<Imagencita>{
	public int compare(Imagencita o1,Imagencita o2) {
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
