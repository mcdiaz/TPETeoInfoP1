import java.util.Comparator;

public  class Comparador implements Comparator<Imagencita>{
	public int compare(Imagencita o1,Imagencita o2) {
		// TODO Auto-generated method stub
		return (int) (o1.getEntropiaCM()-o2.getEntropiaCM());
}
}
