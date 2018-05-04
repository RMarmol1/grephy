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
class NFA{
  
  private ArrayList<String[]> delta = new ArrayList<String[]>();
  private ArrayList<Integer> states = new ArrayList<Integer>();
  private int startState = 0;
  private int acceptingState;
  private String regex;
  private ArrayList<String> nfa = new ArrayList<String>();
  
  NFA(String r){
    startState = 0;
    acceptingState = 0;
    regex = r;
  }
  
  public void generateNFA(){
    //
    //createStates();
    //setAcceptState();
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
  
  /*public void setAcceptState(){
    int t = 0;
    /*while(nfa.get(t) != null && t < nfa.size()){
      if(nfa.get(t+1) == null){
        acceptingState = t;
      }
      t++;
    }
    acceptingState = nfa.size()-1;
    System.out.println("Accepting state is " + acceptingState);
     
  }*/
  
  public boolean accepts(String s){
    char[] digest = s.toCharArray();
    String symbol;
    String state = "0";
    boolean accepts = false;
    ArrayList<String> paths = new ArrayList<String>();
    for(int i = 0; i < digest.length; i++){
      symbol = digest[i] + "";
      paths = getPath(state, symbol);
      System.out.println("symbol:" + symbol + " path: " + paths);
      if(!paths.isEmpty()){
        state = paths.get(0);
        if(true){ //empty transition is used
          i--;
        }
        if(Integer.valueOf(state) == acceptingState){
          accepts = true;
        }
      }
      else{
        accepts = false;
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
        //System.out.println("paths is :" + paths);
      }
    }
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
    for(int i = 0; i < regArr.length; i++){
      symbol = regArr[i];
      lastState = currentState;
      //loopbackState = currentState;
      //loopBackStates.push(loopbackState);
      if(symbol == '('){
        loopbackState = currentState;
        loopBackStates.push(loopbackState);
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
    newDelta[0] = currState + "";
    newDelta[1] = loopbackState + "";
    newDelta[2] = symbol + "";
    //for(String c : newDelta){
    //  System.out.print(c + " ");
    //}
    //System.out.println();
    delta.add(newDelta);
  }

  
  public void toDotNotation(){
    //
  }
  
  public static void main(String[] args){
    NFA nfa = new NFA("ab*(stu(vxy)*)*h");
    nfa.generateNFA();
   // Scanner input = new Scanner(System.in);
   // while(input.hasNext()){
   //   nfa.accepts(input.next());
   // }
   boolean accepts = nfa.accepts("astuh");
   if(accepts == true){
     System.out.println("yup");
   }
   else{
     System.out.println("nah");
   }
    
  }
  
  
  
  
}
