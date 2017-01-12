"""
The TreeMatcher Module contains all the functionality for searching for regular expressions
within a Suffix Tree data structure.
It provides the option of determining if a regex has a match in the tree, counting the number of strings
which contain an occurence, and returning the indices of the strings that contain a match. These searching 
methods are provided by the Matcher class, while the intermediate step of creating an NFA representation for 
the regex is taken care of in the NFA class. See README file for more information on use of the module. 

The required modules to be installed into python for this module to be functional are the following:

SuffixTree module : https://github.com/JDonner/SuffixTree
bitarray module: https://pypi.python.org/pypi/bitarray/

"""

__author__ = "Julien Horwood, University of Ottawa"


class Matcher(object):
	"""
	Defines the API for searching regular expressions within a suffix tree
	The API consists of methods countSeq, findAllSeq and matches. _findPaths should
	be viewed solely as a helper method for the searching methods, while annotate is
	to be used in preprocessing the tree.
	"""
	from bitarray import bitarray

	def __init__(self,tree):
		"initialize a matcher with a suffix tree"
		self.tree=tree

	def countSeq(self,regex, vectors):
		"This method returns the number of sequences containing an occurence of the regular expression defined by the NFA."
		nfa=NFA(regex)
		paths=self._findPaths(nfa)
		vector=self.bitarray(vectors[0].length())
		vector.setall(0)

		for p in paths:
			vector=vector | vectors[p.ident()]

		return vector.count()


	def findAllSeq(self,regex,vectors):
		"This method returns a list containing the indices, starting at 0, of all sequences within the text that were matched by the NFA."

		nfa=NFA(regex)
		paths=self._findPaths(nfa)
		vector=self.bitarray(vectors[0].length())
		vector.setall(0)

		for p in paths:
			vector=vector | vectors[p.ident()]

		
		sequences=[]
		it=vector.itersearch(self.bitarray("1"))
		for i in it:
			sequences.append(i)

		return sequences


	def matches(self,regex):
		"This method returns true if the NFA is matched in the text. Otherwise, it returns false."
		if self._findPaths(NFA(regex),matches=1): return True
		return False

	def _findPaths(self,nfa, matches=0):
		"""
		A helper method at the core of the search for all API methods. 
		Takes as input an nfa and returns a list of nodes which correspond to a match.
		If matches==1, then matches was called and we only need one node to return.
		"""
		def branchout(node,actives):
			"A helper method which creates new paths for all children of the current node when required."
			child=node.children()
			newpaths=[]
			while child is not None:
				newpaths.append((child,0,actives))
				child=child.next()
			return newpaths

		if self.tree is None:
			return
		pathnodes=set([]) # A path is a tuple of the form (node, index(for the character at index of the string found on incoming edge) , active states)
		tempPaths=[]
		#An initial branchout from the root is always required
		tempPaths.extend(branchout(self.tree.root(),frozenset([0])))
		
		for p in tempPaths:
			
			# if regex extends beyond leaf node continue
			if(p[1]==p[0].edgelen()): 		# if at branching point branchout
				tempPaths.extend(branchout(p[0],p[2]))
			else : #simulate the NFA
				newStates=set([])
				c=p[0].edgestr()[p[1]]		#The character to be read next
				
				for s in p[2]:		#loop over all active states to check for character
					
					if c in nfa.transitions[s]: #if character at index of the edge string is in the dictionary for this state
						newStates=newStates.union(nfa.transitions[s][c]) #add the states corresponding to the character
				if newStates.intersection(nfa.finalStates):
					pathnodes.add(p[0])
					if matches==1: return pathnodes
				elif newStates:		#if no final states were reached and newStates was updated, create a new path
					tempPaths.append((p[0], p[1]+1, frozenset(newStates)))

		return pathnodes

	def annotate(self,numSeq):
		"""
		This method associates to each node within the tree a bit-vector representing the sequences 
		of the tree which have a suffix in the subtree of each node. This should be used for preprocessing
		the tree before searches involving the counting of sequences or identification of sequence matches.
		Implements an iterative traversal so as to not lose memory for larger trees. The method returns this
		information in the form of a list of bitarrays, with each node's identifier as an index into the list.

		"""
		vectors=[None]*self.tree.num_nodes() 	#Array that will be used to store the bit-vectors
		#A dictionary childvectors will be used to store information about the bitarrays of each parent node's children.
		#The dictionary also allows us to keep track of which subtree was already visited. 
		#These bitarrays will in turn be used to compute the internal node's bitarray. 
		childvectors={}		
		current=self.tree.root()
		while current is not None:
			# traverse down the left of the tree
			if current.children() is not None and current.ident() not in childvectors:
				childvectors[current.ident()]=[]
				current=current.children()
				continue
			else:
			    #compute bitvector
				leafvector=self.bitarray(numSeq)
				leafvector.setall(0)
				if current.ident() in childvectors: #not a leaf node
					for x in childvectors[current.ident()]:
						leafvector=leafvector | x
					del childvectors[current.ident()]

				# modify bitvector for suffixes ending at this node
				if current.num_leaves()>0:		

					for i in range(1,current.num_leaves()+1):
						v=self.bitarray(numSeq)
						v.setall(0)
						v[current.leaf(i)[2]]=1
						leafvector=leafvector | v

				# Store the bitarray information and, if applicable, store into childvectors for the appropriate parent node.
				vectors[current.ident()]=leafvector
				if current.parent() is not None:
					childvectors[current.parent().ident()].append(leafvector)

			# Travel to the right
			if current.next() is not None:
				current=current.next()
			else: current=current.parent()

		return vectors

