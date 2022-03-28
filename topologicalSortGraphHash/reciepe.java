//TITLE : Topological Sort  : Arranging tasks according to their dependencies

//SY COMP B
//Roll No:
//2401
//2409
//2415



package recipe;
import java.util.Scanner;



class node 
{
	//data members of class node

	int indegree; //stores the indegree of an action

	String action; //stores the action of a recipe

	int stepno; //stores the step number of an action

	node link;


	node() //default  constructor
	{
		indegree = -1;

		action = "";

		stepno = -1;

		link = null;
	}

	node(node sno) //copy constructor
	{
		indegree =sno.indegree;

		action = sno.action;

		stepno = sno.stepno;

		link = null;
	}

}

class myHash //to the stores the actions according their indegrees (open hashing)
{
	//data members of class myHash

	node heads[] = new node[10]; //array of objects of class node

	int max; //stores the total number of actions

	int count; //acts as counter till total number of actions


	myHash() //default constructor
	{
		count =0;
	}


	protected int hashFunction(int key)// hash function 
	{
		return key % max; // mod by max value
	}



	void hashTable(node temp) //adds the node temp at the head 
	{

		if(heads[temp.indegree] == null)
		{
			heads[temp.indegree] = temp;
		}
		else
		{
			node ptr = heads[temp.indegree];
			//adding a node at the first position in singly linked list

			while(ptr.link != null)
			{
				ptr = ptr.link;
			}

			ptr.link = temp;
		}

	}

}


class Graph extends myHash //class Graph inherits class myHash
{
	//data members of class Graph

	int flag;

	int finalArr[]; //stores the final step number of actions

	int adjMat[][] = new int[20][20]; //(u,v) = 1 if u <- v

	node totSteps[]; //stores the actions

	Graph() //default constructor
	{
		max = 0;
		flag = 0;
	}

	public void accept(Scanner sc, int no) //accepts the actions
	{
		//no is the total number of actions accepted from user

		max = no; 
		finalArr = new int[max]; //size of finalArr[] is set to max
		totSteps = new node[max]; //size of totSteps[] is set to max

		System.out.println();
		System.out.println("Enter the actions :");
		sc.nextLine();
		for(int i=0;i<no;i++) //accepts the actions from user
		{
			System.out.print("Action "+i+" : ");

			totSteps[i] = new node();
			totSteps[i].action = sc.nextLine();
			totSteps[i].stepno = i;

		}

	}


	public void displayActions() //displays the action no and respective action
	{
		System.out.println();
		System.out.println("Action no\tAction");

		for(int i=0 ;i<max;i++) //loop till max
		{
			System.out.print(totSteps[i].stepno +"\t\t"+totSteps[i].action);
			System.out.println();
		}
		System.out.println();
	}


	public void displayDegree() //displays the indegree of action alongwith its action number
	{
		System.out.println("Action no\tAction\t\tIndegree");

		for(int i=0 ;i<max;i++) //loop till max
		{
			System.out.print(totSteps[i].stepno +"\t\t"+totSteps[i].action+"\t\t" +totSteps[i].indegree);
			System.out.println();
		}
	}

	public void preReq(Scanner sc) //accepts the prerequisites of every action OR the actions required to be completed before a particular action
	{

		System.out.println();
		System.out.println("Enter the prerequisites for ");
		System.out.println();

		for(int i = 0 ; i < max ; i++) //loop till total number of actions
		{
			System.out.println();
			System.out.println("ACTION "+i+" : " + totSteps[i].action );
			System.out.println();

			System.out.print("Enter the number of prerequisites = "); //accepts the total actions to be completed before ith action
			int pr = sc.nextInt();

			if(pr == 0)//to check if atleast one of the action has indegree 0(cycle detection)
			{
				flag = 1;
			}

			for(int j = 0 ; j < pr ; j++)//loop till total number of prerequisites
			{
				System.out.print("Enter the action number = ");

				int ano = sc.nextInt();

				adjMat[i][ano] = 1; // (u,v)=1 , if u <- v

			}

			totSteps[i].indegree = pr; //indegree of i action is updated
			this.hashTable(new node(totSteps[i])); //action is added in hash table

			System.out.println();
			this.displayActions(); //indegree of every action is displayed
			System.out.println();

		}

	}


