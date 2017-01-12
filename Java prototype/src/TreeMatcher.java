/*
 * 
 * This class provides the essential functionality for searching regular expressions within a suffix tree.
 * The simulation of an automaton in the tree takes place within the findPaths and checkFA methods.
 * 
 * @author: Julien Horwood
 * 
 * 
 */

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;



class TreeMatcher{

	private SuffixTree tree; 

	public TreeMatcher(SuffixTree tree){
		this.tree=tree;
	}
	/*
	 * Determines whether or not a regular expression is in the tree.
	 */
	public boolean matches(String regex){
		RegexPostfixConverter converter=new RegexPostfixConverter(regex);
		
		ArrayList<Path> paths=findPaths(converter.getPostfixRegex());
		
		if(paths.size()>0){
			return true;
		} else {return false;}
	}

	/*
	 * Counts the sequences containing an occurence of the regular expression.
	 * The suffix tree must be annotated prior to this method being called.
	 */
	public int count(String regex){
		RegexPostfixConverter converter=new RegexPostfixConverter(regex);
		ArrayList<Path> paths=findPaths(converter.getPostfixRegex());
		BitSet vector=new BitSet();
		
		for(int i=0; i<paths.size();i++){
			NodeInterface node=paths.get(i).getPathNode();
			
			Info bitVector=node.getInfo();
			if(bitVector instanceof SequenceVector)
				vector.or(((SequenceVector)bitVector).getVector());
		}
		
		
		return vector.cardinality();
	}

	/*
	 * Returns a list of the indices of the sequences within the tree that contain 
	 * a match for the regular expression.
	 */
	public ArrayList<Integer> findAllSeq(String regex){
		ArrayList<Integer> sequences;
		RegexPostfixConverter converter=new RegexPostfixConverter(regex);
		sequences=findAllSeqPostfix(converter.getPostfixRegex());
		
		return sequences;

	}

	/*
	 * This method returns the list of paths for which a match in the tree has been found.
	 * Each path's node can then be used by the various searching methods to output the desired result.
	 */
	private ArrayList<Path> findPaths(String regex){

		FiniteAutomaton automaton=new FiniteAutomaton(regex);
		
		ArrayList<Path> paths=new ArrayList<Path>(500);
		
		HashSet<State> states=new HashSet<State>();
		states.add(automaton.getStart());
		paths.add(new Path(tree,tree.getRoot(), states));

		for(int i=0; i<paths.size();i++){ // This loops ends with only the accepted paths
			Path path=paths.get(i);
			if(path.getIsAccepted()){ continue;} //this path is already accepted, move to the next
			//the regex extends beyond this leafnode, remove it and move on to the next
			if(path.getPathNode() instanceof LeafNode && path.getIndex()==path.getEdgeLength()){
				paths.remove(i);
				i--;
				continue;
			}
			//If currently at a branching point in the tree,create new paths
			if(path.getPathNode()==tree.getRoot() || (path.getPathNode() instanceof InternalNode && path.getIndex()==path.getEdgeLength())){
				ArrayList<Path> newPaths=branchOut(automaton, path,paths);
				paths.addAll(newPaths);
				i--;		
			} else{
				char c=path.nextChar();
				if(!checkFA(automaton, c, path)){ //if path in FA is possible, updates active state for path
					paths.remove(i); //if no route in FA possible, eliminate this path
				}
				i--;
			}
		
			
		}

		return paths;

	}

	/*
	 * Analogous to findAllSeq, but the input must already be in postfix form.
	 */
	public ArrayList<Integer> findAllSeqPostfix(String regex){
		
		ArrayList<Integer> seq=new ArrayList<Integer>();
		ArrayList<Path> paths=new ArrayList<Path>();
		paths=findPaths(regex);
		BitSet vector=new BitSet();
		
		for(int i=0; i<paths.size();i++){
			NodeInterface node=paths.get(i).getPathNode();
			
			Info bitVector=node.getInfo();
			if(bitVector instanceof SequenceVector)
				vector.or(((SequenceVector)bitVector).getVector());
		}
		
		int index=0, next;
		
		while((next=vector.nextSetBit(index))!=-1){
			seq.add(next);
			index=next+1;
		}
		
			
		return seq;
	}

	/*
	This method uses a helper method of its own to determine whether or not there is a valid path
	from the current state matching the provided the character. It automatically updates the active state 
	for this path if there is one. It the new active state is the end state, it also accepts the path.
	*/

	private boolean checkFA(FiniteAutomaton automaton, char c, Path path){
			
			HashSet<State> newActives=checkFA(automaton,path.getActiveStates(), c);
			
			if(newActives.isEmpty()){
				return false;
			} else{
				if(newActives.contains(automaton.getEndState())){
					path.setIsAccepted(true);
				}
				path.setActiveStates(newActives);
				return true;
			}
			
		}

