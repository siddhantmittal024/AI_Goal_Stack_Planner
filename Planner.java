import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

//class Predicate is being used to keep track of predicates in start state, goal state, and current state,
//and make changes according to the required operation
class Predicate {

    //ONT is used to keep track of the blocks on the table
    Boolean ONT[];

    //CL is used to keep track of the blocks which don't have anything stacked on top of them
    Boolean CL[];

    //HOLD is used to keep track of which block the robot arm is holding
    Boolean HOLD[];

    //ON is used to check which blocks are stacked on top of which blocks, for any ON[i][j]=true, Block i is on top of Block j
    Boolean ON[][];

    //AE is used to check whether the arm is empty
    Boolean AE;

    //Predicate class constructor
    Predicate(String state) {

        //All the boolean arrays have a size of 26,as we can have blocks varying from A-Z
        ONT = new Boolean[26];
        CL = new Boolean[26];
        HOLD = new Boolean[26];
        ON = new Boolean[26][26];
        AE = Boolean.TRUE;

        //Initializing all the boolean arrays as false
        for (int i = 0; i < 26; i++) {
            ONT[i] = Boolean.FALSE;
        }

        for (int i = 0; i < 26; i++) {
            CL[i] = Boolean.FALSE;
        }

        for (int i = 0; i < 26; i++) {
            HOLD[i] = Boolean.FALSE;
        }

        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                ON[i][j] = Boolean.FALSE;
            }
        }

        //changePredicate() called to pass the input and accordingly change the values of the arrays
        changePredicate(state);
    }


    //operation() is used to perform a particular operation, out of stack, unstack, pick up, and put down
    void operation(String action) {

        //The String action looks something like "S(A,B)", so we need to separate all the required details

        //action is split on the basis of the brackets- arr[0] now stores "S", and arr[1] stores "A,B"
        String arr[] = action.split("[() ]+");


        //comparing arr[0] to "S", "US", "PU", and "PD" to check what is the operation to be performed
        switch (arr[0]) {

            //Stack-
            //First we split "A,B" into "A" and "B", splitting on the basis of the comma
            //Since A is being stacked on top of B, A is no longer on the table
            //B is no longer clear
            //A is clear, since it is stacked on top
            //The arm is not holding block A, and arm is empty
            //A is now on B
            case "S": {
                String arr2[] = arr[1].split("[,]+");
                ONT[arr2[0].charAt(0)-65]=Boolean.FALSE;
                CL[arr2[1].charAt(0) - 65] = Boolean.FALSE;
                CL[arr2[0].charAt(0) - 65] = Boolean.TRUE;
                HOLD[arr2[0].charAt(0) - 65] = Boolean.FALSE;
                ON[arr2[0].charAt(0) - 65][arr2[1].charAt(0) - 65] = Boolean.TRUE;
                AE = Boolean.TRUE;
                break;
            }

            //Unstack-
            //First we split "A,B" into "A" and "B", splitting on the basis of the comma
            //Since A is no longer on top of B, B is now clear
            //B is no longer clear
            //The arm is now holding block A, and arm is not empty
            //A is no longer on B
            case "US": {
                String arr3[] = arr[1].split("[,]+");
                CL[arr3[0].charAt(0) - 65] = Boolean.FALSE;
                CL[arr3[1].charAt(0) - 65] = Boolean.TRUE;
                HOLD[arr3[0].charAt(0) - 65] = Boolean.TRUE;
                ON[arr3[0].charAt(0) - 65][arr3[1].charAt(0) - 65] = Boolean.FALSE;
                AE = Boolean.FALSE;
                break;
            }

            //Pick Up-
            //The block is no longer on the table
            //The arm is now holding the block, and arm is not empty
            case "PU": {
                ONT[arr[1].charAt(0) - 65] = Boolean.FALSE;
                CL[arr[1].charAt(0) - 65] = Boolean.FALSE;
                HOLD[arr[1].charAt(0) - 65] = Boolean.TRUE;
                AE = Boolean.FALSE;
                break;
            }

            //Put Down-
            //The block is now on the table, and it is clear
            //The arm is no longer holding the block, and arm is now empty
            case "PD": {
                ONT[arr[1].charAt(0) - 65] = Boolean.TRUE;
                CL[arr[1].charAt(0) - 65] = Boolean.TRUE;
                HOLD[arr[1].charAt(0) - 65] = Boolean.FALSE;
                AE = Boolean.TRUE;
                break;
            }
            default:
                break;
        }
    }

    //changePredicate() is used to change the values of the arrays based on the passed state
    void changePredicate(String state) {

        //The state is of the form "ON(Z,X)&ON(Y,W)&ONT(X)&ONT(W)&CL(Z)&CL(Y)&AE"
        //So it first needs to be separated on the basis of the '&' symbol,
        //giving us "ON(Z,X)","ON(Y,W)","ONT(X)","ONT(W)","CL(Z)","CL(Y)", and "AE" as subgoals
        String sub_goals[] = state.split("['&']+");

        //Iterating through all these subgoals
        for (String sub : sub_goals) {

            //we first need to split the subgoal to get the predicate and the variables
            //"ON(Z,X)" is split into "ON" and "Z,X"
            String preds[] = sub.split("[() ]+");

            //The predicate is compared with "ONT", "CL", "HOLD", "ON", and "AE",
            //and the arrays are accordingly filled
            switch (preds[0]) {
                case "ONT": {
                    ONT[preds[1].charAt(0) - 65] = Boolean.TRUE;
                    break;
                }
                case "CL": {
                    CL[preds[1].charAt(0) - 65] = Boolean.TRUE;
                    break;
                }
                case "HOLD": {
                    HOLD[preds[1].charAt(0) - 65] = Boolean.TRUE;
                    break;
                }
                case "ON": {
                    //In case on "ON", since there are two variables passed, we need to split it
                    //once again to get the variables individually
                    String preds2[] = preds[1].split("[,]+");
                    ON[preds2[0].charAt(0) - 65][preds2[1].charAt(0) - 65] = Boolean.TRUE;
                    break;
                }
                case "AE": {
                    AE = Boolean.TRUE;
                    break;
                }
                default:
                    break;
            }
        }
    }

    //checkGoal() is used to check whether the goal has been satisfied completely
    Boolean checkGoal(String goal) {

        //The passed goal is split on the basis of '&' first to get the individual predicates
        String sub_goals[] = goal.split("['&']+");

        //We then interate through all these predicates and check if they're true
        //If any of them is false, it means that the goal has not yet been satisfied, and we return a false
        //Otherwise, if we reach the end of the function that means that none of them returned false, so we return true
        //indicating that the goal is satisfied
        for (String sub : sub_goals) {
            String arr[] = sub.split("[() ]+");
            switch (arr[0]) {
                case "ONT": {
                    if (ONT[arr[1].charAt(0) - 65] == true) {
                        continue;
                    } else {
                        return Boolean.FALSE;
                    }

                }

                case "CL": {
                    if (CL[arr[1].charAt(0) - 65] == true) {
                        continue;
                    } else {
                        return Boolean.FALSE;
                    }
                }

                case "HOLD": {
                    if (HOLD[arr[1].charAt(0) - 65] == true) {
                        continue;
                    } else {
                        return Boolean.FALSE;
                    }

                }

                case "ON": {
                    String preds2[] = arr[1].split("[,]+");
                    if (ON[preds2[0].charAt(0) - 65][preds2[1].charAt(0) - 65] == true) {
                        continue;
                    } else {
                        return Boolean.FALSE;
                    }
                }

                case "AE": {
                    if (AE == Boolean.TRUE) {
                        continue;
                    } else {
                        return Boolean.FALSE;
                    }

                }

                default:
                    return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}

//Planner class is used to create the object of planner which contains the current state, goal state and the steps involved to reach the goal state
public class Planner {
    //Stack to store the intermediate steps and operations involved to reach the goal state
    Stack<String> st;
    //Stores the current state and the goal state as objects of the predicate class
    Predicate curr, goal;

    //contructor to initialize the class variables
    public Planner(String start, String Goal) {
        //initializing the current state and goal state as objects of the predicate class
        curr = new Predicate(start);
        goal = new Predicate(Goal);
        // initializing the stack as empty
        st = new Stack<String>();
        //pushing the entire goal state in stack
        st.push(Goal);
    }

    //function to print the current/updated state of our block system after performing a valid step
    public void printCurrentState(Predicate curr) {
        System.out.print("Updated State: ");

        //iterating through all the blocks to check the status of each block

        // - true indicates that the block is on the table
        // - false indicates that the block is not on the table
        // - if true we print that in our current/updated state
        for (int i = 0; i < 26; i++) {
            if (curr.ONT[i] == true) {
                System.out.print("ONT(" + (char) (i + 65) + ")&");
            }
        }

        // - true indicates that the block is clear
        // - false indicates that the block is not clear
        // - if true we print that in our current/updated state
        for (int i = 0; i < 26; i++) {
            if (curr.CL[i] == true) {
                System.out.print("CL(" + (char) (i + 65) + ")&");
            }
        }

        // - true indicates that the block is on the other block
        // - false indicates that the block is not on the other block(not stacked)
        // - if true we print that in our current/updated state
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                if (curr.ON[i][j] == true) {
                    System.out.print("ON(" + (char) (i + 65) + "," + (char) (j + 65) + ")&");
                }
            }

        }

        // - true indicates that the block is on hold
        // - false indicates that the block is not on hold
        // - if true we print that in our current/updated state
        for (int i = 0; i < 26; i++) {
            if (curr.HOLD[i] == true) {
                System.out.print("HOLD(" + (char) (i + 65) + ")");
            }
        }

        if (curr.AE == true) {
            System.out.print("AE");
        }
        System.out.println();
    }

    //Adding and removing goals to and from the stack to perform the operations in the correct order
    public void plan(String Goal) {
        //Making use of string manipulation to extract the subgoals
        String s[] = Goal.split("['&']");
        //Using the for loop to push each subgoal in the stack
        for (int i = s.length - 1; i >= 0; i--) {
            st.push(s[i]);
        }

        //while the stack still contains some subgoals/goal
        while (!st.empty()) {

            //extract the top element of the stack using pop()
            String sub = st.pop();
            //checking if the subgoal contains more than one subgoal
            if (sub.contains("&")) {
                //if this goal is equal to the goalState but it is not yet satisfied, we push it back into the stack
                if (sub.equals(Goal) && !curr.checkGoal(sub)) {
                    st.push(Goal);
                }

                //if this goal is not satisfied, push its subgoals also into the stack, going from last to first subgoal
                if (!curr.checkGoal(sub)) {
                    String goals[] = sub.split("['&']");
                    for (int i = goals.length - 1; i >= 0; i--) {
                        st.push(goals[i]);
                    }
                }

            }
            else{

                //Splitting on the basis of the brackets, and comaring to all possible values
                String arr[] = sub.split("[() ]+");


                switch(arr[0]){

                    //checking if the subgoal contains ONT
                    case "ONT":{

                        //Checking if this subgoal is not yet satisfied
                        if(!curr.checkGoal(sub)){
                            //performing the required operations to satisfy the "ONT" subgoal
                            String s1="PD(" + arr[1].charAt(0) + ")";
                            String s2="HOLD(" + arr[1].charAt(0) + ")";
                            st.push(s1);
                            st.push(s2);
                        }
                        break;
                    }

                    //checking if the subgoal contains CL
                    case "CL":{
                        //Checking if this subgoal is not yet satisfied
                        if(!curr.checkGoal(sub)){
                            //The arm holding the block causes its clear to be false, so we put it down
                            if (curr.HOLD[arr[1].charAt(0) - 65] == true) {
                                String s1="PD(" + arr[1].charAt(0) + ")";
                                String s2="HOLD(" + arr[1].charAt(0) + ")";
                                st.push(s1);
                                st.push(s2);
                            }

                            else {
                                //performing the required operations to satisfy the "CL" subgoal

                                //checking if there is a block on top of the current block
                                char temp='z';
                                for (int i = 0; i < 26; i++) {
                                    if (curr.ON[i][arr[1].charAt(0) - 65] == true) {
                                        temp=(char)(i+65);
                                    }
                                }

                                if (temp != 'z') {
                                    String s1="US(" + Character.toString(temp) + "," + arr[1].charAt(0) + ")";
                                    String s2="ON(" + Character.toString(temp) + "," + arr[1].charAt(0) + ")&CL(" + Character.toString(temp) + ")&AE";
                                    st.push(s1);
                                    st.push(s2);
                                }
                            }
                        }
                        break;
                    }


                    //checking if the subgoal contains HOLD
                    case "HOLD":{
                        //Checking if this subgoal is not yet satisfied
                        if(!curr.checkGoal(sub)){
                            //if the block to be held is on the table
                            if (curr.ONT[arr[1].charAt(0) - 65] == true) {
                                String s1="PU(" + arr[1].charAt(0) + ")";
                                String s2="ONT(" + arr[1].charAt(0) + ")&CL(" + arr[1].charAt(0) + ")&AE";
                                st.push(s1);
                                st.push(s2);
                            }
                            //performing the required operations to satisfy the "HOLD" subgoal
                            else {
                                //checking if there is a block on top of the current block
                                char temp='z';
                                for (int i = 0; i < 26; i++) {
                                    if (curr.ON[i][arr[1].charAt(0) - 65] == true) {
                                        temp=(char)(i+65);
                                    }
                                }
                                
                                if (temp != 'z') {
                                    //checking to see what block is under the current block
                                    char temp2='z';
                                    for (int i = 0; i < 26; i++) {
                                        if (curr.ON[arr[1].charAt(0) - 65][i] == true) {
                                            temp2=(char)(i+65);
                                        }
                                    }
                                    if(temp2!='z'){
                                        //unstacking the block on top and putting it down
                                        //unstacking the current block so that the arm is not holding it
                                        String s1="US(" +arr[1].charAt(0) +","+Character.toString(temp2)+ ")";
                                        String s2="PD(" + Character.toString(temp) + ")";
                                        String s3="US(" + Character.toString(temp) + "," + arr[1].charAt(0) + ")";
                                        String s4="ON(" + Character.toString(temp) + "," + arr[1].charAt(0) + ")&CL(" + Character.toString(temp) + ")&AE";
                                        st.push(s1);
                                        st.push(s2);
                                        st.push(s3);
                                        st.push(s4);
                                    }

                                }
                                else{
                                    //if there is no block on top of the current block, checking what block is under the current block
                                    char temp2='z';
                                    for (int i = 0; i < 26; i++) {
                                        if (curr.ON[arr[1].charAt(0) - 65][i] == true) {
                                            temp2=(char)(i+65);
                                        }
                                    }
                                    if(temp2!='z'){
                                        //unstack the current block so that the arm is now holding it
                                        String s1="US(" +arr[1].charAt(0) + ","+ Character.toString(temp2)+ ")";
                                        String s4="ON(" +arr[1].charAt(0) + "," + Character.toString(temp2) + ")&AE";
                                        st.push(s1);
                                        st.push(s4);
                                    }
                                }
                            }
                        }
                        break;
                    }

                    //checking if the subgoal contains ON
                    case "ON":{
                        //Checking if this subgoal is not yet satisfied
                        if(!curr.checkGoal(sub)){
                            String arr2[] = arr[1].split("[,]+");

                            //performing the required operations to satisfy the "ON" subgoal
                            String s1="S(" + arr2[0].charAt(0) + "," + arr2[1] + ")";
                            String s2="CL(" + arr2[0].charAt(0) + ")&CL(" + arr2[1] + ")&AE";
                            st.push(s1);
                            st.push(s2);
                        }
                        break;
                    }

                    //checking if the subgoal contains AE
                    case "AE":{
                        //Checking if this subgoal is not yet satisfied
                        if(!curr.checkGoal(sub)){
                            for (int i = 0; i < 26; i++) {
                                //If arm is not currently empty and is holding a block
                                if (curr.HOLD[i] == true) {
                                    String s1="PD(" + Character.toString((char) (i + 65)) + ")";
                                    String s2="HOLD(" + Character.toString((char) (i + 65)) + ")";
                                    //performing the required operations to satisfy the "AE" subgoal
                                    st.push(s1);
                                    st.push(s2);
                                }
                            }
                        }
                        break;
                    }

                    //checking if the operation to be performed is pick up
                    case "PU":{
                        //the required operation is performed
                        curr.operation(sub);

                        //The performed operation is printed
                        System.out.println(sub);

                        //The updated current state is printed
                        printCurrentState(curr);
                        System.out.println();
                        break;
                    }

                    //checking if the operation to be performed is put down
                    case "PD":{
                        //the required operation is performed
                        curr.operation(sub);

                        //The performed operation is printed
                        System.out.println(sub);

                        //The updated current state is printed
                        printCurrentState(curr);
                        System.out.println();
                        break;
                    }

                    //checking if the operation to be performed is stack
                    case "S":{
                        //the required operation is performed
                        curr.operation(sub);

                        //The performed operation is printed
                        System.out.println(sub);

                        //The updated current state is printed
                        printCurrentState(curr);
                        System.out.println();
                        break;
                    }

                    //checking if the operation to be performed is unstack
                    case "US":{
                        //the required operation is performed
                        curr.operation(sub);

                        //The performed operation is printed
                        System.out.println(sub);

                        //The updated current state is printed
                        printCurrentState(curr);
                        System.out.println();
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    // main
    public static void main(String[] args) {

        // declaring 2 strings to store the input from the user
        String startState, goalState;
        Scanner sc = new Scanner(System.in);

        // taking start state as input
        System.out.println("Start state : ");
        startState = sc.nextLine();
        // taking goal state as input
        System.out.println("Goal state : ");
        goalState = sc.nextLine();

        // creating object P of class Planner with start and goal states
        Planner p = new Planner(startState, goalState);

        System.out.println();
        System.out.println("Current State: " + startState);

        System.out.println();
        System.out.println("STEPS: ");

        /* calling the plan() function of the class Planner to perform the goal stack planning
         and printing all the steps and updated state after each step */
        p.plan(goalState);

    }
}

//INPUTS:

// ON(Y,X)&ON(W,Z)&ONT(X)&ONT(Z)&CL(Y)&CL(W)&AE
// ON(Z,X)&ON(Y,W)&ONT(X)&ONT(W)&CL(Z)&CL(Y)&AE

// ON(Y,X)&ON(Z,W)&ONT(X)&ONT(W)&CL(Y)&CL(Z)&AE
// ON(X,Z)&ON(Y,W)&ONT(Z)&ONT(W)&CL(X)&CL(Y)&AE

// ON(Y,X)&ONT(X)&ONT(Z)&ONT(W)&CL(Y)&CL(Z)&CL(W)&AE
// ON(X,Z)&ON(W,Y)&ONT(Z)&ONT(Y)&CL(X)&CL(W)&AE

// ON(E,C)&ON(D,B)&ONT(A)&ONT(C)&ONT(B)&CL(A)&CL(E)&CL(D)&AE
// ON(A,C)&ON(D,B)&ONT(E)&ONT(C)&ONT(B)&CL(E)&CL(A)&CL(D)&AE

// ONT(A)&ONT(D)&CL(C)&CL(D)&ON(C,B)&ON(B,A)&AE
// ONT(A)&ONT(B)&ON(C,A)&ON(D,B)&CL(C)&CL(D)&AE

// ON(D,C)&ON(C,B)&ON(B,A)&ONT(A)&CL(D)&AE
// ONT(A)&ONT(B)&ONT(C)&ONT(D)&CL(A)&CL(B)&CL(C)&CL(D)&AE
