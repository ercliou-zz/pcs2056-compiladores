package compiler.commons;

public class NumberConverter {
	public static String convert(int number){
		if(number < 0 || number > 0xFFF)
			throw new RuntimeException("Number out of bounds.");
		String hex = Integer.toHexString(number);
		while(hex.length()<4){
			hex = "0" + hex;
		}
		return "/" + hex;
	}
}
