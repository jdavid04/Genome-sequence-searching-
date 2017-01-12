"""
A simple set of tests for verifying the functionality of the TreeMatcher module. 
Tests the function of matches, countSeq and findAllSeq by comparing it to the built-in regex matcher.

The module sets up by creating and annotating a suffix tree by adding the sequences from the cMyc.fasta file,
included in the package files. All files used in these tests are included in the package files, however if these
tests are to be used the paths for opening file objects should be specified for your own directory hierarchy.

For each test, the module then verifies all three methods with aid from the the built-in python functions from unittest.

"""

import unittest,SuffixTree,TreeMatcher,time,re

t=SuffixTree.SuffixTree()
matcher=None
vectors=[]
text=None
sequences=[]
def setUpModule():

	global t,text, sequences, matcher, vectors

	file1=open("data/cMyc.fasta",mode="r")
	text=file1.read()
	file1.close()
	sequences=re.split(">.*",text)
	for i in range(len(sequences)):
		sequences[i]=re.sub("\n","",sequences[i])
			
	text=""
	for l in sequences:
		text+=l
		
	#Creating the tree
	i=0
	for l in sequences[1:]:
		t.add(l,i)
		i+=1

	
	# Annotating the tree
	matcher=TreeMatcher.Matcher(t)
	print "annotating..."
	start=time.time()
	vectors=matcher.annotate(i+1)
	end=time.time()
	print "annotation time: "+str(end - start)

	def tearDownModule():
		global t,matcher,vectors
		t=None
		matcher=None
		vectors=None

class TestSolid(unittest.TestCase):
	"Test for checking the functionality of searches of regular expressions with all solid input characters."
	@classmethod
	def setUpClass(TestSolid):
		file2=open("Test files/Solid.txt",mode="r")
		TestSolid.lines=file2.read().splitlines()
		file2.close()
		
	
	def test_matches(self):
		for l in self.lines:
			self.assertEqual(matches_proxy(l,text),matcher.matches(l))

	def test_count(self):
		count=0
		for l in self.lines:
			r=re.compile(l)
			for s in sequences:
				if r.search(s) is not None: count+=1
			self.assertEqual(count,matcher.countSeq(l,vectors))
			count=0

	def test_findAllSeq(self):
		for l in self.lines:
			seq=[]
			r=re.compile(l)
			for index in range(len(sequences)):
				if r.search(sequences[index]) is not None: seq.append(index-1)
			seq.sort()
			seq2=matcher.findAllSeq(l,vectors)
			seq2.sort()
			self.assertEqual(seq,seq2)
		
class TestCharClass(unittest.TestCase):
	"Test for checking the functionality of searches of regular expressions with all CharacterClass input characters."
	@classmethod
	def setUpClass(TestCharClass):
		file2=open("Test files/CharacterClass.txt",mode="r")
		TestCharClass.lines=file2.read().splitlines()
		file2.close()


	def test_matches(self):
		for l in self.lines:
			self.assertEqual(matches_proxy(l,text),matcher.matches(l))

	def test_count(self):
		count=0
		for l in self.lines:
			r=re.compile(l)
			for s in sequences:
				if r.search(s) is not None: count+=1
			self.assertEqual(count,matcher.countSeq(l,vectors))
			count=0

	def test_findAllSeq(self):
		for l in self.lines:
			seq=[]
			r=re.compile(l)
			for index in range(len(sequences)):
				if r.search(sequences[index]) is not None: seq.append(index-1)
			seq.sort()
			seq2=matcher.findAllSeq(l,vectors)
			seq2.sort()
			self.assertEqual(seq,seq2)
		
class TestClosure(unittest.TestCase):
	"Test for checking the functionality of searches of regular expressions with Kleene closure(* and +) operators."
	@classmethod
	def setUpClass(TestClosure):
		file2=open("Test files/Closure.txt",mode="r")
		TestClosure.lines=file2.read().splitlines()
		file2.close()
		
	def test_matches(self):
		for l in self.lines:
			self.assertEqual(matches_proxy(l,text),matcher.matches(l))

	def test_count(self):
		count=0
		for l in self.lines:
			r=re.compile(l)
			for s in sequences:
				if r.search(s) is not None: count+=1
			self.assertEqual(count,matcher.countSeq(l,vectors))
			count=0

	def test_findAllSeq(self):
		for l in self.lines:
			seq=[]
			r=re.compile(l)
			for index in range(len(sequences)):
				if r.search(sequences[index]) is not None: seq.append(index-1)
			seq.sort()
			seq2=matcher.findAllSeq(l,vectors)
			seq2.sort()
			self.assertEqual(seq,seq2)
		
class TestMixture(unittest.TestCase):
	"Test for checking the functionality of searches of regular expressions with all forms of syntax."
	@classmethod
	def setUpClass(TestMixture):
		file2=open("Test files/FinalTest.txt",mode="r")
		TestMixture.lines=file2.read().splitlines()
		file2.close()
		

	def test_matches(self):
		for l in self.lines:
			self.assertEqual(matches_proxy(l,text),matcher.matches(l))

	def test_count(self):
		count=0
		for l in self.lines:
			r=re.compile(l)
			for s in sequences:
				if r.search(s) is not None: count+=1
			self.assertEqual(count,matcher.countSeq(l,vectors))
			count=0

	def test_findAllSeq(self):
		for l in self.lines:
			seq=[]
			r=re.compile(l)
			for index in range(len(sequences)):
				if r.search(sequences[index]) is not None: seq.append(index-1)
			seq.sort()
			seq2=matcher.findAllSeq(l,vectors)
			seq2.sort()
			self.assertEqual(seq,seq2)
		
class TestSampleFile(unittest.TestCase):
	"Test for checking the functionality of searches of regular expressions within the file cMyc.txt"
	@classmethod
	def setUpClass(TestSampleFile):
		file2=open("regex/cMyc.txt",mode="r")
		TestSampleFile.lines=file2.read().splitlines()
		file2.close()


	def test_matches(self):
		for l in self.lines:
			self.assertEqual(matches_proxy(l,text),matcher.matches(l))

	def test_count(self):
		count=0
		for l in self.lines:
			r=re.compile(l)
			for s in sequences:
				if r.search(s) is not None: count+=1
			self.assertEqual(count,matcher.countSeq(l,vectors))
			count=0

	def test_findAllSeq(self):
		for l in self.lines:
			seq=[]
			r=re.compile(l)
			for index in range(len(sequences)):
				if r.search(sequences[index]) is not None: seq.append(index-1)
			seq.sort()
			seq2=matcher.findAllSeq(l,vectors)
			seq2.sort()
			self.assertEqual(seq,seq2)
		

def matches_proxy(pattern,text):
	"A simple method to get the output from the built-in search function to match that of the matches function in TreeMatcher."
	if re.search(pattern,text) is None: return False 
	else: return True

if __name__=='__main__':
	unittest.main()