/* CMPT 440
 * Final Project
 * Filename: NFA.java
 * Student name: Rafael Marmol
 *
 * Version of a grep utility which searches files for regular expression pattern matches and produces dot graph file
 * output for the automata used in the matching computation.
 *
 * Class: NFA
 * 
 * Builds a Non-Deterministic Finite Automota based on a given regular expression
 */
 
 import java.util.Scanner;
 import java.util.ArrayList;
 import java.util.Stack;
 import java.io.PrintWriter;
 import java.io.File;
 import java.io.FileNotFoundException;
 
class NFA{
  
  private ArrayList<String[]> delta = new ArrayList<String[]>();
  private ArrayList<Integer> states = new ArrayList<Integer>();
  private int startState = 0;
  private int acceptingState;
  private String regex;
  private ArrayList<String> nfa = new ArrayList<String>();
  private String match = "";
  private boolean altPath = false;

  /* NFA
   *  parameters:
   *        r -- String in form of a regex
   *  return value: none
   * 
   *  constructor method for NFA
   */
  NFA(String r){
    startState = 0;
    acceptingState = 0;
    regex = r;
  }
  
  /* getAccepting
   *  parameters: none
   *  return value: int
   * 
   *  returns acceptingState
   */
  public int getAccepting(){
    return acceptingState;
  }
  
  /* getDelta
   *  parameters: none
   *  return value: ArrayList<String[]>
   * 
   *  returns delta
   */
  public ArrayList<String[]> getDelta(){
    return delta;
  }
  
  /* getStates
   *  parameters: none
   *  return value: ArrayList<Integer>
   * 
   *  returns states
   */
  public ArrayList<Integer> getStates(){
    return states;
  }
  
  /* getStates
   *  parameters: none
   *  return value: none
   * 
   *  generatesNFA by setting up states
   */
  public void generateNFA(){
    
    setStates();
    /*for(String[] sA : delta){
      for(String s : sA){
        System.out.print(s + " ");
      }
      System.out.println();
    }*/
    //System.out.println(delta);
    //System.out.println(states);
    //System.out.println(acceptingState);
    
  }
  
  /* getDeltas
   *  parameters: none
   *  return value: ArrayList<String[]>
   * 
   *  returns delta
   */
  public ArrayList<String[]> getDeltas(){
    return delta;
  }
  
  /* getMatch
   *  parameters: none
   *  return value: String
   * 
   *  returns match
   */
  public String getMatch(){
    return match;
  }
  
  /* accepts
   *  parameters:
   *        s -- String to be evaluated
   *  return value: boolean
   * 
   *  computes String s on the NFA  by following its path and
   *  returns true if accepted or runs shorter string or returns
   *  false if false
   */
  public boolean accepts(String s){
    Stack<String> altMatchStack = new Stack<String>();
    Stack<Integer> pathNumStack = new Stack<Integer>();
    char[] digest = s.toCharArray();
    String symbol;
    String state = "0";
    boolean accepts = false;
    match = "";
    ArrayList<String> paths = new ArrayList<String>();
    for(int i = 0; i < digest.length; i++){
      symbol = digest[i] + "";
      paths = getPath(state, symbol);
      //System.out.println("symbol:" + symbol + " path: " + paths);
      if(!paths.isEmpty()){
        state = paths.get(0);

        if(paths.get(1).equals("~")){ //empty transition is used
          i--;
        }
        else{
          match += symbol;
        }
        if(Integer.valueOf(state) == acceptingState){
          accepts = true;
        }
        if(paths.size() > 1){
          altMatchStack.push(match);
          pathNumStack.push(2);
        }
      }
      else{
        if(match.length() > 0){
         //checks if left part still matches
         accepts = accepts(match);
         /*if(accepts == false && altPath == false){
           altPath = true;
           accepts = accepts(match);
         }*/
         //alt path 2
         if(accepts == false && !altMatchStack.isEmpty()){
           altPath = true;
           accepts = accepts(altMatchStack.pop());
         }         
         break;
        }
        else{
          accepts = false;
          break;
        }
        
      }
    }
    return accepts;
  }
  
