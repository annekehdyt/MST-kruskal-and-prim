import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.time.LocalTime;

/*********************************************************************
 * 
 * Anneke Soraya Hidayat
 * A20406957
 * 
 * History :
 * 	11/16/2017	- Class generated
 *  11/19/2017	- Kruskal() completed
 *  11/23/2017	- Prims() completed
 *  11/25/2017  - Update Arrays
 **********************************************************************/

public class MSTgraph {
	int vCnt;
	int edgeCnt;
	int idx;
	int eNum;
	int i;
	int iter;
	int v_1, v_2;
	Edge[] edge;
	Edge[] kruskalResult;
	Edge[] primResult;
	Edge[] primEdge;
	String[] edge_temp;
	String[] vertices;
	String[] cutset_S;
	String[] cutset_V_S;
	String filePath;
	String[] inputlist;
	DisjointSet[] subsets;
	BinaryHeap primHeap;
	BinaryHeap kruskalHeap;
	Instant kruskalStart;
	Instant kruskalEnd;
	Instant primStart;
	Instant primEnd;
	Duration kruskalDuration;
	Duration primDuration;
	
	/**********************************
	 * This class contains the Constant value for extracting the information
	 * from the input file. (The index for variable)
	 * @author Anne Soraya
	 *
	 **********************************/
	public class Constants {
	    private Constants() { }

	    public static final int VERTICES_NUM   = 0;
	    public static final int VERTICES_SET   = 1;
	    public static final int EDGE_NUM       = 2;
	    public static final int EDGE_SET_START = 3;
	    public static final int V_SOURCE       = 0;
	    public static final int V_DEST         = 1;
	    public static final int E_WEIGHT       = 6; 
	}

	/***********************************
	 * This class contains the Edge representation (Adjacency List) structure
	 * for Kruskal and Prim. Each edge contains the {source vertex, destination vertex, and the edge weight}
	 * a--3--b			{a,b,3}
	 * |\    |			{b,d,1}
	 * 2  \4 1			{a,d,4}
	 * |    \|			{a,c,2}
	 * c--5--d			{c,d,5}
	 * 
	 * @author Anne Soraya
	 ***********************************/
	public class Edge implements Cloneable{
		public String source, dest;
		public int weight;
		
		Edge(){
			this.source = null;
			this.dest = null;
			this.weight = -1;
		}
		
		Edge(String source, String dest, int weight){
			this.source = source;
			this.dest = dest;
			this.weight = weight;
		}
		
		public void addEdgeVal(String source, String dest, int weight) {
			this.source = source;
			this.dest = dest;
			this.weight = weight;
		}
		
		public void removeEdge() {
			source = null;
			dest = null;
			weight = Integer.MAX_VALUE;
		}

		public int getWeight() {
			return this.weight;
		}
		
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
	}
	
	/**********************************************
	 * This class contains the Disjoint Set structure
	 * representation used in the Union-Find algorithm
	 * @author Anne Soraya
	 **********************************************/
	public class DisjointSet {
		int parent;
		int rank;
	
		private DisjointSet(int parent){
			this.parent=parent;
			rank = 0;
		}
	}

	/****************************************************
	 * The BinaryHeap class for Kruskal and Prim Implementation
	 * This class specialize for the MinimumPriorityHeap.
	 * The 0 index of this heap will contained the minimum Edge -> weight value.
	 * 
	 * @author Anne Soraya
	 *
	 ****************************************************/
	public class BinaryHeap{
	    private static final int childNum = 2;
	    ArrayList<Edge> edgeMinHeap;
	   
	    public BinaryHeap(){
	        edgeMinHeap = new ArrayList<Edge>();
	    }
	 
	    public boolean isEmpty( ){
	    	if(edgeMinHeap.size() == 0)
	    		return true;
	    	else
	    		return false;
	    }

	    public void clear( ){
	        edgeMinHeap.clear();
	    }
	 
