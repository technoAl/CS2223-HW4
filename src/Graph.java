
import java.util.HashSet;
import java.util.Set;

public class Graph {
	private final int numVertices; // vertices
	private int numEdges; // edges
	private Set<Integer>[] adj; // adjacency lists, chose to use sets to prevent double adding an edge as repeats exist in the data
	// also makes looking if an edge exists a little quicker

	public Graph(int V) { // Initialize empty sets and whatnot
		this.numVertices = V;
		this.numEdges = 0;
		adj = (Set<Integer>[]) new Set[V];
		for (int v = 0; v < V; v++)
			adj[v] = new HashSet<Integer>();
	}

	public int getNumVertices() {
		return numVertices;
	}

	public int getNumEdges() {
		return numEdges;
	}

	// Add Edge
	public void addEdge(int v, int w) {
		// Undirected graph so you can add each vertice to each other's adjacency list
		adj[v].add(w);
		adj[w].add(v);
		numEdges++;
	}

	// Return Adjacency Set
	public Set<Integer> adj(int v) {
		return adj[v];
	}
}