  /* getPath
   *  parameters:
   *        state -- String denoting the state starting from
   *        symbol -- String symbol moving on from state
   *  return value: ArrayList<Integer>
   * 
   *  returns possible paths from state with symbol
   */
  public ArrayList<String> getPath(String state, String symbol){
    ArrayList<String> paths = new ArrayList<String>();
    String[] arr;
    for(int i = 0; i < delta.size(); i++){
      arr = delta.get(i);
      if(arr[0].equals(state) && (arr[2].equals(symbol) || arr[2].equals("~"))){
        paths.add(arr[1]);
        paths.add(arr[2]);
        
      }
    }
    //System.out.println("paths from state " + state + " is :" + paths);
    //System.out.println(paths);
    return paths;
  }
  
  /* setStates
   *  parameters: none
   *  return value: none
   * 
   *  runs through regex to create states of the NFA
   */
  public void setStates(){
    char symbol;
    char[] regArr = regex.toCharArray();
    states.add(0);
    int lastState = 0;
    int currentState = 0;
    int loopbackState = 0;
    int newState;
    int stateCount = 1;
    Stack<Integer> loopBackStates = new Stack<Integer>();
    Stack<Integer> plusState = new Stack<Integer>();
    for(int i = 0; i < regArr.length; i++){
      symbol = regArr[i];
      lastState = currentState;
      // if + involved and just one other character
      if(!plusState.isEmpty() && i+1 < regArr.length && regArr[i] != '(' && regArr[i+1] != ')' && regArr[i+1] != '*' && regArr[i+1] != '+'){
        loopbackState = plusState.pop();
        setDelta(loopbackState, loopBackStates.pop(), symbol);
        acceptingState = currentState;
        //System.out.println("1");
      }
      // if + involved
      // if ( keep track of state to return to when ) is reached
      else if(!plusState.isEmpty() && symbol == '('){
        /*loopbackState = plusState.pop();
        setDelta(loopbackState, loopBackStates.pop(), symbol);
        acceptingState = currentState;*/
        
        loopbackState = currentState;
        loopBackStates.push(loopbackState);
        //System.out.println("2");
        
      }
      // if + involved
      // if ) then revert back to loopbackstate
      else if(!plusState.isEmpty() && (i+1 < regArr.length && regArr[i+1] == ')') ){
        loopbackState = plusState.pop();
        setDelta(loopbackState, loopBackStates.pop(), symbol);
        acceptingState = currentState;
        
        loopbackState = loopBackStates.pop();
        setDelta(currentState, loopbackState, symbol);
        i+=2;
        currentState = loopbackState;
        acceptingState = currentState;
        if(i<regArr.length && regArr[i] == '+'){
          loopbackState = lastState;
          plusState.push(loopbackState);
          loopBackStates.push(currentState);
          i++;
        }
        else if(regArr[i] != '*' && regArr[i] != ')' && regArr[i] != '+'){
          i--;
        }
        //System.out.println("3");
      }
      // if + involved
      // if ) and * need to loop whole () section
      else if(!plusState.isEmpty() && (symbol == ')' && (i+1 < regArr.length && regArr[i+1] == '*'))){
        loopbackState = plusState.pop();
        setDelta(loopbackState, loopBackStates.pop(), symbol);
        acceptingState = currentState;
        
        loopbackState = loopBackStates.pop();
        // uses '~' as epsilon
        setDelta(currentState, loopbackState, '~');
        i++;
        currentState = loopbackState;
        acceptingState = currentState;
        //System.out.println("4");
      }
      // if + involved
      // if * then loop state
      else if(!plusState.isEmpty() && (i+1 < regArr.length && regArr[i+1] == '*') ){
        loopbackState = plusState.pop();
        setDelta(loopbackState, loopBackStates.pop(), symbol);
        acceptingState = currentState;
        
        loopbackState = lastState; 
        setDelta(currentState, loopbackState, symbol);
        i++;
        currentState = loopbackState;
        acceptingState = currentState;
        //System.out.println("5");
      }
      // if ( keep track of state to return to when ) is reached
      else if(symbol == '('){
        loopbackState = currentState;
        loopBackStates.push(loopbackState);
        //System.out.println("6");
      }
      // if ) then revert back to loopbackstate
      else if(i+1 < regArr.length && regArr[i+1] == ')' ){
        loopbackState = loopBackStates.pop();
        setDelta(currentState, loopbackState, symbol);
        i+=2;
        currentState = loopbackState;
        acceptingState = currentState;
        if(i<regArr.length && regArr[i] == '+'){
          loopbackState = lastState;
          plusState.push(loopbackState);
          loopBackStates.push(currentState);
          i++;
        }
        /* else if(regArr[i] != '*' && regArr[i] != ')' && regArr[i] != '+'){
          i--;
        }*/
        //System.out.println("7");
      }
      // if ) and * need to loop whole () section
      else if(symbol == ')' && (i+1 < regArr.length && regArr[i+1] == '*')){
        loopbackState = loopBackStates.pop();
        // uses '~' as epsilon
        setDelta(currentState, loopbackState, '~');
        i++;
        currentState = loopbackState;
        acceptingState = currentState;
        //System.out.println("8");
      }
      // if * then loop state
      else if(i+1 < regArr.length && regArr[i+1] == '*'){
        loopbackState = lastState; 
        setDelta(currentState, loopbackState, symbol);
        i++;
        currentState = loopbackState;
        acceptingState = currentState;
        //System.out.println("9");
      }
      // if + keep track of state to return to for other side of plus
      else if(i+1 < regArr.length && regArr[i+1] == '+'){
        states.add(stateCount);
        setDelta(lastState, stateCount, symbol);
        currentState = stateCount;
        acceptingState = currentState;
        stateCount++;
        loopbackState = lastState;
        plusState.push(loopbackState);
        loopBackStates.push(currentState);
        i++;
        //System.out.println("10");
      }
      // otherwise just make a new state
      else {
        //make a new state
        states.add(stateCount);
        setDelta(lastState, stateCount, symbol);
        currentState = stateCount;
        acceptingState = currentState;
        stateCount++;
        //System.out.println("11");
      }
    }
  }
  
