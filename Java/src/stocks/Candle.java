package stocks;

public class Candle {
	private String timestamp;
	private double values[] = new double[4];
	
	public Candle(String t, double o, double c, double h, double l) {
		timestamp = t;
		values[0] = o; values[1] = c; values[2] = h; values[3] = l;
	}
	
	public double getOpen() { return values[0]; }
	
	public double getClose() { return values[1]; }
	
	public double getHigh() { return values[2]; }
	
	public double getLow() { return values[3]; }
	
	public String getTimestamp() { return timestamp; }
	
	@Override
	public boolean equals(Object o) {
		Candle candle;
		if(o instanceof Candle) {
			candle = (Candle)o;
		}
		else return false;
		return this.timestamp.equals(candle.getTimestamp());
	}
	
}
