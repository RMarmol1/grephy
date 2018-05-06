/* CMPT 440
 * Final Project
 * Filename: Driver_grephy.java
 * Student name: Rafael Marmol
 *
 * Version of a grep utility which searches files for regular expression pattern matches and produces dot graph file
 * output for the automata used in the matching computation.
 *
 * Class: Driver_grephy
 */
 import java.io.*;
 import java.util.Scanner;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
class Driver_grephy{
   
  public static void main(String[] args) throws FileNotFoundException {
             
    boolean nDot = false;
    boolean dDot = false;
    String dFileName = "";
    String nFileName = "";
    String regex = "";
    String fileName = "";
      
    // Set arguments from cmd line to variables
    if(args[0].equals("-n")){
      nDot = true;
      nFileName = args[1]; 
        
      if(args[2].equals("-d")){
        dDot = true;
        dFileName = args[3];
        regex = args[4];
        fileName = args[5];
      }
      else {
        regex = args[2];
        fileName = args[3];
      }
    }
    else if(args[0].equals("-d")){
      dDot = true;
      dFileName = args[1];
      if(args[2].equals("-n")){
        nDot = true;
        nFileName = args[3];
        regex = args[4];
        fileName = args[5];
      }
      else {
        regex = args[2];
        fileName = args[3];
      }
    }
    else if(!args[0].equals("-n") && !args[0].equals("-d")){
      regex = args[0];
      fileName = args[1];
    }
    else {
      System.out.println("Invalid arguments.");
    }

      
    // work with desired file if found
    File fileIn = new File (fileName);
     
    try{
       
      Scanner input = new Scanner(fileIn);
        
      // create array to hold alphabet of file (max value of # accepted characters (A-Z, a-z, 0-9))
      char[] alphabet = new char[61];
      
      // used to check for duplicates later
      boolean dup = false;
      
      int count = 0;
      String fileString = "";
     
      while(input.hasNext()){
        fileString += input.next();
      }
     
      char[] fileArray = fileString.toCharArray();
      
      // add characters into alphabet
      // check for duplicates in alphabet 
      for(char c : fileArray){
        dup = false;
        for(int i = 0; i < alphabet.length; i++){
          if(c == alphabet[i]){
            dup = true;
          }
        }
        if(dup == false){
          alphabet[count] = c;
          count++;
        }
      }
      
      System.out.println("Hello! Welcome to Grep Tool!");
      System.out.println("Type in a string of characters to see if it matches the regular expression");
      System.out.println("If a match is found, console will output: MATCH FOUND");
      System.out.println("If a match is not found, console will output: NOT FOUND");
      System.out.println("If you wish to cancel this program at any time, hit CTRL+C");
      System.out.println("Regex to match is:");
      System.out.println(regex);
      System.out.println("Alphabet of file is:");
      System.out.println(alphabet);

      // make NFA
      NFA nfa = new NFA(regex);
      nfa.generateNFA();
      
      // make DFA
      DFA dfa = new DFA();
      dfa.generateFromNFA(nfa);
        
      // if dot notation requested for nfa
      if(nDot == true){
        nfa.toDotNotation(nFileName);
      }
      
      // if dot notation requested for dfa
      if(dDot == true){
        dfa.toDotNotation(dFileName);
      }
          
      Scanner input2 = new Scanner(System.in);
      
      System.out.println("Enter a string to be analyzed:");
      
      // Scans through each line of an input file
      while (input2.hasNextLine()){
        
        // String to be scanned to find the pattern.
        String line = input2.nextLine();
        
        
        // check if string contains illegal characters
        char[] lineArr = line.toCharArray();
        int legalCount = 0;
        for(int i = 0; i < alphabet.length; i++){
          for(int t = 0; t < lineArr.length; t++){
            if(lineArr[t] == alphabet[i]){
              legalCount++;
            }
          
          }
        } 
        
        if(legalCount < lineArr.length){
          System.out.println("Sorry, you used an illegal character which is not in the alphabet of the file");
          System.out.println("Alphabet of file is:");
          System.out.println(alphabet);
        }
        else {
          if(nfa.accepts(line)){
            System.out.println("Accepted: " + nfa.getMatch());
          }
          else {
            System.out.println("Rejected.");
          }
        }
        
        System.out.println("Enter a string to be analyzed:");
      }
    }
     
    catch (FileNotFoundException ex){
      System.out.println("ERROR: File Not Found.");
    }
  }

 }