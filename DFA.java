/* CMPT 440
 * Final Project
 * Filename: NFA.java
 * Student name: Rafael Marmol
 *
 * Version of a grep utility which searches files for regular expression pattern matches and produces dot graph file
 * output for the automata used in the matching computation.
 *
 * Class: DFA
 *
 * Builds a Deterministic Finite Automata based on a given NFA
 */

import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;

class DFA{
  private ArrayList<String[]> delta = new ArrayList<String[]>();
  private ArrayList<Integer> states = new ArrayList<Integer>();
  private int startState;
  private ArrayList<Integer> acceptingStates = new ArrayList<Integer>();
  private String match;
  private boolean secondCheck = false;
  
  /* DFA
   *  parameters: none
   *  return value: none
   * 
   *  constructor method for DFA
   */
  DFA(){
    startState = 0;
    //acceptingStates.add(0);
  }
  
  /* getMatch
   *  parameters: none
   *  return value: String
   * 
   *  return match
   */
  public String getMatch(){
    return match;
  }
  
  /* generateFromNFA
   *  parameters:
   *        nfa -- NFA to build from
   *  return value: none
   * 
   *  builds the DFA states based on a given DFA
   */
  public void generateFromNFA(NFA nfa){
    String[] dArr;
    int s1;
    int s2;
    char symbol;
    char prevSymbol = '~';
    int sCount = nfa.getStates().size();
    int prevState = 0;
    int newState = 0;
    int count = 0;
    acceptingStates.add(nfa.getAccepting());
    // loop through each delta in the NFA to make new states
    for(int i = 0; i < nfa.getDelta().size(); i++){
      dArr = nfa.getDelta().get(i);
      s1 = Integer.parseInt(dArr[0]);
      s2 = Integer.parseInt(dArr[1]);
      prevState = s1;
      symbol = dArr[2].charAt(0);
      if(symbol == '~'){
        //go where epsilon points and copy paste with new states evrything following said states
        // go through and get evrything from to after loopback
        for(int j = 0; j < nfa.getDelta().size(); j++){
          String[] sArr = nfa.getDelta().get(j);
          if(Integer.parseInt(sArr[0]) >= s2 && Integer.parseInt(sArr[0]) <= s1 && !sArr[2].equals("~")){

            if(Integer.parseInt(sArr[0]) == s1 || sArr[2].equals("~")){
              String[] remove = delta.get(delta.size()-1);
              delta.remove(remove);
              newState = s1;
              prevState--;
              symbol = prevSymbol;
            }

            else {
              newState = sCount;
              sCount++;
              symbol = sArr[2].charAt(0);
            }

            if(states.indexOf(s2) == -1){
              states.add(s2);
            }
            
            setDelta(prevState, newState, symbol);
            prevSymbol = symbol;
            prevState = newState;
          }

        }
        
        acceptingStates.add(newState);
        
      }
      else{
        if(states.indexOf(s1) == -1){
          states.add(s1);
        }
        if(states.indexOf(s2) == -1){
          states.add(s2);
        }
        setDelta(s1, s2, symbol);
        prevSymbol = symbol;
      }
    }
  }
  
  /* accpets
   *  parameters:
   *        s -- String compute on
   *  return value: boolean
   * 
   *  computes s on DFA to see if it accepts it or not
   *  if false will check again for one shorter length string
   *  or just return false
   */
  public boolean accepts(String s){
    char[] digest = s.toCharArray();
    String symbol;
    String state = "0";
    boolean accepts = false;
    match = "";
    ArrayList<String> paths = new ArrayList<String>();
    // if given empty string
    if(digest.length == 0){
      if(acceptingStates.indexOf(Integer.valueOf(state)) > -1){
        accepts = true;
      }
    }
    else{
      // loop through each character and follow its delta through the dfa
      for(int i = 0; i < digest.length; i++){
        symbol = digest[i] + "";
        paths = getPath(state, symbol);
        //System.out.println("symbol:" + symbol + " path: " + paths);
      
        if(!paths.isEmpty()){
          state = paths.get(0);

          match += symbol;
          if(acceptingStates.indexOf(Integer.valueOf(state)) > -1){
            accepts = true;
          }
          
        }
        else{
          accepts = false;
          break;
        }
      }
      // may need to check for smaller length string if fails
      if(match.length() > 0 && secondCheck == false && accepts == false){ 
        secondCheck = true;
        accepts = accepts(match);
      }
      secondCheck = false;
    }
    
    return accepts;
    
  }
  
  /* getPath
   *  parameters:
   *        state -- String denoting start state
   *        symbol -- String denoting symbol transition
   *  return value: ArrayList<String[]>
   * 
   *  returns possible paths from state to another state on symbol
   */
  public ArrayList<String> getPath(String state, String symbol){
    ArrayList<String> paths = new ArrayList<String>();
    String[] arr;
    for(int i = 0; i < delta.size(); i++){
      arr = delta.get(i);
      if(arr[0].equals(state) && arr[2].equals(symbol)){
        paths.add(arr[1]);
        paths.add(arr[2]);
        
      }
    }
    //System.out.println("paths from state " + state + " is :" + paths);
    return paths;
  }
  
  /* toDotNotation
   *  parameters:
   *        fileName -- String name for new file
   *  return value: none
   * 
   *  creats a new dot file to show DFA in dot notation
   */
  public void toDotNotation(String fileName) throws FileNotFoundException{
    //
    try{
      File file = new File(fileName);
      PrintWriter writer = new PrintWriter(fileName);
      writer.println("digraph G{");
      for(String [] sA : delta){
          writer.println("  " + sA[0] + " -> " + sA[1] + " [label=" + sA[2] + "];");
      }
      writer.println("}");
         
      writer.close();
    }
    catch (FileNotFoundException ex){
       System.out.println("ERROR: File Not Found.");
    }
  }
  
  /* setDelta
   *  parameters:
   *        currState -- String denoting start state
   *        loopbackState -- String denoting state moving to
   *        symbol -- char denoting symbol to transition on
   *  return value: none
   * 
   *  creats transition function from start state to new/previous state on symbol
   */
  public void setDelta(int currState, int loopbackState, char symbol){
    String[] newDelta = new String[3];
    newDelta[0] = currState + "";
    newDelta[1] = loopbackState + "";
    newDelta[2] = symbol + "";
    delta.add(newDelta);
  }
  
  //main method for testing
  /*public static void main(String[] args){
    DFA dfa = new DFA();
    NFA nfa = new NFA("a(stu(vxy)*)*b");
    nfa.generateNFA();
    dfa.generateFromNFA(nfa);
    if(dfa.accepts("ab") == true){
      System.out.println("Yes");
    }
    else {
      System.out.println("No");
    }
    dfa.printDelta();
    dfa.printAccepting();
  }*/
  
}