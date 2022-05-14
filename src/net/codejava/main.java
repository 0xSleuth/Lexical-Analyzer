package net.codejava;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class main {
	
	final static String[] accessModifiers = {"public", "private", "protected"};
	final static String[] arithmeticOperators = {"+", "-", "*", "/", "%"};
	final static String[] dataTypes = {"int", "float", "double", "char", "String", "boolean"};
	final static String[] relationalOperators = {"=", "!", ">", "<"};
	final static String[] punctuators = {"{", ";", "}", "(", ")", "[", "]", ","};
	final static String[] uniqueKeywords = {"Class", "extends", "static", "super", "new", "try", "catch",
			"final", "finally", "throw", "abstract", "void", "if", "else", "for"
			, "return", "break", "continue"};
	
	public static void invalidToken(int line, FileWriter file, String tokenValue) {
		try {
			file.write("Invalid Token at line " + String.valueOf(line) + ", Value: " + tokenValue + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void specifyToken(String s, FileWriter file, int line) {
		if(contains(s, accessModifiers)) {
			Token token = new Token("Access Modifiers", s, line);
			createToken(token, file);
		}else if(contains(s, dataTypes)) {
			Token token = new Token("DT", s, line);
			createToken(token, file);
		}else if(contains(s, uniqueKeywords)) {
			Token token = new Token(s, s, line);
			createToken(token, file);
		}else if(isInteger(s)) {
			Token token = new Token("Integer Constant", s, line);
			createToken(token, file);
		}else if(isFloat(s)) {
			Token token = new Token("Float Constant", s, line);
			createToken(token, file);
		}else if(isIdentifier(s)) {
			Token token = new Token("ID", s, line);
			createToken(token, file);
		}else {
			invalidToken(line, file, s);
		}
	}
	
	public static List<String> convertToList(String[] arr){
		List<String> list = Arrays.asList(arr);
		return list;
	}
	
	public static boolean contains(String s, String[] arr) {
		List<String> list = convertToList(arr);
		if(list.contains(s)) {
			return true;
		}
		return false;
	}
	
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static void createToken(Token token, FileWriter file) {
		try {
			file.write("[\"" + token.cp + "\" - \"" + token.vp + "\" - \"" + String.valueOf(token.line) +"\"]\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isFloat(String s) {
		try {
			Float.parseFloat(s);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
	
	public static String convertToString(Vector<Character> vector) {
		String s = "";
		for(int i = 0; i < vector.size(); i++) {
			s = s + String.valueOf(vector.get(i));
		}
		return s;
	}
	
	public static boolean isIdentifier(String s) {
		String regex = "^([a-zA-Z_][a-zA-Z\\d_]*)"; 
		  
        Pattern p = Pattern.compile(regex); 
  
      
        if (s == null) { 
            return false; 
        } 
  
        Matcher m = p.matcher(s); 
  
        return m.matches();
	}

	public static void main(String[] args) throws IOException {
		
		int line = 1;
		int flag1 = 0;
		
		Vector<Character> vector = new Vector<Character>();

		File file = new File("input.txt");
		
		FileWriter filewriter = new FileWriter("output.txt");
		
		Token token;
        try (FileReader fr = new FileReader(file)) {
            int a = fr.read();
            char c;
            while (a != -1) {
                c = (char) a;
                if(c == '\r') {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                		vector.clear();
                	}
                	line++;
                }else if(c == '\n') {
                	
                }else if(c == '\"') {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	int flag2 = 0;
                	vector.clear();
                	while (true) {
                		a = fr.read();
                		c = (char) a;
                		if(c == '\"') {
                			if(flag2 == 0) {
                				token = new Token("String Constant", convertToString(vector), line);
                				createToken(token, filewriter);
                				vector.clear();
                				break;
                			}else{
                				invalidToken(line, filewriter, convertToString(vector));
                				vector.clear();
                    			break;
                			}
                		}else if(a == -1) {
                			invalidToken(line, filewriter, convertToString(vector));
                			vector.clear();
                			return;
                		}else if(c == '\r') {
                			invalidToken(line, filewriter, convertToString(vector));
                			vector.clear();
                			line++;
                			break;
                		}else if(c == '\\') {
                			vector.add(c);
                			c = (char) fr.read();
                			vector.add(c);
                			if(!(c == '\"' || c == 'n' || c == 't' || c == '\'' || c == '\\')) {
                				flag2 = 1;
                			}
                		}else {
                			vector.add(c);
                		}
                	}
                	
                }else if(c == '\'') {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	int flag = 0;
                	vector.clear();
                	c = (char) fr.read();
                	if(c == '\r') {
                		invalidToken(line, filewriter, convertToString(vector));
                    	line++;
                    }else {
                    	vector.add(c);
                    	if(c == '\\') {
                    		c = (char) fr.read();
                    		if(c == '\r') {
                    			invalidToken(line, filewriter, convertToString(vector));
                            	line++;
                            }else {
                            	vector.add(c);
                        		if(!(c == '\"' || c == 'n' || c == 't' || c == '\'' || c == '\\')) {
                        			invalidToken(line, filewriter, convertToString(vector));
                        			flag = 1;
                        		}
                            }
                    		
                    	}
                    	if(flag == 0) {
                    		c = (char) fr.read();
                    		if(c == '\r') {
                    			invalidToken(line, filewriter, convertToString(vector));
                            	line++;
                            }else if(c == '\'') {
                    			token = new Token("Char_Const", convertToString(vector), line);
                    			createToken(token, filewriter);
                    		}else {
                    			vector.add(c);
                    			invalidToken(line, filewriter, convertToString(vector));
                    		}
                    	}
                    }
                	vector.clear();
                }else if(contains(String.valueOf(c), arithmeticOperators)) {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	vector.clear();
                	vector.add(c);
                	c = (char) fr.read();
                	if(c == '\r') {
                		if(vector.lastElement() == '+' || vector.lastElement() == '-') {
                			token = new Token("Arithmetic Operator POS/NEG", convertToString(vector), line);
                			createToken(token, filewriter);
                		}else {
                			token = new Token("Arithmetic Operator", convertToString(vector), line);
                			createToken(token, filewriter);
                		}
            			line++;
                    }else if(c == '=') {
                    	vector.add(c);
                    	token = new Token("Assignment Operator", convertToString(vector), line);
                    	createToken(token, filewriter);
                    }else if(vector.lastElement() == '+' && c == '+') {
                    	vector.add(c);
                    	token = new Token("INC/DEC", convertToString(vector), line);
                    	createToken(token, filewriter);
                    }else if(vector.lastElement() == '-' && c == '-') {
                    	vector.add(c);
                    	token = new Token("INC/DEC", convertToString(vector), line);
                    	createToken(token, filewriter);
                    }else if(vector.lastElement() == '/' && c == '/') {
                    	c = (char) fr.read();
                    	while(c != '\r') {
                    		c = (char) fr.read();
                    		if(c == '\r') {
                    			line++;
                    		}
                    	}
                    }else {
                    	flag1 = 1;
                    	if(vector.lastElement() == '+' || vector.lastElement() == '-') {
                			token = new Token("Arithmetic Operator POS/NEG", convertToString(vector), line);
                			createToken(token, filewriter);
                		}else {
                			token = new Token("Arithmetic Operator", convertToString(vector), line);
                			createToken(token, filewriter);
                		}
                    }
                	vector.clear();
                }else if(contains(String.valueOf(c), punctuators)) {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	vector.clear();
                	vector.add(c);
                	token = new Token(convertToString(vector), convertToString(vector), line);
        			createToken(token, filewriter);
        			vector.clear();
                }else if(c == '&') {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	vector.clear();
                	vector.add(c);
                	c = (char) fr.read();
                	if(c == '&') {
                		vector.add(c);
                		token = new Token("Logical Operator", convertToString(vector), line);
            			createToken(token, filewriter);
                	}else {
                		vector.add(c);
                		invalidToken(line, filewriter, convertToString(vector));
                	}
                	vector.clear();
                }else if(c == '|') {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	vector.clear();
                	vector.add(c);
                	c = (char) fr.read();
                	if(c == '|') {
                		vector.add(c);
                		token = new Token("Logical Operator", convertToString(vector), line);
            			createToken(token, filewriter);
                	}else {
                		vector.add(c);
                		invalidToken(line, filewriter, convertToString(vector));
                	}
                	vector.clear();
                }else if(contains(String.valueOf(c), relationalOperators)) {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                	}
                	vector.clear();
                	vector.add(c);
                	c = (char) fr.read();
                	if(c == '=') {
                		vector.add(c);
                		token = new Token("Relational Operator", convertToString(vector), line);
            			createToken(token, filewriter);
                	}else if(c == '\r') {
                		if(vector.lastElement() == '<' || vector.lastElement() == '>') {
                			token = new Token("Relational Operator", convertToString(vector), line);
                			createToken(token, filewriter);
                		}else {
                			token = new Token(convertToString(vector), convertToString(vector), line);
                			createToken(token, filewriter);
                		}
                		line++;
                	}else {
                		flag1 = 1;
                		if(vector.lastElement() == '<' || vector.lastElement() == '>') {
                			token = new Token("Relational Operator", convertToString(vector), line);
                			createToken(token, filewriter);
                		}else {
                			token = new Token(convertToString(vector), convertToString(vector), line);
                			createToken(token, filewriter);
                		}
                	}
                	vector.clear();
                }else if(c == ' ') {
                	if(vector.size() != 0) {
                		specifyToken(convertToString(vector), filewriter, line);
                		vector.clear();
                	}
                }else if(c == '.'){
                	if(isInteger(convertToString(vector)) && vector.size() != 0) {
                		vector.add(c);
                	}else {
                		if(vector.size() != 0) {
                			specifyToken(convertToString(vector), filewriter, line);
                			vector.clear();
                		}
                		vector.add(c);
                		token = new Token(convertToString(vector), convertToString(vector), line);
            			createToken(token, filewriter);
            			vector.clear();
                	}
                }else {
                	vector.add(c);
                }
                
                
                
                
                if(flag1 == 0) {
                	a = fr.read();
                }else {
                	a = (int) c;
                	flag1 = 0;
                }
                
            }
            if(vector.size() != 0) {
        		specifyToken(convertToString(vector), filewriter, line);
        		vector.clear();
        	}
            filewriter.close();
            fr.close();
        } catch (Exception e) {
            
        }
		
	}

}
