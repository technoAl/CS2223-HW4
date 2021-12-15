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

	// Runs a BFS until the target is found, then returns the path from start to end
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

			if(current.currentValue == target && current.distanceFromStart > foundDist){ // Stop if target is found
				break;
			} else if(current.currentValue == target && current.distanceFromStart < foundDist){
				foundDist = current.distanceFromStart;
				paths.add(current);
			} else if(current.currentValue == target){
				paths.add(current);
			}

			// Loop through neighbors
			for(int neighbor: sg.graph().adj(current.currentValue)){

				// If you haven't visited
				if(!visited[neighbor]){
					//System.out.println(sg.name(neighbor));

					// Make sure the node will be visited in the future
					Node neighborNode = new Node(neighbor, current.distanceFromStart + 1);
					neighborNode.path.addAll(current.path);
					neighborNode.path.add(current.currentValue);

					toVisit.add(neighborNode);

					// Set the node as already visited here to prevent later occurrences of this same Node from messing with the path
				}
			}
			//System.out.println();
		}

		if(type == null || type.equals("")) {
			for(int i = 0; i < paths.get(0).path.size(); i++){
				result.add(sg.name(paths.get(0).path.get(i)));
			}
			result.add(end);
			return result;
		}

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

		return result;
	}

	static class Node {
		int currentValue;
		int distanceFromStart;
		ArrayList<Integer> path;
		public Node(int currentValue, int distanceFromStart){
			this.currentValue = currentValue;
			this.distanceFromStart = distanceFromStart;
			path = new ArrayList<>();
		}

	}
}
