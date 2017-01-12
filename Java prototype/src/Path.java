/*
 * 
 * A path is the object that links the suffix tree to an automaton for simulation.
 * 
 * @author: Julien Horwood
 * 
 * 
 */

import java.util.HashSet;
class Path{

	private SuffixTree tree;
	private NodeInterface pathNode;
	private HashSet<State> activeStates;
	private int index; //for node edge
	private int edgeLength;
	private char [] edgeChars;
	private boolean isAccepted;
	public static int counter;
	


	public Path(SuffixTree tree, NodeInterface node, HashSet<State> activeStates){
		
		this.tree=tree;
		this.pathNode=node;
		this.activeStates=activeStates;
		index=0;
		edgeLength=node.getLength();
		edgeChars=tree.getSubstring(node.getLeftIndex(), edgeLength).toCharArray();
			
	}
	
	

	//getters
	public NodeInterface getPathNode(){ return pathNode; }
	public HashSet<State> getActiveStates(){return activeStates;}
	public int getIndex(){return index;}
	public int getEdgeLength(){return edgeLength;}
	public boolean getIsAccepted(){return isAccepted;}
	public SuffixTree getSuffixTree(){return tree;}

	//setters
	public void setPathNode(NodeInterface pathNode){
		this.pathNode=pathNode;
		edgeLength=pathNode.getLength();
	}
	public void setActiveStates(HashSet<State> activeStates){this.activeStates=activeStates;}
	public void setIndex(int index){this.index=index;}
	public void setIsAccepted(boolean isAccepted){this.isAccepted=isAccepted;}

	
	//Returns the character in position index of the substring found on the edge leading to this node
	public Character nextChar(){
		char c=edgeChars[index];
		index++;
		return c;
	}

	public boolean equals(Object o){
		if(o==null){
			return false;
		}
		if(getClass() != o.getClass()){
			return false;
		}
		Path path=(Path) o;
		if(path.getPathNode() != this.pathNode || path.getActiveStates() !=this.activeStates || this.tree!=path.getSuffixTree()){
			return false;
		}
		return true;
	}

	public int hashCode(){
		int hash=1;
		hash=hash*41+pathNode.hashCode();
		hash=hash*41+activeStates.hashCode();
		return hash;
	}
	public String toString(){
		String string;
		string=("Node: "+pathNode.toString()+" ");
		for(Character c :edgeChars)
			string+=c+", ";
		for(State s:activeStates)
		string+="	"+s.toString("")+", ";
		return string;
	}

}