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
 
 import java.util.Scanner;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 class Driver_grephy{
   
   public static void main(String[] args){
     
      Scanner input = new Scanner(System.in);
      
      // Scans through each line of an input file
      while (input.hasNextLine()){
        // String to be scanned to find the pattern.
        String line = input.nextLine();
        
        // regular expression 
        // this one finds strings in alphabet {a,b} where number of a's is odd
        String pattern = "^b*a(b*ab*a)*b*";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(line);
        
        // Checks string on regular expression and prints matches
        if (m.find( )) {
          System.out.println("Found value: " + m.group(0) );
          // System.out.println("Found value: " + m.group(1) );
          // System.out.println("Found value: " + m.group(2) );
        } else {
          System.out.println("NO MATCH");
        }
      }
   }
 }