import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.*;

public class Q1 {
	public static void main(String[] args) throws IOException, ParseException {
		SymbolGraph sg = new SymbolGraph("./network_large_directed_multiEdge.json");

		System.out.println(getNeighbors("giCentre", "North America", sg));
		System.out.println(shortestPath("Lane Harrison", "Robert Gove", sg, "institution"));
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

	// Runs a BFS until the target is found
	// Stores all paths of the shortest length
	// Picks a path based on a type (looking for institution or not)
	// Otherwise returns the first path found
	public static List<String> shortestPath(String start, String end, SymbolGraph sg, String type){
		List<String> result = new ArrayList<>();
		List<Integer> resultVertices = new ArrayList<>();
		List<Node> paths = new ArrayList<>();

		// Store visited arrays, previous vertice, and a queue
		boolean[] visited = new boolean[sg.graph().getNumVertices()];
		HashSet<Integer>[] prevVertice = new HashSet[sg.graph().getNumVertices()];

		int target = sg.index(end);
		int startValue = sg.index(start);
		Queue<Node> toVisit = new ArrayDeque<>();

		Node startNode = new Node(startValue, 0);
		toVisit.add(startNode);
		//visited[startValue] = true;

		int foundDist = Integer.MAX_VALUE;

		// Run until queue is empty or target is found
		while(!toVisit.isEmpty()){
			Node current = toVisit.remove(); // get current Node
			visited[current.currentValue] = true;

			// Find target
			if(current.currentValue == target && current.distanceFromStart > foundDist){ // End if already have paths shorter than this one
				break;
			} else if(current.currentValue == target && current.distanceFromStart < foundDist){ // Set shortest path length when find the target
				foundDist = current.distanceFromStart;
				paths.add(current);
			} else if(current.currentValue == target){ // If another path matches the shortest length add it to the list of paths
				paths.add(current);
			}

			// Loop through neighbors
			for(int neighbor: sg.graph().adj(current.currentValue)){

				// If you haven't visited
				if(!visited[neighbor]){
					//System.out.println(sg.name(neighbor));

					// Make a new neighbor node and store the path using path of the node it came front
					// Also increase the distance from start by 1
					Node neighborNode = new Node(neighbor, current.distanceFromStart + 1);
					neighborNode.path.addAll(current.path);
					neighborNode.path.add(current.currentValue);

					// Make sure the node will be visited in the future
					toVisit.add(neighborNode);

					// Set the node as already visited here to prevent later occurrences of this same Node from messing with the path
				}
			}
			//System.out.println();
		}

		// If no type, just return the first path found
		if(type == null || type.equals("")) {
			for(int i = 0; i < paths.get(0).path.size(); i++){
				result.add(sg.name(paths.get(0).path.get(i)));
			}
			result.add(end);
			return result;
		}

		// Otherwise look for a path matching the type : "institution" in the example
		for (Node n : paths) {
			boolean typeMatch = true;
			for (int i = 1; i < n.path.size(); i++) {
				if (!sg.type(sg.name(n.path.get(i))).equals(type)) {
					typeMatch = false;
				}
			}
			if(typeMatch){
				for (int i = 0; i < n.path.size(); i++) {
					result.add(sg.name(n.path.get(i)));
				}
				result.add(end);
				return result;
			}
		}

		// return path
		return result;
	}

	// Node class
	static class Node {
		int currentValue; // Vertice it's at
		int distanceFromStart; // Distance out from the start
		ArrayList<Integer> path; // Path of node it took to get there
		public Node(int currentValue, int distanceFromStart){
			this.currentValue = currentValue;
			this.distanceFromStart = distanceFromStart;
			path = new ArrayList<>();
		}

	}
}
