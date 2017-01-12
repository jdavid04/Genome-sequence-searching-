/*
 * 
 * A regular state within the automaton. Takes one letter, or possibly null, as input before it is entered.
 * 
 * @author: Julien Horwood
 * 
 * 
 */

class RegularState implements State{

private State outState1, outState2; //States reached by outgoing transitions
private Character transition; //Character that enables a transition to this state
private boolean wasPrinted;

public RegularState(State outState1, State outState2, Character transition){
	this.outState1=outState1;
	this.outState2=outState2;
	this.transition=transition;
	wasPrinted=false;
}

// getters

public State getOutState1() {return outState1;}
public State getOutState2() {return outState2;}
public Character getTransition() {return transition;}
public boolean getWasPrinted() {return wasPrinted;}

// setters

public void setOutState1(State outState1) {this.outState1=outState1;}
public void setOutState2(State outState2) {this.outState2=outState2;}
public void setTransition(Character transition)  {this.transition=transition;}
public void setWasPrinted(boolean b){this.wasPrinted=b;}



public String toString(String depth){
	String result;
	if(transition==null){result=depth+"<Null transition to this state>";}
		else { result=depth+"<state transition: "+transition+">"; }

	return result;
}





 }