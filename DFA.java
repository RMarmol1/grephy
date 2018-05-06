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
  
  DFA(){
    startState = 0;
    //acceptingStates.add(0);
  }
  
  public void printDelta(){
    for(String[] sA : delta){
      for(String s : sA){
        System.out.print(s + " ");
        
      }
      System.out.println();
    }
  }
  
  public void printAccepting(){
    System.out.println(acceptingStates);
  }
  
  public String getMatch(){
    return match;
  }
  
  public void generateFromNFA(NFA nfa){
    String[] dArr;
    int s1;
    int s2;
    char symbol;
    char prevSymbol = '~';
    int sCount = nfa.getStates().size();
    int prevState = 0;
    int newState = 0;
    //System.out.println("sCount:" +sCount);
    int count = 0;
    acceptingStates.add(nfa.getAccepting());
    for(int i = 0; i < nfa.getDelta().size(); i++){
      dArr = nfa.getDelta().get(i);
      s1 = Integer.parseInt(dArr[0]);
      s2 = Integer.parseInt(dArr[1]);
      prevState = s1;
      symbol = dArr[2].charAt(0);
      if(symbol == '~'){
        //System.out.println("AHHHHH");
        //go where epsilon points and copy paste with new states evrything following said states
        // go through and get evrything from to after loopback
        for(int j = 0; j < nfa.getDelta().size(); j++){
          String[] sArr = nfa.getDelta().get(j);
          if(Integer.parseInt(sArr[0]) >= s2 && Integer.parseInt(sArr[0]) <= s1 && !sArr[2].equals("~")){
            //System.out.println("AHHHHH");
            //s1 = sCount;
            //sCount++;
            //prevState = s1;
            if(Integer.parseInt(sArr[0]) == s1 || sArr[2].equals("~")){
              String[] remove = delta.get(delta.size()-1);
              delta.remove(remove);
              newState = s1;
              prevState--;
              symbol = prevSymbol;
              //symbol = sArr[2].charAt(0);
            }
            /*else if(Integer.parseInt(sArr[1]) == s1){
              System.out.println("huh");
              newState = prevState;
              symbol = prevSymbol;
            }*/
            //if(sArr(2).equals("~")){
            //  newState
            //}
            else {
              newState = sCount;
              sCount++;
              symbol = sArr[2].charAt(0);
            }
            
            //symbol = sArr[2].charAt(0);

            if(states.indexOf(s2) == -1){
              states.add(s2);
            }
            setDelta(prevState, newState, symbol);
            prevSymbol = symbol;
            prevState = newState;
          }
          
          //System.out.println(sArr[0]);
          //count++;
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
      //prevState = s1;
      }
    }
    System.out.println(states);
  }
  
  public boolean accepts(String s){
    //Stack<String> altMatchStack = new Stack<String>();
    //Stack<Integer> pathNumStack = new Stack<Integer>();
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

        match += symbol;
        if(acceptingStates.indexOf(Integer.valueOf(state)) > -1){
          accepts = true;
        }
        

        
      }
      else{
        if(match.length() > 0){ 
         accepts = accepts(match);
         if(accepts == false){
          break;
         }
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
      if(arr[0].equals(state) && arr[2].equals(symbol)){
        paths.add(arr[1]);
        paths.add(arr[2]);
        
      }
    }
    System.out.println("paths from state " + state + " is :" + paths);
    //System.out.println(paths);
    return paths;
  }
  
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
  
  public void setDelta(int currState, int loopbackState, char symbol){
    String[] newDelta = new String[3];
    newDelta[0] = currState + "";
    newDelta[1] = loopbackState + "";
    newDelta[2] = symbol + "";
    delta.add(newDelta);
  }
  
  public static void main(String[] args){
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
  }
  
 
  
}