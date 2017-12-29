# MST-kruskal-and-prim

-------------------------------------
## PROJECT DESCRIPTION
-------------------------------------
THIS PROJECT AIMED FOR THE COMPLETION OF INTRODUCTION OF ALGORITHM CLASS CS430 FALL 2017.
THIS PROJECT IMPLEMENTS AND COMPARE BETWEEN TWO MST (MINIMUM SPANNING TREE):
   1. KRUSKAL'S WITH UNION-FIND
   2. PRIM'S WITH HEAP

------------------------------------
## ENVIRONMENT
------------------------------------
java version "1.8.0_131"
Java(TM) SE Runtime Environment (build 1.8.0_131-b11)
Java HotSpot(TM) 64-Bit Server VM (build 25.131-b11, mixed mode)

-----------------------------------
## JAVA CLASS HIERARCHICAL
-----------------------------------
All the classes are build in nested classes under MSTgraph class. 

MSTgraph
--int find(DisjointSet sets[], int parent)
--void Union(DisjointSet sets[], int v_1, int v_2)
--int indexOfElement(String[] sets, String element)
--Edge[] removeEdge(Edge[] edges, Edge tobeRemoved)
--String[] removeVertex(String[] vertices, String tobeRemoved)
--writeFile(Edge[] edge, String filename, int iter, long duration)
--String[] getFile(String filePath)
--void kruskalMST(String fileOut)
--void primMST(String fileOut)
--void main(String[] args)
Constants
Edge
--void addEdgeVal(String source, String dest, int weight)
--void removeEdge()
--int getWeight()
--Object clone()
DisjointSet
BinaryHeap
--boolean isEmpty()
--void clear()
--int parent(int i)
--getChild(int parentidx, int childidx)
--insert(Edge e)
--Edge findMin()
--Edge getDeleteMin()
--void perc_up(int elementidx)
--void perc_down(int elementidx)
--int minChild(int elementedx)
--void printHeap()

------------------------------------------------
## RUNNING THE PROGRAM INSTRUCTION
------------------------------------------------
INPUT FILE : input each line by this format
[# of Vertices (NON-NEGATIVE INTEGER)]
[SETS of VERTICES (STRING) separate with comma EXAMPLE : a,b,cc,d,e,ff,g]
[# of Edges (NON-NEGATIVE INTEGER)]
[ADJACENCY LIST each line counts as one edge EXAMPLE : (a,b)=13 (STRING, NO SPACE IN BETWEEN)]

TO EXECUTE USING CMD PROMPT :
> java -jar MSTgraph.jar
> ----Example----
> ./[FILENAME] OR ./graph.txt OR /[SUB-FOLDER]/[FILENAME]
> [put your input file]

OUTPUT FILE : Default
Prims output file : primout.txt
Kruskal output file : kruskalout.txt

You can modify the output file name by go to the main method under MSTgraph class, and put the string fileOut in each void method below:
graph.kruskalMST("./kruskalout.txt");
graph.primMST("./primout.txt");


-----------------------------------------------------------



Example:
Graph.txt
____________________________________
9
a,b,c,d,e,f,g,h,i
14
(a,b)=4
(b,c)=8
(c,d)=7
(d,e)=9
(e,f)=10
(f,g)=2
(g,h)=1
(h,a)=8
(b,h)=11
(h,i)=7
(c,i)=2
(i,g)=6
(c,f)=4
(d,f)=14
____________________________________

kruskal out
------------------------------------
2017-11-26 15:33:05.738

# edge : 8 
(g,h)=1
(c,i)=2
(f,g)=2
(c,f)=4
(a,b)=4
(c,d)=7
(h,a)=8
(d,e)=9

Execute Duration : 
0.00406 seconds (in seconds)
4060843 ns (in nano seconds)
# of iteration (while loop) : 11

------------------------------------
primOut
------------------------------------
2017-11-26 15:33:05.742

# edge : 8 
(a,b)=4
(b,c)=8
(c,i)=2
(c,f)=4
