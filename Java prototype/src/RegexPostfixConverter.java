/*
 * This class provides the required functionality to convert an expression from infix to postfix representation.
 * This allows it to be used as input for the construction of an automaton (See class FiniteAutomaton).
 * The class also provides the functionality to format a string appropriately for input.
 * 
 * 
 * @author: Julien Horwood
 * 
 * 
 */

import java.util.Stack;
import java.util.HashMap;
class RegexPostfixConverter{
	
	private String postfixRegex;
	private static final HashMap<Character,Integer> precedenceTable=new HashMap<Character, Integer>();
		static{
		precedenceTable.put('*', 3);
		precedenceTable.put('+', 3);
		precedenceTable.put('?', 3);
		precedenceTable.put('-', 3);
		precedenceTable.put('#', 2);
		precedenceTable.put('|', 1);
		}	

	public RegexPostfixConverter(){
		postfixRegex=null;
	}

	public RegexPostfixConverter(String infix){
		infix=format(infix);
		postfixRegex=toPostfix(infix);
	}

	public String getPostfixRegex(){
		return postfixRegex;
	}

	/*This method inserts the concatenation operator #, expands the range notation 
	* and treats character classes in order to convert the expression to an adequate format
	* for the matching algorithm
	*/
	public String format(String regex){
		StringBuilder expression=new StringBuilder();
		char [] characters=regex.toCharArray();
		Character lastChar=null;
		for(int i=0; i<characters.length; i++){
			Character c=characters[i];
			switch(c){

				case '(':	if(lastChar!=null && lastChar!='|')
								expression.append('#');
							expression.append(c);
							break;
						
				case '[': 	if(lastChar!= null && lastChar!='|'){
								expression.append('#');
							}
							lastChar='[';
							i++;
							expression.append('(');
							
							while(characters[i]!=']'){
								if(Character.isLetter(characters[i]) && (Character.isLetter(lastChar) || lastChar=='-')){
								expression.append('|');
								expression.append(characters[i]);
								} else if(characters[i]=='-' && Character.isLetter(lastChar) && Character.isLetter(characters[i+1])){
									expression.append(characters[i]);
									expression.append(characters[i+1]);
									i++;
								} 
								else if(Character.isLetter(characters[i]) && lastChar=='['){
									expression.append(characters[i]);
								} else { System.err.println("Invalid input");}
								lastChar=characters[i];
								i++;
							} 	
							expression.append(")");
							break;

				case '*': 	expression.append(c);
							break;

				case '+':	expression.append(c);
							break;

				case '?':	expression.append(c);
							break;

				case '{':   int lastSeparator;
							String tempString;
							if(characters[i-1]==')'){
							lastSeparator=expression.lastIndexOf("(");
							tempString=expression.substring(lastSeparator);
							expression.delete(lastSeparator, expression.length());
							} else{ 
								lastSeparator=expression.lastIndexOf("#");
								tempString=expression.substring(lastSeparator+1);
								expression.delete(lastSeparator+1, expression.length());
								}
							
							
							int num=Character.getNumericValue(characters[i+1]);
							for(int index=0; index<num-1; index++){
								expression.append(tempString);
								expression.append('#');
							}
							expression.append(tempString);
							if(characters[i+2]==',' && characters[i+3]=='}'){
								expression.append('#');
								expression.append(tempString);
								expression.append('*');
								i+=3;
							} else if(characters[i+2] == '}'){
								i+=2;
							} else if(characters[i+2]==',' && characters[i+3]!='}'){
								expression.append('#');
								num=Character.getNumericValue(characters[i+3])-num;
								for(int index=0; index<num-1; index++){
									expression.append(tempString);
									expression.append('?');
									expression.append('#');
								}
								expression.append(tempString);
								expression.append('?');
								i+=4;
							} else {System.err.println("Invalid input");}
							break;



				case '|':	expression.append(c);
							break;

				case ')':	expression.append(c);
							break;

				default :	if(lastChar!=null && lastChar!='|' && lastChar!='(')
							expression.append('#');
							expression.append(c);
							break;

			}

			lastChar=characters[i];
		}

		return expression.toString();

	}

	/*
	*This string converts a valid format infix string into its corresponding postfix expression.
	*Implements the shunting-yard algorithm.
	*/
	public String toPostfix(String infix){
		StringBuilder postfix=new StringBuilder();
		Stack<Character> literals=new Stack<Character>();
		char [] characters=infix.toCharArray();
		

		for(int i=0; i<characters.length; i++){
			char c=characters[i];
			if(c=='*' || c=='+' || c=='?' || c=='-' || c=='#' || c=='|'){
				while(!literals.empty() && precedenceTable.get(literals.peek())!=null && precedenceTable.get(c) <= precedenceTable.get(literals.peek())){
					postfix.append(literals.pop());
				}
				literals.push(c);
			} else if(c=='('){
				literals.push(c);
			} else if(c==')'){
				while(literals.peek()!='('){
					postfix.append(literals.pop());
				}
				literals.pop();
			} else{
				postfix.append(c);
			}
		}

		while(!literals.empty()){
			postfix.append(literals.pop());
		}

		return postfix.toString();
	}
}