# grephy
Formal Languages + Computability: Final Project

This is a a version of the grep utility, called "grephy", that searches files for regular expression pattern matches and produces dot graph
file output for the automata used in the matching computation

It creates a NFA based on a given regular expression and converts that NFA to a DFA on which we can compute a given string
String can be accepted or rejected by the DFA based on its states and transition functions called deltas.

You can run this file by:
  1. Open command prompt
  2. cd to directory
  3. compile each file <br />
    javac Driver_grephy.java <br />
    javac NFA.java <br />
    javac DFA.java <br />
  4. Run the program by typing <br />
    java Driver_grephy -n file.dot -d file.dot regex inputfile <br />
    ex: java Driver_grephy -n NFA.dot -d DFA.dot ab* input1.txt <br />

  Other:<br />
    -  the -n file.dot is used to create a new dot file for the NFA <br />
    -  the -d file.dot is used to create a new dot file for the DFA <br />
    -  you do not need to include one or both in the arguments <br />
       ex:  java Driver_grephy -n NFA.dot ab* inpt1.txt <br />
       ex:  java Driver_grephy -d DFA.dot ab* inpt1.txt <br />
       ex:  java Driver_grephy ab* inpt1.txt<br />
    *  It is necessary to have a regex and inputfile or else the application will not run <br />

Examples: <br />

java Driver_grephy -n NFA1.dot -d DFA1.txt ab* input1.txt <br />
java Driver_grephy -n NFA2.dot g+(hi) input2.txt <br />
java Driver_grephy -d DFA3.dot a(stu(vxy)*)b input3.txt <br />
java Driver_grephy r+s* input4.txt <br />
java Driver_grephy -n NFA5.dot -d DFA5.dot mn*(n*(jkl)*)*o input5.txt <br />
java Driver_grephy -d DFA6.dot w*x*y*z* input6.txt <br />
java Driver_grephy -n NFA7.dot -d DFA7.dot a+(b(c)*d) input7.txt <br />


DOT files can be viewed at: <br />
http://www.webgraphviz.com/




