This project was done by Julien Horwood under the supervision of Marcel Turcotte at the University of Ottawa.

The main module of this project is TreeMatcher. It seeks to provide the functionality required to search regular expressions within a suffix tree data structure which must be constructed prior to any search operations.
Therefore, it requires the installation of a specific suffix tree library, found at the following link: https://github.com/JDonner/SuffixTree .
Any questions with respect to the creation and manipulation of suffix trees
through this library should be referred to its own README file. Upon downloading the suffix tree library, there are certain limitations set upon the number of strings added to the tree, the number of characters within each string, as well as upon the number of nodes the tree may contain. One can reallocate the space for these variables within the stree.h header file of this library according to the needs of the user. This must be done before installing the SuffixTree module. 

In addition, TreeMatcher makes use of the bitarray module, which can be downloaded by following this link: https://pypi.python.org/pypi/bitarray/

The TreeMatcher module is intended to be used by instantiating a Matcher object with a suffix tree object. One must then specify the regular expression to be used for searching at each method call. The syntax supported for regular expressions is the following:

- Any solid character within the alphabet (defaulted to “ACGT”)
- Simple character classes, for example [ACG]
- Kleene closure operators * and +
- The ? operator
- The | operator
- The don’t care operator, represented by .

The TreeMatcher module has three main methods with which searching can be done:

 - matches takes as input a properly written regular expression and returns
True if the regular expression is found in the suffix tree. Otherwise, it returns false. Here, properly written refers to the fact that all precedence and grouping must be explicitly represented with parentheses. For example,
ab* should be written as a(b*).

- countSeq takes as input a properly written regular expression and a list of  bitarrays and returns the number of sequences in the tree containing a match.
The list of bitarrays should be the result of a previous call to the annotate method of the Matcher class and greatly increases the speed of the operation.
It should be noted that, in general, only one call should be made to the annotate method after creation of the suffix tree. As it is a time consuming task, it is much more efficient to call annotate once and then performing multiple search operations using the same matcher object.

- findAllSeq behaves similarly to countSeq in that it takes the same parameters (and thus has the same previous annotation requirements), but returns the list of indices corresponding to the sequences in the tree containing a match.



