/*
 * 
 * Interface for a state within the automaton.
 * 
 * @author: Julien Horwood
 * 
 * 
 */
interface State{


	public State getOutState1();
	public State getOutState2();
	public boolean getWasPrinted();

	public void setOutState1(State a);
	public void setOutState2(State a);
	public void setWasPrinted(boolean b);

	public String toString(String depth);
	
	
}