	    private int parent(int i) {
	        return (i - 1)/childNum;
	    }
	 
	    private int getChild(int parentidx, int childidx) {
	        return childNum * parentidx + childidx;
	    }
	 
	    public void insert(Edge e){
	    	edgeMinHeap.add(e);
	        perc_up(edgeMinHeap.size() - 1);
	        
	    }
	 
	    public Edge findMin( ){       
	        return edgeMinHeap.get(0);
	    }
	 
	    public Edge getDeleteMin(){
	    	Edge keyItem = edgeMinHeap.get(0);
	    	edgeMinHeap.set(0, edgeMinHeap.get(edgeMinHeap.size()-1));
	        perc_down(0);  
	        return keyItem;
	    }
	 
	    public void perc_up(int elementidx){
	    	Edge tmp = new Edge();
	        tmp = edgeMinHeap.get(elementidx);    
	        while (elementidx > 0 && tmp.weight < edgeMinHeap.get(parent(elementidx)).weight){
	            edgeMinHeap.set(elementidx, edgeMinHeap.get(parent(elementidx)));
	            elementidx = parent(elementidx);
	        }  
	        edgeMinHeap.set(elementidx, tmp);
	    }
	 
	    public void perc_down(int elementidx){
	        int child;
	        Edge tmp = edgeMinHeap.get(elementidx);  
	        while (getChild(elementidx, 1) < edgeMinHeap.size()){
	            child = minChild(elementidx);
	            if (edgeMinHeap.get(child).weight < tmp.weight)
	                edgeMinHeap.set(elementidx, edgeMinHeap.get(child));
	            else
	                break;
	            elementidx = child;
	        }
	        edgeMinHeap.set(elementidx, tmp);
	    }
	 
	    private int minChild(int elementidx) {
	        int bestChild = getChild(elementidx, 1);
	        int k = 2;
	        int pos = getChild(elementidx, k);
	        while ((k <= childNum) && (pos < edgeMinHeap.size())) 
	        {
	            if (edgeMinHeap.get(pos).weight < edgeMinHeap.get(bestChild).weight) 
	                bestChild = pos;
	            pos = getChild(elementidx, k++);
	        }    
	        return bestChild;
	    }

	    public void printHeap(){
	        for(Edge e:edgeMinHeap)
	        	System.out.println(e.source + "," + e.dest + " == " + e.weight);
	    }     
	}
	
	/***********************************************
	 * Constructor on the MSTgraph class
	 * 1. Read File
	 * 2. Get the input of Vertices and Edges
	 * 3. Allocate memory for the Edge classes
	 * 
	 * @param filePath	The path of input file
	 ***********************************************/
	MSTgraph(String filePath){
		this.filePath=filePath;
		inputlist = getFile(filePath);
		
		vCnt = Integer.parseInt(inputlist[Constants.VERTICES_NUM]);
		edgeCnt = Integer.parseInt(inputlist[Constants.EDGE_NUM]);
		idx=0;
		//vertices = new Character[vCnt];
		vertices = new String[vCnt];
		
		vertices = inputlist[Constants.VERTICES_SET].trim().split(",");
		
		edge = new Edge[edgeCnt];
		idx=0;
		
		for(int i=Constants.EDGE_SET_START; i<inputlist.length; i++) {
			edge_temp = inputlist[i].substring(1, inputlist[i].indexOf(")")).split(",");
			edge[idx] = new Edge(
					edge_temp[Constants.V_SOURCE],
					edge_temp[Constants.V_DEST], 
					Integer.parseInt(inputlist[i].substring(inputlist[i].indexOf("=")+1)));
			//System.out.println(edge[idx].source + " " + edge[idx].dest + " " + edge[idx].weight);
			idx++;
		}
	}
	
	
	/*********************************
	 * Find the parent root of the given element
	 * in the Disjoint Set
	 * 
	 * @param sets : the whole disjoint sets
	 * @param parent : the index of the self element
	 * @return the root of the element's parent
	 *********************************/
	public int find(DisjointSet sets[], int parent){ 
        if (sets[parent].parent != parent)
            sets[parent].parent = find(sets, sets[parent].parent);
 
        return sets[parent].parent;
    }
	