	void updateTable() //implements topological sort and final order of actions is calculated
	{
		//action of in degree 0 is taken out in temp and removed from the list of actions with indegree 0 (heads[0])
		if(flag == 1 && heads[0]!=null)
		{
			node temp = heads[0];


			heads[0] = heads[0].link; 

			temp.link = null;

			finalArr[count] = temp.stepno; //step number of action in temp is stored

			count++; //count is incremented


			for(int i=0;i<max;i++) //loop till max
			{
				//all the indegrees of actions pointed by temp.stepno action is decremented by 1
				if(adjMat[i][temp.stepno] == 1) //action i is pointed by temp.stepno action 
				{
					adjMat[i][temp.stepno] = 0; //we are breaking the link as indegree is decremented

					int deg = hashFunction(totSteps[i].indegree); //original degree is stored in variable deg

					totSteps[i].indegree--; //indegree of action i is decremented


					//action i is deleted from the list of its original indegree deg

					if(heads[deg].stepno == i) //if found at the first position in list with head[deg]
					{
						node p = heads[deg];
						heads[deg] = heads[deg].link;
						p.link = null;
						p.indegree = totSteps[i].indegree;
						this.hashTable(p); //action i is added in hash table with updated indegree
					}
					else //if node of action i is not found the the first position
					{
						node ptr = heads[deg].link;
						node prev = heads[deg]; 


						while(ptr.link != null)
						{

							if(ptr.stepno == i)
							{
								break;
							}

							prev = ptr;
							ptr = ptr.link;

						}

						//deleting action i
						prev.link = ptr.link;				

						ptr.link = null;

						ptr.indegree = totSteps[i].indegree; //indegree is updated

						node temp2 = new node(ptr); //node ptr is copied in node temp2


						this.hashTable(temp2); //action i in temp2 is added in hash table with updated indegree

					}
				}
			}
		}

	}


	void displayARR() //displays the final order of actions to make a recipe
	{
		System.out.println("\n\n");

		System.out.println("ORDERED STEPS FOR THE RECIPE : ");

		for(int i=0;i<finalArr.length;i++) //loop till total number of actions
		{
			System.out.println("\t\t"+totSteps[finalArr[i]].action);

			if( i < finalArr.length-1)
			{
				System.out.println("\t\t  |");

				System.out.println("\t\t  V");

			}
		}
		System.out.println();
	}

}



public class recipe {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);



		System.out.print("Enter the number of actions = "); //accepts the total number of steps to make a recipe

		int no = sc.nextInt();

		Graph g  =  new Graph(); //object of class Graph

		g.accept(sc, no); //accepts all the actions

		g.displayActions();; //displays all the actions

		g.preReq(sc); //accepts the prerequisites of every action

		g.displayDegree();// display the actions with indegree

		for(int i=0;i<no;i++) //loop till total number of steps
		{
			g.updateTable();
		}

		if(g.count!= no)
		{
			System.out.println("\nThe graph is cyclic!!");
			System.out.println("Cannot apply topological sort to cyclic graphs");
		}
		else
		{
			g.displayARR(); //display final order of steps

			System.out.println();
			System.out.println("FOLLOW THE STEPS!!");

		}
	}

}