	/*
	This helper method updates the valid states reached after reading the next input character for this path.
	*/

	private HashSet<State> checkFA(FiniteAutomaton automaton, HashSet<State> actives, Character c){
			
		HashSet<State> next=new HashSet<State>();
		HashSet<State> tempActives=new HashSet<State>();
		
		//traverse epsilon transitions
		for(State active : actives){
		tempActives.addAll(reachables(active));
		}
		
		
		
		for(State active : tempActives){
			State out1=active.getOutState1();
			State out2=active.getOutState2();
			
			//base cases
			if(active==automaton.getEndState()){
				next.add(active);
			}
			
			if(out1==null && out2==null){
				continue;
			}
			
			//If no base cases apply, traverse FA to find reachable states from reading the character
			
			if(out1!=null && out1 instanceof RegularState && ((RegularState)out1).getTransition()==c){
				next.add(out1);
			} else if(out1 != null && out1 instanceof CharRangeState){
				char min=((CharRangeState)out1).getMinChar();
				char max=((CharRangeState)out1).getMaxChar();
				if(min<=c && c<=max){
					next.add(out1);
				}
			}
			
			if(out2!=null && out2 instanceof RegularState && ((RegularState)out1).getTransition()==c){
				next.add(out2);
			} else if(out2 != null && out2 instanceof CharRangeState){
				char min=((CharRangeState)out2).getMinChar();
				char max=((CharRangeState)out2).getMaxChar();
				if(min<=c && c<=max){
					next.add(out2);
				}
			}
			
		}
		
		return next;

	}
		
	
	
	
	//create new paths corresponding to the edges that branch out from current internal node
		private ArrayList<Path> branchOut(FiniteAutomaton automaton, Path path,ArrayList<Path> paths){

		paths.remove(path);
		return branchOut(automaton, path,((NodeInterface)((InternalNode)path.getPathNode()).getFirstChild()));
		}

		private ArrayList<Path> branchOut(FiniteAutomaton automaton,Path path, NodeInterface node){

			ArrayList<Path> newPaths=new ArrayList<Path>();
			Path p=new Path(tree, node, path.getActiveStates());
			newPaths.add(p);	
			
			
			if(node.getRightSybling()!=null){
				newPaths.addAll(branchOut(automaton, path,((NodeInterface)node.getRightSybling())));
			}
			return newPaths;

		}
		
		/*
		 * Returns the set of states which are reachable from the active state parameter.
		 */
		private HashSet<State> reachables(State active){
			
			HashSet<State> newActives=new HashSet<State>();
				
				State out1,out2;
				out1=active.getOutState1();
				out2=active.getOutState2();
				
				if(out1!=null && out1 instanceof RegularState && ((RegularState)out1).getTransition()==null){
					newActives.addAll(reachables(out1));
				} else{
					newActives.add(active);
				}
				
				if(out2!=null && out2 instanceof RegularState && ((RegularState)out2).getTransition()==null){
					newActives.addAll(reachables(out2));
				} else{
					newActives.add(active);
				}
				
				return newActives;
			
		}

		/*
		 * Method used to annotate the suffix tree with SequenceVectors. Must be called before count or findAllSeq.
		 */
		public void annotate(int seqLength,int numSeq){
			
			if(tree.getRoot()==null) return;
			
			ArrayList<BitSet> vectors=annotate(seqLength, numSeq,(NodeInterface)tree.getRoot().getFirstChild());
			BitSet vector=new BitSet(numSeq);
			for(int i=0; i<vectors.size();i++){
				vector.or(vectors.get(i));
			}
			SequenceVector seqV=new SequenceVector(vector);
			tree.getRoot().setInfo(seqV);		
			
			
			
		}
		
		private ArrayList<BitSet> annotate(int seqLength, int numSeq, NodeInterface node){
			
			if(node==null){
				return null;
			}
			ArrayList<BitSet> vectors=new ArrayList<BitSet>();
			BitSet vector=new BitSet(numSeq);
			if(node instanceof InternalNode){
				vectors=annotate(seqLength,numSeq, (NodeInterface)((InternalNode)node).getFirstChild());
				for(int i=0; i<vectors.size();i++){
					vector.or(vectors.get(i));
				}
                     
			} else{
				
				SuffixCoordinates coord=((LeafNode)node).getCoordinates();
				while(coord!=null){
				
				vector.set(coord.getPosition()/seqLength);
				coord=coord.getNext();
				
				}
				vectors.add(vector);
			}
			
			SequenceVector seqV=new SequenceVector(vector);
			node.setInfo(seqV);
			
			if(node.getRightSybling()!=null){
				vectors.addAll(annotate(seqLength,numSeq, (NodeInterface)node.getRightSybling()));
			}
			
			return vectors;
		}
}
		
	

	