	/*********************************
	 * Union the both element v_1 and v_2 into one sets
	 * share the same root with the given condition
	 * 
	 * @param sets : the whole disjoint sets
	 * @param v_1
	 * @param v_2
	 ***********************************/
	public void Union(DisjointSet sets[], int v_1, int v_2){
        int v_1root = find(sets, v_1);
        int v_2root = find(sets, v_2);
 
        if (sets[v_1root].rank < sets[v_2root].rank)
            sets[v_1root].parent = v_2root;
        else if (sets[v_1root].rank > sets[v_2root].rank)
            sets[v_2root].parent = v_1root;
        else{
            sets[v_2root].parent = v_1root;
            sets[v_1root].rank++;
        }
    }
	
	
	
	/***************************************
	 * Checks whether the given array contains / return the idx of element
	 * 
	 * @param sets : given array
	 * @param element : element to be checked
	 * @return the index of element or -1 if its not exist
	 */
	public int indexOfElement(String[] sets, String element) {
		int idx=-1;
		
		for(int i=0; i<sets.length; i++) {
			//System.out.println(sets[i]);
			if(sets[i] == null)
				continue;
			if(sets[i].equals(element)) {
				idx=i;
				break;
			}
		}
		
		return idx;
	}
	
	/**************************************************
	 * Remove a particular object (Edge) from an array
	 * and adjust the length of the array
	 * 
	 * @param edges : the array Object
	 * @param tobeRemoved : the object element to be removed
	 * @return the adjusted array Object
	 **************************************************/
	public Edge[] removeEdge(Edge[] edges, Edge tobeRemoved){
	    for (int i=0; i<edges.length; i++){
	        if (edges[i].equals(tobeRemoved)){
	            Edge[] copy = new Edge[edges.length-1];
	            System.arraycopy(edges, 0, copy, 0, i);
	            System.arraycopy(edges, i+1, copy, i, edges.length-i-1);
	            return copy;
	        }
	    }
	    return edges;
	}
	
	/***********************************************
	 * Remove a particular Object (Character) from an array 
	 * and adjust the length of the array 
	 * 
	 * @param vertices : the array Object
	 * @param tobeRemoved : the object element to be removed
	 * @return the adjusted array object
	 **************************************************/
	public String[] removeVertex(String[] vertices, String tobeRemoved){
	    for (int i=0; i<vertices.length; i++){
	        if (vertices[i].equals(tobeRemoved)){
	            String[] copy = new String[vertices.length-1];
	            System.arraycopy(vertices, 0, copy, 0, i);
	            System.arraycopy(vertices, i+1, copy, i, vertices.length-i-1);
	            return copy;
	        }
	    }
	    return vertices;
	}
	
