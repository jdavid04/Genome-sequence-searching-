/*
 * 
 * Represents a state in the automaton as being entered upon by reading a range of inputs from the alphabet.
 * A CharRangeState is created when a character class is part of the regular expression from which an automaton 
 * is to be constructed.
 * 
 * @author: Julien Horwood
 * 
 * 
 */

class CharRangeState implements State{
	
private State outState1, outState2; //States reached by outgoing transitions
private Character minChar; //Determines start of char range
private Character maxChar; //Determines end of char range
private boolean wasPrinted;

public CharRangeState(State outState1, State outState2, Character minChar, Character maxChar){
	this.outState1=outState1;
	this.outState2=outState2;
	this.minChar=minChar;
	this.maxChar=maxChar;
	wasPrinted=false;
}

// getters

public State getOutState1() {return outState1;}
public State getOutState2() {return outState2;}
public Character getMinChar() {return minChar;}
public Character getMaxChar() {return maxChar;}
public boolean getWasPrinted() {return wasPrinted;}

// setters

public void setOutState1(State outState1) {this.outState1=outState1;}
public void setOutState2(State outState2) {this.outState2=outState2;}
public void setMinChar(Character minChar) {this.minChar=minChar;}
public void setMaxChar(Character maxChar) {this.maxChar=maxChar;}
public void setWasPrinted(boolean b){this.wasPrinted=b;}



public String toString(String depth){
	String result;
	if(minChar==null || maxChar==null){result=depth+"<Null transition to this state>";}
		else { result=depth+"<state transition range: From "+minChar+" to "+maxChar+">"; }

	return result;
	}

}