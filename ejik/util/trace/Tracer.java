package ejik.util.trace;

public class Tracer {

	private static Object mutex = new Object();
	public static void tr1ace(String s) {
		synchronized (mutex) {
			System.out.println("___Tracer___: " + s);
		}
	}
	
}
