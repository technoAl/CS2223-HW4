import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Q1 {
	public static void main(String[] args) throws IOException, ParseException {
		SymbolGraph sg = new SymbolGraph("./network_large_directed_multiEdge.json");
		System.out.println(getNeighbors("giCentre", "North America", sg));
	}

	public static List<String> getNeighbors(String node, String continent, SymbolGraph sg){
		List<String> result = new ArrayList<>();
		Graph graph = sg.graph();
		Set<Integer> neighbors = (Set<Integer>) graph.adj(sg.index(node));
		for(int vertice: neighbors){
			System.out.println(sg.continent(sg.name(vertice)));
			if(sg.continent(sg.name(vertice)).equals(continent)){
				result.add(sg.name(vertice));
			}
		}
		return result;
	}

	public static List<String> shortestPath(String start, String end, SymbolGraph sg){
		List<String> result = new ArrayList<>();


		
		return result;
	}
}
