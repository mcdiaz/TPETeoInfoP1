import java.io.Serializable;

public class DuplaSerial implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int s;
	public float p;
	public DuplaSerial(int s, float p)
	{
		this.s=s;
		this.p=p;
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
	
}
