# grephy
Formal Languages + Computability: Final Project

This is a a version of the grep utility, called "grephy", that searches files for regular expression pattern matches and produces dot graph
file output for the automata used in the matching computation

It creates a NFA based on a given regular expression and converts that NFA to a DFA on which we can compute a given string
String can be accepted or rejected by the DFA based on its states and transition functions called deltas.

You can run this file by:
  1. Open command prompt
  2. cd to directory
  3. compile each file
    javac Driver_grephy.java
    javac NFA.java
    javac DFA.java
  4. Run the program by typing
    java Driver_grephy -n file.dot -d file.dot regex inputfile
    ex: java Driver_grephy -n NFA.dot -d DFA.dot ab* inpt1.txt

  Other:
    -  the -n file.dot is used to create a new dot file for the NFA
    -  the -d file.dot is used to create a new dot file for the DFA
    -  you do not need to include one or both in the arguments
       ex:  java Driver_grephy -n NFA.dot ab* inpt1.txt
       ex:  java Driver_grephy -d DFA.dot ab* inpt1.txt
       ex:  java Driver_grephy ab* inpt1.txt
    *  It is necessary to have a regex and inputfile or else the application will not run

Examples:



