## CSD 311 ASSIGNMENT 4

### PLANNER USING GOAL STACK TECHNIQUE

#### Programming Language used:** Java

#### Predicates:

ONT: On Table

CL: Clear 

HOLD: Hold

ON: On 

AE: Arm empty

#### Operations:

S: Stack

US: Unstack

PU: Pick up

PD: Put down

#### Algorithms:

We have used regular expressions for string manipulation to extract the predicates and the variables from the input strings for start and goal state. We have then made use of the stack and performed the suitable operations. We have two major classes: the Predicate class and the Planner class. The predicate class is used to keep track of predicates in start state, goal state, current state and to make changes to these predicates according to the required operations. 

The planner class contains the current state, goal state and the steps involved in the conversion from one state to another. 

Functions: 

- void operation(): This is used to perform a particular operation, out of stack, unstack, pick up, and put down
- void changePredicate(): This is used to change the values of the arrays based on the passed state
- Boolean checkGoal(): This is used to check whether the goal has been satisfied completely
- void printCurrentState(): Function to print the current/updated state of our block system after performing a valid step
- void plan(): Function to add and remove goals and predicates to and from the stack to perform the operations in the correct order. This function is taking care of the main logic for the goal stack planner.


**Outputs for given test configurations:** 

- **Start State: ON(Y,X)&ON(W,Z)&ONT(X)&ONT(Z)&CL(Y)&CL(W)&AE**
**![](./screenshots/1.png)**

- **Goal State: ON(Z,X)&ON(Y,W)&ONT(X)&ONT(W)&CL(Z)&CL(Y)&AE**

**![](./screenshots/2.png)**

- **Start State: ON(Y,X)&ON(Z,W)&ONT(X)&ONT(W)&CL(Y)&CL(Z)&AE**
**![](./screenshots/3.png)**

- **Goal State: ON(X,Z)&ON(Y,W)&ONT(Z)&ONT(W)&CL(X)&CL(Y)&AE**

**![](./screenshots/4.png)**

**![](./screenshots/5.png)**

- **Start State: ON(Y,X)&ONT(X)&ONT(Z)&ONT(W)&CL(Y)&CL(Z)&CL(W)&AE**
**![](./screenshots/6.png)**

- **Goal State: ON(X,Z)&ON(W,Y)&ONT(Z)&ONT(Y)&CL(X)&CL(W)&AE**

**![](./screenshots/7.png)**

- **Start State: ON(E,C)&ON(D,B)&ONT(A)&ONT(C)&ONT(B)&CL(A)&CL(E)&CL(D)&AE**
**![](./screenshots/8.png)**

- **Goal State: ON(A,C)&ON(D,B)&ONT(E)&ONT(C)&ONT(B)&CL(E)&CL(A)&CL(D)&AE**
**![](./screenshots/9.png)**


**Contribution:** 

**Prarthna Pahuja** - 1910110283

**Sakshi Mathur** - 1910110336 

**Siddhant Mittal** - 1910110388


*\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_\_*

