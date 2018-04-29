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
class NFA{
  
  private int[][][] delta;
  private int[] states;
  private int startState;
  private int acceptingState;
  private String regex;
  
  NFA(String r){
    startState = 0;
    acceptingState = 0;
    regex = r;
  }
  
  public void generateNFA(){
    //
    
  }
  
  public boolean accepts(){
    return true;
  }
  
  public void createDelta(){
    char[] regArr = regex.toCharArray();
    states = new int[regArr.length];
    states = new int[] {0};
    int numStates = 1;
    for(int i = 0; i < regArr.length; i ++){
      if (regArr[i] == '('){
      }
      if(regArr[i] != '*' || regArr[i] != '^' || regArr[i] != '$' ){
         numStates++;
      }
      
    }
    
    
  }
  
  public void toDotNotation(){
    //
  }
  
  
  
  
}