/* CMPT 440
 * Final Project
 * Filename: grephy.java
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
  
  /* main
   *  parameters:
   *        args -- arguments from command line
   *  return value: none
   * 
   *  runs main method of program
   */  
  public static void main(String[] args) throws FileNotFoundException {
    
    // variables to hold args from cmd line    
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
       
      // scanners for checking alphabet and another to check each line 
      Scanner inputAlpha = new Scanner(fileIn);
      Scanner inputLineChecker = new Scanner(fileIn);
        
      // create array to hold alphabet of file (max value of # accepted characters (A-Z, a-z, 0-9))
      char[] alphabet = new char[61];
      
      // used to check for duplicates later
      boolean dup = false;
      
      // used to keep place in array
      int count = 0;
      
      //used get each letter into a string
      String fileString = "";
     
      while(inputAlpha.hasNext()){
        fileString += inputAlpha.next();
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
      
      inputAlpha.close();
      
      // display greeting
      System.out.println("************************************************************************");
      System.out.println("***                                                                  ***");
      System.out.println("***                Hello! Welcome to Grep Tool!                      ***");
      System.out.println("***                                                                  ***");
      System.out.println("***    Each line from the file will be evaluated                     ***");
      System.out.println("***    and analyzed against the regex using an NFA and DFA           ***");
      System.out.println("***    If a match is found, console will output: MATCH FOUND         ***");
      System.out.println("***    If a match is not found, console will output: NOT FOUND       ***");
      System.out.println("***    If you wish to cancel this program at any time, hit CTRL+C    ***");
      System.out.println("***                                                                  ***");
      System.out.println("***    Developer: Rafael Marmol                                      ***");
      System.out.println("***                                                                  ***");
      System.out.println("************************************************************************");
      System.out.println();
      System.out.println("Regex to match is:");
      System.out.println(regex);
      System.out.println("Alphabet of file is:");
      System.out.println(alphabet);
      System.out.println();

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
      
      // used to keep track of which line you're on
      int lineNum = 0;
      
      // Scans through each line of an input file
      while (inputLineChecker.hasNextLine()){
        
        // String to be scanned to find the pattern.
        String line = inputLineChecker.nextLine();
        
        
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
        
        //if no illegal characters used, then compute on dfa and display match
        else {
          if(dfa.accepts(line)){
            System.out.println("LINE " + lineNum + ": MATCH FOUND: " + dfa.getMatch());
          }
          else {
            System.out.println("LINE " + lineNum + ": NOT FOUND.");
          }
          lineNum++;
        }

      }
      
      inputLineChecker.close();
    }
    
    // if file not found
    catch (FileNotFoundException ex){
      System.out.println("ERROR: File Not Found.");
    }
  }

 }