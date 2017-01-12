"""
Uses the same set of tests as in Tests.py in order to compare the speed of the TreeMatcher API with the built-in re module.
The file object paths should be specified accordingly to your directory hierarchy.

"""

import unittest,SuffixTree,TreeMatcher,time,re

t=SuffixTree.SuffixTree()
matcher=None
vectors=[]
text=None
sequences=[]
def setUpModule():
	
	global t, text, sequences, matcher, vectors

	file1=open("Research prototype/src/benchmark/data/CTCF.fasta",mode="r")
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
	@classmethod
	def setUpClass(TestSolid):
		file2=open("Java prototype/Prototype benchmark/Solid.txt",mode="r")
		TestSolid.lines=file2.read().splitlines()
		file2.close()

	def test_matches(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.matches(l)
		end=time.time()
		print "TestSolid matches ST: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matches_proxy(l,text)
		end=time.time()
		print "TestSolid matches standard: " + str(end-start)

	def test_count(self):
		start=time.time()
		for x in range(20):
			count=0
			for l in self.lines:
				r=re.compile(l)
				for s in sequences:
					if r.search(s) is not None: count+=1
				count=0
		end=time.time()
		print "TestSolid count standard: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.countSeq(l,vectors)
		end=time.time()
		print "TestSolid count ST: " + str(end-start)

	def test_findAllSeq(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				seq=[]
				r=re.compile(l)
				for index in range(len(sequences)):
					if r.search(sequences[index]) is not None: seq.append(index-1)
		end=time.time()
		print "TestSolid findAllSeq standard: " + str(end-start)


		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.findAllSeq(l,vectors)
		end=time.time()
		print "TestSolid findAllSeq ST: "+ str(end-start)

	
	

class TestCharClass(unittest.TestCase):
	@classmethod
	def setUpClass(TestCharClass):
		file2=open("Java prototype/Prototype benchmark/CharacterClass.txt",mode="r")
		TestCharClass.lines=file2.read().splitlines()
		file2.close()

	def test_matches(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.matches(l)
		end=time.time()
		print "TestCharClass matches ST: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matches_proxy(l,text)
		end=time.time()
		print "TestCharClass matches standard: " + str(end-start)

	def test_count(self):
		start=time.time()
		for x in range(20):
			count=0
			for l in self.lines:
				r=re.compile(l)
				for s in sequences:
					if r.search(s) is not None: count+=1
				count=0
		end=time.time()
		print "TestCharClass count standard: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.countSeq(l,vectors)
		end=time.time()
		print "TestCharClass count ST: " + str(end-start)

	def test_findAllSeq(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				seq=[]
				r=re.compile(l)
				for index in range(len(sequences)):
					if r.search(sequences[index]) is not None: seq.append(index-1)
		end=time.time()
		print "TestCharClass findAllSeq standard: " + str(end-start)


		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.findAllSeq(l,vectors)
		end=time.time()
		print "TestCharClass findAllSeq ST: "+ str(end-start)



		
class TestClosure(unittest.TestCase):
	@classmethod
	def setUpClass(TestClosure):
		file2=open("Java prototype/Prototype benchmark/Closure.txt",mode="r")
		TestClosure.lines=file2.read().splitlines()
		file2.close()
	
	def test_matches(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.matches(l)
		end=time.time()
		print "TestClosure matches ST: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matches_proxy(l,text)
		end=time.time()
		print "TestClosure matches standard: " + str(end-start)

	def test_count(self):
		start=time.time()
		for x in range(20):
			count=0
			for l in self.lines:
				r=re.compile(l)
				for s in sequences:
					if r.search(s) is not None: count+=1
				count=0
		end=time.time()
		print "TestClosure count standard: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.countSeq(l,vectors)
		end=time.time()
		print "TestClosure count ST: " + str(end-start)

	def test_findAllSeq(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				seq=[]
				r=re.compile(l)
				for index in range(len(sequences)):
					if r.search(sequences[index]) is not None: seq.append(index-1)
		end=time.time()
		print "TestClosure findAllSeq standard: " + str(end-start)


		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.findAllSeq(l,vectors)
		end=time.time()
		print "TestClosure findAllSeq ST: "+ str(end-start)
		
class TestMixture(unittest.TestCase):
	@classmethod
	def setUpClass(TestMixture):
		file2=open("Java prototype/Prototype benchmark/FinalTest2.txt",mode="r")
		TestMixture.lines=file2.read().splitlines()
		file2.close()
		
	
	def test_matches(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.matches(l)
		end=time.time()
		print "TestMixture matches ST: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matches_proxy(l,text)
		end=time.time()
		print "TestMixture matches standard: " + str(end-start)

	def test_count(self):
		start=time.time()
		for x in range(20):
			count=0
			for l in self.lines:
				r=re.compile(l)
				for s in sequences:
					if r.search(s) is not None: count+=1
				count=0
		end=time.time()
		print "TestMixture count standard: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.countSeq(l,vectors)
		end=time.time()
		print "TestMixture count ST: " + str(end-start)

	def test_findAllSeq(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				seq=[]
				r=re.compile(l)
				for index in range(len(sequences)):
					if r.search(sequences[index]) is not None: seq.append(index-1)
		end=time.time()
		print "TestMixture findAllSeq standard: " + str(end-start)


		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.findAllSeq(l,vectors)
		end=time.time()
		print "TestMixture findAllSeq ST: "+ str(end-start)
		
class TestSampleFile(unittest.TestCase):
	@classmethod
	def setUpClass(TestSampleFile):
		file2=open("Research prototype/src/benchmark/regex/CTCF.txt",mode="r")
		TestSampleFile.lines=file2.read().splitlines()
		file2.close()

		
	
	def test_matches(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.matches(l)
		end=time.time()
		print "TestSampleFile matches ST: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matches_proxy(l,text)
		end=time.time()
		print "TestSampleFile matches standard: " + str(end-start)

	def test_count(self):
		start=time.time()
		for x in range(20):
			count=0
			for l in self.lines:
				r=re.compile(l)
				for s in sequences:
					if r.search(s) is not None: count+=1
				count=0
		end=time.time()
		print "TestSampleFile count standard: " + str(end-start)

		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.countSeq(l,vectors)
		end=time.time()
		print "TestSampleFile count ST: " + str(end-start)

	def test_findAllSeq(self):
		start=time.time()
		for x in range(20):
			for l in self.lines:
				seq=[]
				r=re.compile(l)
				for index in range(len(sequences)):
					if r.search(sequences[index]) is not None: seq.append(index-1)
		end=time.time()
		print "TestSampleFile findAllSeq standard: " + str(end-start)


		start=time.time()
		for x in range(20):
			for l in self.lines:
				matcher.findAllSeq(l,vectors)
		end=time.time()
		print "TestSampleFile findAllSeq ST: "+ str(end-start)


def matches_proxy(pattern,text):
	if re.search(pattern,text) is None: return False 
	else: return True

if __name__=='__main__':
	unittest.main()