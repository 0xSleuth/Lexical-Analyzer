package net.codejava;

public class Token {
	String cp, vp;
	int line;
	Token(String cp, String vp, int line){
		this.cp = cp;
		this.vp = vp;
		this.line = line;
	}
}
