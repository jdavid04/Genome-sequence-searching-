/*
 * This class implements the Info interface from the SuffixTree library in order to provide a bit-vector 
 * for a given node. This bit-vector represents the sequences within the tree which end in the sub-tree 
 * from this node.
 * 
 * 
 * @author: Julien Horwood
 * 
 * 
 */






import java.util.BitSet;


public class SequenceVector implements Info {

	private BitSet vector;
	private int numSeq=0;
	private Info nextInfo;
	
	
	public SequenceVector(BitSet bitVector){
		vector=bitVector;
		for(int i=0; i<bitVector.length();i++){
			if(bitVector.get(i)){
				numSeq++;
			}
		}
		
	}
	public Info getNextInfo() {
		
		return nextInfo;
	}

	public void setNextInfo(Info next) {
		nextInfo=next;
	}
	
	public BitSet getVector(){
		return vector;
	}
	public int getNumSeq(){
		return numSeq;
	}
	
	public void setVector(BitSet v){
		vector=v;
		numSeq=0;
		for(int i=0; i<v.length();i++){
			if(v.get(i)){
				numSeq++;
			}
		}
	}

}
