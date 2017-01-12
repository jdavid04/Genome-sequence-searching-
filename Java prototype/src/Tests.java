/*
 * Provides a range of tests to verify the speed of the regex searching methods by comparing 
 * it with the standard java library for searching regular expressions.
 * The path for the files to be used for testing should be specified where it is indicated.
 * 
 * 
 * @author: Julien Horwood
 * 
 * 
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.RandomAccessFile;
class Tests{
	public static void main(String [] args){
	
	SuffixTree tree=new SuffixTree("ACGT");
	TreeBuilder builder=new TreeBuilder(tree);
	StringBuilder sequence=null;
	TreeMatcher matcher=new TreeMatcher(tree);
	StringBuilder text=new StringBuilder();
	int count=0;
	
	//benchmark test
	
	//Create suffix tree for file
	try{
		RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/data/E2f1.fasta", "r");
		String line;
		while((line=in.readLine()) != null){ 
				

				if(line.charAt(0)=='>'){
				if(sequence!=null){
				text.append(sequence.toString());	
				sequence.append(String.format("%05d",count));
				sequence.append("$");
				System.out.println(sequence.toString());
				builder.addToken(sequence.toString());
				
				}
				sequence=new StringBuilder();
				count++;
				continue;
			}
			
			sequence.append(line);
		
			
		}
		text.append(sequence.toString());	
		sequence.append(String.format("%05d",count));
		sequence.append("$");
		System.out.println(sequence.toString());
		builder.addToken(sequence.toString());
		
		in.close(); 
	}catch(IOException e){
		e.printStackTrace();
	}
	System.out.println("Annotating tree..");
	matcher.annotate(106, 3422);
	System.out.println("Annotation complete.");
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * Benchmark Preliminary test
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
		//Matches method
		try{
		int counter=0;
		String line;
		
		while(counter<20){
			RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=in.readLine())!=null){	
			matcher.matches(line);  	
			}
			counter++;
		}

		double currentTime=System.currentTimeMillis();
		counter=0;
		
		while(counter<20){
			RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=in.readLine())!=null){
			matcher.matches(line);  
			}
			counter++;
		}

		System.out.println("Preliminary test Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
		
		
		
		//Test standard algo
		
		counter=0;	
		Pattern pattern;
		Matcher m;
		
			
		while(counter<20){
			RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=reader.readLine()) != null){
			pattern=Pattern.compile(line);
			m=pattern.matcher(text);
			m.find();	
			}	
			counter++;
		}
		counter=0;
		currentTime=System.currentTimeMillis();
		while(counter<20){
			RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=reader.readLine()) != null){
			pattern=Pattern.compile(line);
			m=pattern.matcher(text);
			m.find();	
			}	
			counter++;
		}
		System.out.println("Preliminary test Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
		
		
		//Count method
		
		counter=0;
		int sum=0;
		while(counter<20){
			RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=in.readLine())!=null){	
			matcher.count(line);  	
			}
			counter++;
		}

		currentTime=System.currentTimeMillis();
		counter=0;
		
		while(counter<20){
			RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=in.readLine())!=null){
			sum+=matcher.count(line);  
			}
			counter++;
		}

		System.out.println("Preliminary test Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
		System.out.println("Sequence matches sum: "+sum);
		
		//Test standard algo
		
		counter=0;	
		sum=0;
		
			
		while(counter<20){
			RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				int start=0; int end=100;
				while(end<=text.length()){
					m.region(start, end);
					if(m.find()) sum++;	
					start+=100;
					end+=100;
				}	
			}
			counter++;
		}
		counter=0;
		sum=0;
		currentTime=System.currentTimeMillis();
		while(counter<20){
			RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				int start=0; int end=100;
				while(end<=text.length()){
					m.region(start, end);
					if(m.find()) sum++;	
					start+=100;
					end+=100;
				}	
			}
			counter++;
		}
		System.out.println("Preliminary test Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
		System.out.println("Sequence matches sum: "+sum);
		
		//FindAllSeq method
		

		counter=0;
		while(counter<20){
			RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=in.readLine())!=null){	
			matcher.findAllSeq(line);  	
			}
			counter++;
		}

		currentTime=System.currentTimeMillis();
		counter=0;
		
		while(counter<20){
			RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=in.readLine())!=null){
			matcher.findAllSeq(line);  
			}
			counter++;
		}

		System.out.println("Preliminary test findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
		
		
		//Test standard algo
		
		counter=0;	
		ArrayList<Integer> s=new ArrayList<Integer>(3422);
		while(counter<20){
			RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				int start=0; int end=100;
				while(end<=text.length()){
					m.region(start, end);
					if(m.find()) s.add(start/100);	
					start+=100;
					end+=100;
				}	
			}
			counter++;
		}
		counter=0;
		s=new ArrayList<Integer>(3422);
		currentTime=System.currentTimeMillis();
		
		while(counter<20){
			RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/src/benchmark/regex/cMyc.txt", "r");
			while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				int start=0; int end=100;
				while(end<=text.length()){
					m.region(start, end);
					if(m.find()) s.add(start/100);	
					start+=100;
					end+=100;
				}	
			}
			counter++;
		}
		System.out.println("Preliminary test findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
		
		} catch(IOException e){
			e.printStackTrace();
		}
		
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * Test 1:Solid.txt
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
		
		
		try{
			int counter=0;
			String line;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.matches(line);  	
				}
				counter++;
			}

			double currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=in.readLine())!=null){
				matcher.matches(line);  
				}
				counter++;
			}

			System.out.println("Test 1 Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			
			//Test standard algo
			
			counter=0;	
			Pattern pattern;
			Matcher m;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			counter=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			System.out.println("Test 1 Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Count method
			
			counter=0;
			int sum=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.count(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=in.readLine())!=null){
				sum+=matcher.count(line);  
				}
				counter++;
			}

			System.out.println("Test 1 Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//Test standard algo
			
			counter=0;	
			sum=0;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			sum=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 1 Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//FindAllSeq method
			

			counter=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.findAllSeq(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=in.readLine())!=null){
				matcher.findAllSeq(line);  
				}
				counter++;
			}

			System.out.println("Test 1 findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Test standard algo
			
			counter=0;	
			ArrayList<Integer> s=new ArrayList<Integer>(3422);
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			s=new ArrayList<Integer>(3422);
			currentTime=System.currentTimeMillis();
			
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Solid.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 1 findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			} catch(IOException e){
				e.printStackTrace();
			}
			
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * Test 2:CharClass.txt
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */

		try{
			int counter=0;
			String line;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.matches(line);  	
				}
				counter++;
			}

			double currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=in.readLine())!=null){
				matcher.matches(line);  
				}
				counter++;
			}

			System.out.println("Test 2 Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			
			//Test standard algo
			
			counter=0;	
			Pattern pattern;
			Matcher m;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			counter=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			System.out.println("Test 2 Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Count method
			
			counter=0;
			int sum=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.count(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=in.readLine())!=null){
				sum+=matcher.count(line);  
				}
				counter++;
			}

			System.out.println("Test 2 Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//Test standard algo
			
			counter=0;	
			sum=0;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			sum=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 2 Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//FindAllSeq method
			

			counter=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.findAllSeq(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=in.readLine())!=null){
				matcher.findAllSeq(line);  
				}
				counter++;
			}

			System.out.println("Test 2 findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Test standard algo
			
			counter=0;	
			ArrayList<Integer> s=new ArrayList<Integer>(3422);
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			s=new ArrayList<Integer>(3422);
			currentTime=System.currentTimeMillis();
			
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharacterClass.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 2 findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			} catch(IOException e){
				e.printStackTrace();
			}
		
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * Test 3: CharRange.txt
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		try{
			int counter=0;
			String line;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.matches(line);  	
				}
				counter++;
			}

			double currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=in.readLine())!=null){
				matcher.matches(line);  
				}
				counter++;
			}

			System.out.println("Test 3 Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			
			//Test standard algo
			
			counter=0;	
			Pattern pattern;
			Matcher m;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			counter=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			System.out.println("Test 3 Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Count method
			
			counter=0;
			int sum=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.count(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=in.readLine())!=null){
				sum+=matcher.count(line);  
				}
				counter++;
			}

			System.out.println("Test 3 Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//Test standard algo
			
			counter=0;	
			sum=0;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			sum=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 3 Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//FindAllSeq method
			

			counter=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.findAllSeq(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=in.readLine())!=null){
				matcher.findAllSeq(line);  
				}
				counter++;
			}

			System.out.println("Test 3 findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Test standard algo
			
			counter=0;	
			ArrayList<Integer> s=new ArrayList<Integer>(3422);
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			s=new ArrayList<Integer>(3422);
			currentTime=System.currentTimeMillis();
			
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/CharRange.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 3 findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			} catch(IOException e){
				e.printStackTrace();
			}
			
		
			
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * Test 4: Range.txt
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		
		try{
			int counter=0;
			String line;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.matches(line);  	
				}
				counter++;
			}

			double currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=in.readLine())!=null){
				matcher.matches(line);  
				}
				counter++;
			}

			System.out.println("Test 4 Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			
			//Test standard algo
			
			counter=0;	
			Pattern pattern;
			Matcher m;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			counter=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			System.out.println("Test 4 Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Count method
			
			counter=0;
			int sum=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.count(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=in.readLine())!=null){
				sum+=matcher.count(line);  
				}
				counter++;
			}

			System.out.println("Test 4 Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//Test standard algo
			
			counter=0;	
			sum=0;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			sum=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 4 Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//FindAllSeq method
			

			counter=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.findAllSeq(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=in.readLine())!=null){
				matcher.findAllSeq(line);  
				}
				counter++;
			}

			System.out.println("Test 4 findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Test standard algo
			
			counter=0;	
			ArrayList<Integer> s=new ArrayList<Integer>(3422);
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			s=new ArrayList<Integer>(3422);
			currentTime=System.currentTimeMillis();
			
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Range.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 4 findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			} catch(IOException e){
				e.printStackTrace();
			}
			
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * Test 5: Closure.txt
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */
		

		
		try{
			int counter=0;
			String line;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.matches(line);  	
				}
				counter++;
			}

			double currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=in.readLine())!=null){
				matcher.matches(line);  
				}
				counter++;
			}

			System.out.println("Test 5 Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			
			//Test standard algo
			
			counter=0;	
			Pattern pattern;
			Matcher m;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			counter=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			System.out.println("Test 5 Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Count method
			
			counter=0;
			int sum=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.count(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=in.readLine())!=null){
				sum+=matcher.count(line);  
				}
				counter++;
			}

			System.out.println("Test 5 Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//Test standard algo
			
			counter=0;	
			sum=0;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			sum=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 5 Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//FindAllSeq method
			

			counter=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.findAllSeq(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=in.readLine())!=null){
				matcher.findAllSeq(line);  
				}
				counter++;
			}

			System.out.println("Test 5 findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Test standard algo
			
			counter=0;	
			ArrayList<Integer> s=new ArrayList<Integer>(3422);
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			s=new ArrayList<Integer>(3422);
			currentTime=System.currentTimeMillis();
			
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/Closure.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 5 findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			} catch(IOException e){
				e.printStackTrace();
			}
		
		/*
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * FinalTest.txt
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 */

		
		try{
			int counter=0;
			String line;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.matches(line);  	
				}
				counter++;
			}

			double currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=in.readLine())!=null){
				matcher.matches(line);  
				}
				counter++;
			}

			System.out.println("Test 6 Mixed  Matches-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			
			//Test standard algo
			
			counter=0;	
			Pattern pattern;
			Matcher m;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			counter=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=reader.readLine()) != null){
				pattern=Pattern.compile(line);
				m=pattern.matcher(text);
				m.find();	
				}	
				counter++;
			}
			System.out.println("Test 6 Mixed  Matches-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Count method
			
			counter=0;
			int sum=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.count(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=in.readLine())!=null){
				sum+=matcher.count(line);  
				}
				counter++;
			}

			System.out.println("Test 6 Mixed  Count-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//Test standard algo
			
			counter=0;	
			sum=0;
			
				
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			sum=0;
			currentTime=System.currentTimeMillis();
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) sum++;	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 6 Mixed  Count-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			System.out.println("Sequence matches sum: "+sum);
			
			//FindAllSeq method
			

			counter=0;
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=in.readLine())!=null){	
				matcher.findAllSeq(line);  	
				}
				counter++;
			}

			currentTime=System.currentTimeMillis();
			counter=0;
			
			while(counter<20){
				RandomAccessFile in=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=in.readLine())!=null){
				matcher.findAllSeq(line);  
				}
				counter++;
			}

			System.out.println("Test 6 Mixed  findallSeq-SuffixTree: " +(System.currentTimeMillis()-currentTime)/1000);
			
			
			//Test standard algo
			
			counter=0;	
			ArrayList<Integer> s=new ArrayList<Integer>(3422);
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			counter=0;
			s=new ArrayList<Integer>(3422);
			currentTime=System.currentTimeMillis();
			
			while(counter<20){
				RandomAccessFile reader=new RandomAccessFile("...Insert path here.../Java prototype/Prototype benchmark/FinalTest.txt", "r");
				while((line=reader.readLine()) != null){
					pattern=Pattern.compile(line);
					m=pattern.matcher(text);
					int start=0; int end=100;
					while(end<=text.length()){
						m.region(start, end);
						if(m.find()) s.add(start/100);	
						start+=100;
						end+=100;
					}	
				}
				counter++;
			}
			System.out.println("Test 6 Mixed  findAllSeq-Standard: "+(System.currentTimeMillis()-currentTime)/1000);
			
			} catch(IOException e){
				e.printStackTrace();
			}
	}
}