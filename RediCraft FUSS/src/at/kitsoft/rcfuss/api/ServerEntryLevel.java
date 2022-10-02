package at.kitsoft.rcfuss.api;

public enum ServerEntryLevel {
	ALL("ALL", 0),
	ALPHA("ALPHA", 3),
	BETA("BETA", 1),
	STAFF("STAFF", 2);

	public final String label;
	public final int code;
	
	private ServerEntryLevel(String label, int code) {
		this.label = label;
		this.code = code;
	}
	
	public String toString() {
		return this.label;
	}
	
	public int toCode() {
		return this.code;
	}
}