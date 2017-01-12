/*
 * 
 * This class constructs an automaton to represent a regular expression.
 * The construction process implements Thompson's construction algorithm.
 * 
 * @author: Julien Horwood
 * 
 * 
 */

import java.util.Stack;

class FiniteAutomaton{
	private State startState, endState;
	
	//Constructors
	public FiniteAutomaton(){
		endState=new RegularState(null, null, null);
		startState=new RegularState(endState, null, null);
	}

	public FiniteAutomaton(Character token){
		endState=new RegularState(null, null, token);
		startState=new RegularState(endState, null, null);
	}

	public FiniteAutomaton(Character min, Character max){
		endState=new CharRangeState(null,null,min,max);
		startState=new RegularState(endState,null,null);
	}

	public FiniteAutomaton(State startState, State endState){
		this.startState=startState;
		this.endState=endState;
	}

	//Convert regular expression to NFA. Implements Thompson's construction algorithm.
	//We assume the given RE is in processed, postfix notation

	public FiniteAutomaton(String regex){ 
		
		Stack<FiniteAutomaton> partialFAs=new Stack<FiniteAutomaton>();
		FiniteAutomaton end1, end2;
		//tokenize the characters
		char [] tokens=regex.toCharArray();	
		
		// Loop through the array of tokens
		for(char token:tokens){
			FiniteAutomaton tempFA;
			
			switch(token){

				// if it is | , take the 2 previous FAs for the previous characters, and create a new one that branches out

				case '|':	
							
							end1=partialFAs.pop();
							end2=partialFAs.pop();
							tempFA=splitFA(end1,end2);
							partialFAs.push(tempFA);
							break;

				// it it is *, take the previous FA and construct a new FA for its closure

				case '*':	end1=partialFAs.pop();
							tempFA=closureFA(end1);       
							partialFAs.push(tempFA);
							break;

				// if it is +, take the previous FA and construct a new FA for its positive closure

				case '+':	end1=partialFAs.pop();
							tempFA=posClosureFA(end1);
							tempFA.setStart(end1.getStart());
							partialFAs.push(tempFA);
							break;

				// if it is ?, take the previous FA and split it with a null FA

				case '?':	end1=partialFAs.pop();
							State end=new RegularState(null,null,null);
							State start=new RegularState(end1.getStart(), end, null);
							end1.getEndState().setOutState1(end);
							tempFA=new FiniteAutomaton(start,end);
							partialFAs.push(tempFA);
							break;

				//If it is the special character #, concatenate the 2 previous FAs

				case '#':	end2=partialFAs.pop();
							end1=partialFAs.pop();
							tempFA=concatenate(end1, end2);
							partialFAs.push(tempFA);
							break;

				case '-':	end2=partialFAs.pop();
							end1=partialFAs.pop();
							if(end1.getEndState() instanceof RegularState && end2.getEndState() instanceof RegularState){
							tempFA=new FiniteAutomaton(((RegularState)end1.getEndState()).getTransition(), ((RegularState)end2.getEndState()).getTransition());
							} else{
								System.err.println("Invalid input");
								tempFA=null;
							}
							partialFAs.push(tempFA);
							break;

				case '.':	tempFA=new FiniteAutomaton(Character.MIN_VALUE, Character.MAX_VALUE);
							partialFAs.push(tempFA);
							break;

				// if it is a char, create an FA for that char

				default:   tempFA=new FiniteAutomaton(token); 
						   partialFAs.push(tempFA);
						   break;
			}		
		
		}
		FiniteAutomaton a=partialFAs.pop();
		startState=a.getStart();
		endState=a.getEndState();
	}

	private FiniteAutomaton splitFA(FiniteAutomaton a1, FiniteAutomaton a2){
		State start=new RegularState(a1.getStart(), a2.getStart(), null);
		State end=new RegularState(null, null, null);
		a1.getEndState().setOutState1(end);
		a2.getEndState().setOutState1(end);
		return new FiniteAutomaton(start, end);
	}

	private FiniteAutomaton closureFA(FiniteAutomaton a){
		State end=new RegularState(null, null, null);
		a.getEndState().setOutState1(a.getStart());
		a.getEndState().setOutState2(end);
		State start=new RegularState(a.getStart(), end, null);
		return new FiniteAutomaton(start,end);
	}

	private FiniteAutomaton posClosureFA(FiniteAutomaton a){
		a.getEndState().setOutState1(a.getStart());
		return a;
	}

	private FiniteAutomaton concatenate(FiniteAutomaton a1, FiniteAutomaton a2){
		a1.getEndState().setOutState1(a2.getStart());
		return new FiniteAutomaton(a1.getStart(), a2.getEndState());
	}



	public State getStart() {return startState; }
	public State getEndState(){ return endState; }
	public void setStart(State start){ this.startState=start; }
	public void setEndState(State endState) {this.endState=endState; }
	

	public void pprint(){
		pprint(this.startState,"");
	}

	public void pprint(State state, String depth){ 
	if(state==startState){System.out.print("Start state: ");}
	if(state==endState) {System.out.println(depth+"End State: "+state.toString(""));}
		else{ System.out.println(state.toString(depth)); }
	state.setWasPrinted(true);

	depth+="	";
	State out1=state.getOutState1();
	State out2=state.getOutState2();

	if(out1!=null || out2!=null){
		System.out.println(depth+"outgoing states:");
	}
	if(out1!=null){
		if(out1.getWasPrinted()==false){
			pprint(out1,depth);
		} else if(out1==endState){
		System.out.println(depth+"End state: "+out1.toString(""));
		} else if(out1==startState){
		System.out.println(depth+"Start state: "+out1.toString(""));
		} else{
		System.out.println(depth+out1.toString(""));
		}
	}

	if(out2!=null){
		if(out2.getWasPrinted()==false){
		pprint(out2, depth);
		} else if(out2==endState){
		System.out.println(depth+"End state: "+out2.toString(""));
		} else if(out2==startState){
		System.out.println(depth+"Start state: "+out2.toString(""));
		} else{
		System.out.println(depth+out1.toString(""));
		}
	}


	
	}
	


}