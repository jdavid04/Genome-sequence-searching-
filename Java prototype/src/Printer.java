/*
 * 
 * A tool for printing the contents of a suffix tree.
 * 
 * @author: Julien Horwood
 * 
 * 
 */

class Printer{
	private SuffixTree tree;

	public Printer(SuffixTree tree){
		this.tree=tree;
	}

	public SuffixTree getSuffixTree(){
		return tree;
	}

	public void setSuffixTree(SuffixTree tree){
		this.tree=tree;
	}

	private void pprint(NodeInterface node, String depth){
		if(node==tree.getRoot()) {
			System.out.println("root node");
			pprint(((NodeInterface)((InternalNode)node).getFirstChild())," ");
		}
			else{
				String info="";
				String label=tree.getSubstring(node.getLeftIndex(), node.getLength());
				if(node.getInfo()!=null){  info=node.getInfo().toString();}
				if(node instanceof InternalNode){
					System.out.println(depth + "internal node label: "+label+" "+info);
					pprint(((NodeInterface)((InternalNode)node).getFirstChild()), depth+ " ");
				}
				if(node instanceof LeafNode){
					System.out.println(depth+ "leaf node label: "+label+" , pos: "+((LeafNode)node).getCoordinates().toString()+" "+info);	
				}
				NodeInterface rightNode=((NodeInterface)node.getRightSybling());
				if(rightNode!=null){pprint(rightNode, depth);}
			}
	}


	public void pprint(){
		pprint(tree.getRoot(),"");
	}
}