class NFA(object):
	
	def __init__(self,regex,alphabet="ACGT"):
		"""
		This method constructs an NFA, represented by a list of dictionaries, which corresponds 
		to a regular expression to be searched within the text. Implements Glushkov's construction of an NFA,
		as presented in: 
		Flexible Pattern Matching in Strings: Practical On-Line Search Algorithms for Texts and Biological Sequences (2002), 
		by Gonzalo Navarro and Mathieu Raffinot.
		The notable variables of this NFA are as follows:
		finalStates: Set of final states, represented as integers.
		transitions: A list of dictionaries to represent state transitions.
		alphabet: String of accepted characters, defaulted to ACGT for DNA sequences. This is used primarily for the "don't care"
				  syntax, and it's interpretation in the program requires use of small alphabets.
		follow: Determines for each state which states are reachable next.

		"""
		self.alphabet=alphabet
		regex=self.format(regex)
		self.follow=[set([])]*(len(regex)+1)
		d={}
		self.finalStates=set([])
		#parse the regular expression
		self.root=self.parse(regex,0)[0]
		#build the variables on the tree
		self.m=self.variables(self.root,0)
		self.transitions=[None]*(self.m+1)
		#build the automaton

		

		for x in self.root.first: #node.first must be implemented to return a list of tuples of the form (position,character)
			if x[1] in d: #if character is already in the dictionary
				d[x[1]].add(x[0]) #add the position to the set of transitions
			else :
				s=set([x[0]])
				d[x[1]]=s #associate a new set to the character as a key in the dictionary
		self.transitions[0]=d
		
		for i in range(self.m+1):
			
			
			for x in self.follow[i]: #follow must be implemented to return a list of sets of tuples of the form (position,character)
				if x[1] in d: #if character is already in the dictionary
					d[x[1]].add(x[0]) #add the position to the set of transitions
				else :
					s=set([x[0]])
					d[x[1]]=s #associate a new set to the character as a key in the dictionary
			self.transitions[i]=d
			d={}
			
		if self.root.empty: #node.empty must be set to return true if the null transition is a valid word
			self.finalStates.add(0)
		for x in self.root.last:
			self.finalStates.add(x[0])

	def format(self,regex):
		"""
		This method interprets simple character classes and don't care syntax. 
		It would be worthwile to explore expanding it or integrating this parsing into the parse method for optimal performance.

		"""
		expression=""
		anything="("
		for x in self.alphabet[:-1]:
			anything+=x+"|"
		anything+=self.alphabet[-1]+")"
		regex=regex.replace(".",anything)
		i=0
		while i<len(regex):
			if regex[i]=="[":
				lastChar="["
				expression+="("
				expression+=regex[i+1]
				i+=2
				while regex[i]!="]":
					expression+="|"
					expression+=regex[i]
					i+=1
				expression+=")"
				i+=1

			else:
				expression+=regex[i]
				i+=1

		return expression

	def parse(self,regex,last):
		"Creates a parsing tree for the given regular expression."
	
		
		treeNode=None
		while last<len(regex):
			if regex[last]=='|':	#union operator
				(rNode,last)=self.parse(regex,last+1)
				treeNode=self.Node('|',treeNode,rNode)
			elif regex[last]=='*':	#star operator
				treeNode=self.Node('*',treeNode,None)
				last+=1
			elif regex[last]=='+':	#+ operator
				treeNode=self.Node('+',treeNode,None)
				last+=1
			elif regex[last]=='?':	#? operator
				treeNode=self.Node('?',treeNode,None)
				last+=1
			elif regex[last]=='(':	#open parenthesis
				(rNode,last)=self.parse(regex,last+1)
				last+=1
				if treeNode is not None:
					treeNode=self.Node('#',treeNode,rNode)
				else :
					treeNode=rNode
			elif regex[last]==')':	#close parenthesis
				return (treeNode,last)
			else:					#normal character
				
				rNode=self.Node(regex[last],None,None)
				if treeNode is not None:
					treeNode=self.Node('#',treeNode,rNode)
				else:
					treeNode=rNode
				last+=1
		
		return (treeNode,last)

	def variables(self,node, lpos):
		"""
		Computes the variables node.first(node), last(node), follow(x), and node.empty(node) for each node of the tree representation for regex.
		For each node, first is the set of positions at which simulation can begin for the regex corresponding to the subtree starting at node,
		last is the set of positions at which simulation may end for this subtree,  and empty determines whether or not the empty word is valid.
		follow is a global variable defining which positions are reachable from others.
		"""
		
		#Recursively compute the children
		if node.charKey in {'|' ,'#'}:
			lpos=self.variables(node.leftN,lpos)
			lpos=self.variables(node.rightN,lpos)
		elif node.charKey in {'*','+','?'}:
			lpos=self.variables(node.leftN,lpos)
		#Compute values for current node
		if node.charKey is None:
			node.first={}
			node.last={}
			node.empty=True
		elif node.charKey=='|':
			node.first=node.leftN.first.union(node.rightN.first)
			node.last=node.leftN.last.union(node.rightN.last)
			node.empty=node.leftN.empty or node.rightN.empty
		elif node.charKey=='#':
			if node.leftN.empty:
				node.first=node.leftN.first.union(node.rightN.first)
			else :
				node.first=node.leftN.first
			if node.rightN.empty:
				node.last=node.leftN.last.union(node.rightN.last)
			else :
				node.last=node.rightN.last
			node.empty=node.leftN.empty and node.rightN.empty
			for x in node.leftN.last:
				self.follow[x[0]]=self.follow[x[0]].union(node.rightN.first)
		elif node.charKey=='*':
			node.first=node.leftN.first
			node.last=node.leftN.last
			node.empty=True
			for x in node.leftN.last:
				self.follow[x[0]]=self.follow[x[0]].union(node.leftN.first)

		elif node.charKey=='+':
			node.first=node.leftN.first
			node.last=node.leftN.last
			node.empty=False
			for x in node.leftN.last:
				self.follow[x[0]]=self.follow[x[0]].union(node.leftN.first)
		elif node.charKey=='?':
			node.first=node.leftN.first
			node.last=node.leftN.last
			node.empty=True
		else:
			lpos+=1
			node.first={(lpos,node.charKey)}
			node.last={(lpos,node.charKey)}
			node.empty=False
			self.follow[lpos]=set([])
	
		return lpos

	class Node(object):
		"Represents node objects in the parsing tree."
		charKey=None
		leftN=None
		rightN=None
		first=None
		last=None
		empty=None

		def __init__(self,char,l,r):
			self.charKey=char
			self.leftN=l
			self.rightN=r