	/*******************************************
	 * Generate the output text file report
	 * 
	 * @param edge	: the MST tree adjacency list
	 * @param filename : the output file name
	 * @param iter : # of iteration in the while loop
	 * @param duration : the time duration during the execution for particular algorithm {Kruskal, Prim}
	 ********************************************/
	public void writeFile(Edge[] edge, String filename, int iter, long duration) {
		
		try {
			FileWriter fileWriter = new FileWriter(filename);		
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			
			bufferedWriter.write(LocalDate.now() + " " + LocalTime.now());
			bufferedWriter.newLine();bufferedWriter.newLine();
			bufferedWriter.write("# edge : " + edge.length+" ");

			bufferedWriter.newLine();
			for(Edge e:edge) {
				bufferedWriter.write("(" + e.source + "," + e.dest + ")=" + e.weight);
				bufferedWriter.newLine();
			}
			bufferedWriter.newLine();
			bufferedWriter.write("Execute Duration : ");
			bufferedWriter.newLine();
			bufferedWriter.write((double)TimeUnit.NANOSECONDS.toMicros(duration)/1000000 + " seconds (in seconds)");
			bufferedWriter.newLine();
			bufferedWriter.write(duration + " ns (in nano seconds)");
			bufferedWriter.newLine();
			bufferedWriter.write("# of iteration (while loop) : " + iter);
			
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**********************************************
	 * Read the input text file and extract the lines and return
	 * 
	 * @param filePath : the input file name
	 * @return the Array of line input
	 **********************************************/
	public String[] getFile(String filePath){
		String[] inputlist;
        String line = null;
        String[] first_third = new String[3];
        int i=0;
		int edgeNum;
        
		try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            for(i=0; i<first_third.length; i++) {
            	if((line = bufferedReader.readLine()) != null){
            		first_third[i] = line;
            		//System.out.println(line);
            	}
            }

            i=3;
            edgeNum = Integer.parseInt(first_third[Constants.EDGE_NUM]);
            inputlist = new String[edgeNum + 3];
            for(int j=0; j<first_third.length; j++)
    			inputlist[j] = first_third[j];
            while((line = bufferedReader.readLine()) != null) {
                	inputlist[i++] = line;
            }

            bufferedReader.close();       
            return inputlist;
        }
        catch(FileNotFoundException ex) {
        	ex.printStackTrace();
            System.out.println(
                "Unable to open file '" + filePath + "'");                
        }
        catch(IOException ex) {
        	ex.printStackTrace();
            System.out.println(
                "Error reading file '" + filePath + "'");                  
        }
		
		return null;
	}
	
	/********************************************
	 * Method for Kruskal.
	 * 1. It uses binaryHeap for sorting the edge in a non-decreasing, each pop would give a minimum weight edge
	 * 2. For each pop, we check whether it formed a cycle or not by using the Union-Find method.
	 * 3. Put the candidate edge to the MST tree in 'kruskalResult'
	 * 4. Discard the edge which formed a cycle
	 * 5. Iterate until the #Edge in 'kruskalResult' < |V|-1
	 * 6. Pass the 'kruskalResult' edge set, the 'fileOut', and the time duration to writeFile for generating the output text file
	 * 
	 * @param fileOut : the name/path for the output file
	 *******************************************/
	public void kruskalMST(String fileOut) {
		
		eNum = 0;
		i = 0;
		iter = 0;
		int edgeidx=0;

		kruskalHeap = new BinaryHeap();
		long startTime = System.nanoTime();
		kruskalStart = Instant.now();
		for(Edge kruskalE:edge)
			kruskalHeap.insert(kruskalE);
		
		subsets = new DisjointSet[vCnt];
		for(int inc=0; inc<vCnt; inc++) {
            subsets[inc] = new DisjointSet(inc);
        }
		
		kruskalResult = new Edge[vCnt-1];
		while (eNum < vCnt-1)
        {
			iter++;
            Edge smallEdge = new Edge();
            smallEdge = kruskalHeap.getDeleteMin();
            
            v_1 = find(subsets, indexOfElement(vertices, smallEdge.source));
            v_2 = find(subsets, indexOfElement(vertices, smallEdge.dest));
              
            if (v_1 != v_2){
                kruskalResult[edgeidx++] = smallEdge;
                eNum++;
                Union(subsets, v_1, v_2);
            }
            
            v_1 = Integer.MIN_VALUE;
            v_2 = Integer.MIN_VALUE;
        }
		kruskalEnd = Instant.now();
		
		//for (Edge res:kruskalResult)
			//System.out.println("(" + res.source +"," + res.dest +")=" + res.weight);
		
		kruskalDuration = Duration.between(kruskalStart, kruskalEnd);
		long difference = System.nanoTime() - startTime;
		writeFile(kruskalResult, fileOut, iter, difference);
	}
	
	/**********************************************
	 * Method for Prim.
	 * 1. Initialize the cutset(S,V-S) {'cutset_S', 'cutset_V_S'}
	 * 2. Initialize binary heap for update the edge in the cutset(S,V-S) for every iteration
	 * 3. Pick the minimum weight edge in the cutset(S,V-S)
	 * 4. Update the vertices {'cutset_S', 'cutset_V_S'}
	 * 5. Iterate until the #Edge in 'primResult' < |V|-1
	 * 6. Pass the 'primResult' edge set, the 'fileOut', and the time duration to writeFile for generating the output text file
	 * 
	 * @param fileOut : the name/path for the output file
	 **********************************************/
	public void primMST(String fileOut) {
		
		iter = 0;
		idx = 0;
		eNum = 0;
		int cutset_Sidx = 0;
		int cut_VSidx = 0;
		int primEcnt = edgeCnt;
		primEdge = new Edge[primEcnt];
		
		long startTime = System.nanoTime();
		for(int i=0; i<primEdge.length; i++)
			primEdge[i] = edge[i];
		
		cutset_S = new String[vCnt];
		cutset_V_S = new String[vCnt-1];
		primHeap = new BinaryHeap();
	
		primStart = Instant.now();
		
		/**************************************************
		 * S <- v_1
		 * V_S <- v_2,...., v_n
		 **************************************************/
		cutset_S[cutset_Sidx++] = vertices[0];
		for(int i=1; i<vertices.length; i++) 
			cutset_V_S[cut_VSidx++] = vertices[i];

		primResult = new Edge[vCnt-1];
		while (eNum < vCnt-1) {
			
			iter++;
			for(Edge primEdge:primEdge) {
				for(String set_S:cutset_S) {
					for(String set_VS:cutset_V_S) {
						if( (primEdge.source.equals(set_S) || primEdge.dest.equals(set_S)) &&
							(primEdge.source.equals(set_VS) || primEdge.dest.equals(set_VS) )) {
							primHeap.insert(primEdge);
							break;
						}	
					}
				}
			}
			
			primResult[eNum] = primHeap.getDeleteMin();
			primEdge = removeEdge(primEdge, primResult[eNum]);
			
			/********************************************
			 * Check which one belongs to the cutset_S and cutset_V_S and update
			 ********************************************/
			
			if(indexOfElement(cutset_S, primResult[eNum].dest) == -1){
				cutset_S[cutset_Sidx++] = primResult[eNum].dest;
				cutset_V_S = removeVertex(cutset_V_S, primResult[eNum].dest);
			}else {
				cutset_S[cutset_Sidx++] = primResult[eNum].source;
				cutset_V_S = removeVertex(cutset_V_S, primResult[eNum].dest);
			}		
			
			primHeap.clear();
			eNum = eNum + 1;
		}
		
		
		//System.out.println("Result");
		//for(Edge res:primResult)
			//System.out.println("(" + res.source +"," + res.dest +")=" + res.weight);
		primEnd = Instant.now();
		long difference = System.nanoTime() - startTime;
		primDuration = Duration.between(primStart, primEnd);
		
		writeFile(primResult, fileOut, iter, difference);
	}
	
	
	/*************************************************
	 * MAIN FUNCTION
	 * PLEASE EXECUTE FROM HERE
	 * 
	 * @param args
	 *************************************************/
	public static void main(String[] args){
		String fileName;
		Scanner scanner = new Scanner( System.in );
		
		System.out.println("Please put the path to the file: " );
		System.out.println("----Example----");
		System.out.println("./[FILENAME] OR ./graph.txt OR /[SUB-FOLDER]/[FILENAME]");

		fileName = scanner.nextLine();
		
		MSTgraph graph = new MSTgraph(fileName);
		graph.kruskalMST("./kruskalout.txt");
		graph.primMST("./primout.txt");

		scanner.close();
	}
}