/*Output 1: Acyclic graph
 * 
 * Enter the number of actions = 9

Enter the actions :
Action 0 : Bake Cake
Action 1 : Preheat Oven
Action 2 : Cool Cake
Action 3 : Frost Cake
Action 4 : Make Frosting
Action 5 : Add batter to pan
Action 6 : Mix ingredients
Action 7 : Eat Dessert:)
Action 8 : Grease/Flour pan 

Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 


Enter the prerequisites for 


ACTION 0 : Bake Cake

Enter the number of prerequisites = 2
Enter the action number = 1
Enter the action number = 5


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 1 : Preheat Oven

Enter the number of prerequisites = 0


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 2 : Cool Cake

Enter the number of prerequisites = 1
Enter the action number = 0


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 3 : Frost Cake

Enter the number of prerequisites = 2
Enter the action number = 2
Enter the action number = 4


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 4 : Make Frosting

Enter the number of prerequisites = 0


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 5 : Add batter to pan

Enter the number of prerequisites = 2
Enter the action number = 6
Enter the action number = 8


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 6 : Mix ingredients

Enter the number of prerequisites = 0


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 7 : Eat Dessert:)

Enter the number of prerequisites = 1
Enter the action number = 3


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 



ACTION 8 : Grease/Flour pan 

Enter the number of prerequisites = 0


Action no	Action
0		Bake Cake
1		Preheat Oven
2		Cool Cake
3		Frost Cake
4		Make Frosting
5		Add batter to pan
6		Mix ingredients
7		Eat Dessert:)
8		Grease/Flour pan 


Action no	Action		     Indegree
0		Bake Cake		      2
1		Preheat Oven		  0
2		Cool Cake		      1
3		Frost Cake		      2
4		Make Frosting		  0
5		Add batter to pan     2
6		Mix ingredients		  0
7		Eat Dessert:)		  1
8		Grease/Flour pan 	  0



ORDERED STEPS FOR THE RECIPE : 
		Preheat Oven
		  |
		  V
		Make Frosting
		  |
		  V
		Mix ingredients
		  |
		  V
		Grease/Flour pan 
		  |
		  V
		Add batter to pan
		  |
		  V
		Bake Cake
		  |
		  V
		Cool Cake
		  |
		  V
		Frost Cake
		  |
		  V
		Eat Dessert:)


FOLLOW THE STEPS!!






Output 2: Cyclic graph


Enter the number of actions = 4

Enter the actions :
Action 0 : a
Action 1 : b
Action 2 : c
Action 3 : d

Action no	Action
0		a
1		b
2		c
3		d


Enter the prerequisites for 


ACTION 0 : a

Enter the number of prerequisites = 1
Enter the action number = 3


Action no	Action
0		a
1		b
2		c
3		d



ACTION 1 : b

Enter the number of prerequisites = 1
Enter the action number = 0


Action no	Action
0		a
1		b
2		c
3		d



ACTION 2 : c

Enter the number of prerequisites = 1
Enter the action number = 1


Action no	Action
0		a
1		b
2		c
3		d



ACTION 3 : d

Enter the number of prerequisites = 1
Enter the action number = 2


Action no	Action
0		a
1		b
2		c
3		d


Action no	Action		Indegree
0		a		1
1		b		1
2		c		1
3		d		1

The graph is cyclic!!
Cannot apply topological sort to cyclic graphs


Output 3: Cyclic grapg with one node with degree Zero

Enter the number of actions = 5

Enter the actions :
Action 0 : a
Action 1 : b
Action 2 : c
Action 3 : d
Action 4 : e

Action no	Action
0		a
1		b
2		c
3		d
4		e


Enter the prerequisites for 


ACTION 0 : a

Enter the number of prerequisites = 1
Enter the action number = 3


Action no	Action
0		a
1		b
2		c
3		d
4		e



ACTION 1 : b

Enter the number of prerequisites = 1
Enter the action number = 0


Action no	Action
0		a
1		b
2		c
3		d
4		e



ACTION 2 : c

Enter the number of prerequisites = 2
Enter the action number = 1
Enter the action number = 4


Action no	Action
0		a
1		b
2		c
3		d
4		e



ACTION 3 : d

Enter the number of prerequisites = 1
Enter the action number = 2


Action no	Action
0		a
1		b
2		c
3		d
4		e



ACTION 4 : e

Enter the number of prerequisites = 0


Action no	Action
0		a
1		b
2		c
3		d
4		e


Action no	Action		Indegree
0		a		1
1		b		1
2		c		2
3		d		1
4		e		0

The graph is cyclic!!
Cannot apply topological sort to cyclic graphs



 */