  /* setDelta
   *  parameters:
   *        currState -- String denoting the state starting from
   *        loopbackState -- String denoting symbol moving to
   *        symbol -- char for transition
   *  return value: none
   * 
   *  creates transition function for given states and symbol
   */
  public void setDelta(int currState, int loopbackState, char symbol){
    String[] newDelta = new String[3];
    boolean alreadyIn = false;
    newDelta[0] = currState + "";
    newDelta[1] = loopbackState + "";
    newDelta[2] = symbol + "";
    for(String[] sA : delta){
      if(sA[0].equals(newDelta[0]) && sA[1].equals(newDelta[1]) && sA[2].equals(newDelta[2])){
        alreadyIn = true;
      }
    }
    if((currState == loopbackState && symbol == '~') || (alreadyIn == true)){
      // if already in deltas then no need to add
    }
    else{
      delta.add(newDelta);
    }
    
  }

  /* toDotNotation
   *  parameters:
   *        fileName -- String for the name of the new file
   *  return value: none
   * 
   *  creates a new file showinf the NFA in the form of dot notation
   */
  public void toDotNotation(String fileName) throws FileNotFoundException{
    //
    try{
      File file = new File(fileName);
      PrintWriter writer = new PrintWriter(fileName);
      writer.println("digraph G{");
      for(String [] sA : delta){
        if(sA[2].equals("~")){
          writer.println("  " + sA[0] + " -> " + sA[1] + " [label=ε];");
        }
        else {
          writer.println("  " + sA[0] + " -> " + sA[1] + " [label=" + sA[2] + "];");
        }
      }
      writer.println("}");
         
      writer.close();
    }
    catch (FileNotFoundException ex){
       System.out.println("ERROR: File Not Found.");
    }
  }
  
  
  //main method for testing purposes
  /*public static void main(String[] args) {
    NFA nfa = new NFA("ab*(b(ab)*)*b*");
    nfa.generateNFA();
    
    //nfa.toDotNotation();
    
   boolean accepts;
   
   Scanner input = new Scanner(System.in);
    while(input.hasNext()){
      accepts = nfa.accepts(input.next());
      if(accepts == true){
        System.out.println("yup");
        System.out.println(nfa.getMatch());
      }
      else{
        System.out.println("nah");
      }
    }
   
    
  }*/
    
  
}
