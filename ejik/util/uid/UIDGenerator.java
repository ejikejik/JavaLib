package ejik.util.uid;

import java.util.Random;

public class UIDGenerator {
	
	private static char[] alphabet = {'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m','1','2','3','4','5','6','7','8','9','0'}; 

	public static String getUid(int lenght) {
		StringBuilder sb = new StringBuilder();
		Random rand = new Random();
		do {
			sb.append(alphabet[(int)(alphabet.length*rand.nextDouble())]);
		} while (sb.length() < lenght);
		return sb.toString();
	}
	
	public static String getUid() {
		return getUid(32);
	}
}
