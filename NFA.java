/* CMPT 440
 * Final Project
 * Filename: NFA.java
 * Student name: Rafael Marmol
 *
 * Version of a grep utility which searches files for regular expression pattern matches and produces dot graph file
 * output for the automata used in the matching computation.
 *
 * Class: NFA
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
  
  //private Stack<String> altMatchStack = new Stack<String>();
  //private Stack<Integer> pathNumStack = new Stack<Integer>();
  
  NFA(String r){
    startState = 0;
    acceptingState = 0;
    regex = r;
  }
  
  public int getAccepting(){
    return acceptingState;
  }
  
  public ArrayList<String[]> getDelta(){
    return delta;
  }
  
  public ArrayList<Integer> getStates(){
    return states;
  }
  
  public void generateNFA(){
    
    setStates();
    for(String[] sA : delta){
      for(String s : sA){
        System.out.print(s + " ");
      }
      System.out.println();
    }
    //System.out.println(delta);
    System.out.println(states);
    System.out.println(acceptingState);
    
  }
  
  public ArrayList<String[]> getDeltas(){
    return delta;
  }
  
  public String getMatch(){
    return match;
  }
  
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
         //System.out.println("match");
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
         //
         
         
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
    System.out.println("paths from state " + state + " is :" + paths);
    //System.out.println(paths);
    return paths;
  }
  
  
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
    //Stack<Character> loopBackSymbol = new Stack<Character>();
    for(int i = 0; i < regArr.length; i++){
      symbol = regArr[i];
      lastState = currentState;
      //loopbackState = currentState;
      //loopBackStates.push(loopbackState);
      if(symbol == '('){
        loopbackState = currentState;
        loopBackStates.push(loopbackState);
        //loopBackSymbol.push(regArr[i-1]); //added
      }
      else if(i+1 < regArr.length && regArr[i+1] == ')' ){
        loopbackState = loopBackStates.pop();
        setDelta(currentState, loopbackState, symbol);
        i+=2;
        currentState = loopbackState;
        acceptingState = currentState;
      }
      else if(symbol == ')' && (i+1 < regArr.length && regArr[i+1] == '*')){
        loopbackState = loopBackStates.pop();
        // uses '~' as epsilon
        setDelta(currentState, loopbackState, '~');
        //setDelta(loopbackState, loopbackState, loopBackSymbol.pop());
        i++;
        currentState = loopbackState;
        acceptingState = currentState;
      }
      else if(i+1 < regArr.length && regArr[i+1] == '*'){
        loopbackState = lastState; //loopBackStates.pop();
        setDelta(currentState, loopbackState, symbol);
        //System.out.println("hello");
        i++;
        currentState = loopbackState;
        acceptingState = currentState;
      }
      else {
        //make a new state
        states.add(stateCount);
        setDelta(lastState, stateCount, symbol);
        currentState = stateCount;
        acceptingState = currentState;
        stateCount++;
      }
      
      
      
    }
  }
  
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
    }
    //else if(delta.indexOf(newDelta)
    else{
      //System.out.println("index is: " + delta.indexOf(newDelta));
      delta.add(newDelta);
    }
    
  }

  
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
  
  public static void main(String[] args) {
    NFA nfa = new NFA("a(stu(vxy)*)*b");
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
   
    
  }
  
  
  
  
}
