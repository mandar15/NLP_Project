package nlp.idiosyncrasies;

import java.util.regex.Pattern;

public class test {

	public static void main(String args[]){
		String token = "\\";
		System.out.println(token);
		//System.out.println(Pattern.matches("(?a-![@',&])\\p{Punct}", token));
		System.out.println(Pattern.matches("[a-zA-Z]*\\p{Punct}+[a-zA-Z]*", token));
	}
}
