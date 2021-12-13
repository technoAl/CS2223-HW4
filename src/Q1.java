import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class Q1 {
	public static void main(String[] args) throws IOException, ParseException {
		SymbolGraph sg = new SymbolGraph("./network_large_directed_multiEdge.json");

		System.out.println(getNeighbors("giCentre", "North America", sg));
		System.out.println(shortestPath("Lane Harrison", "Robert Gove", sg));
	}

	// Gets all neighbors with a matching continent
	public static List<String> getNeighbors(String node, String continent, SymbolGraph sg){
		List<String> result = new ArrayList<>();
		Graph graph = sg.graph();
		Set<Integer> neighbors = (Set<Integer>) graph.adj(sg.index(node));

		// Loops through all neighbors
		for(int vertice: neighbors){
			//System.out.println(sg.continent(sg.name(vertice)));

			// Check Same continent
			if(sg.continent(sg.name(vertice)).equals(continent)){
				result.add(sg.name(vertice));
			}
		}

		return result;
	}

	// Runs a BFS until the target is found, then returns the path from start to end
	public static List<String> shortestPath(String start, String end, SymbolGraph sg){
		List<String> result = new ArrayList<>();

		// Store visited arrays, previous vertice, and a queue
		boolean[] visited = new boolean[sg.graph().getNumVertices()];
		int[] prevVertice = new int[sg.graph().getNumVertices()];
		int target = sg.index(end);

		Queue<Integer> toVisit = new ArrayDeque<>();

		toVisit.add(sg.index(start));
		visited[sg.index(start)] = true;

		// Run until queue is empty or target is found
		while(!toVisit.isEmpty()){
			int current = toVisit.remove(); // get current Node

			if(current == target){ // Stop if target is found
				break;
			}

			// Loop through neighbors
			for(int neighbor: sg.graph().adj(current)){

				// If you haven't visited
				if(!visited[neighbor]){
					//System.out.println(sg.name(neighbor));
					// Set previous node
					prevVertice[neighbor] = current;

					// Make sure the node will be visited in the future
					toVisit.add(neighbor);

					// Set the node as already visited here to prevent later occurrences of this same Node from messing with the path
					visited[neighbor] = true;
				}
			}
			//System.out.println();
		}

		// Run through the previous vertices until we reach the start
		int prev = prevVertice[target];
		result.add(sg.name(sg.index(end)));
		while(prev != sg.index(start)){
			result.add(sg.name(prev));
			prev = prevVertice[prev];
		}
		result.add(sg.name(prev));

		// List is backwards as we added starting at the end
		Collections.reverse(result);

		return result;
	}
}
