import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class SymbolGraph {
	private String pathName;
	private Graph graph; // Uses IDs
	private ST<String, Vertice> nameToVertice;
	private ST<Long, Vertice> IDToVertice;
	private String[] keys;

	public SymbolGraph(String pathName) throws IOException, ParseException {
		this.pathName = pathName;
		nameToVertice = new ST<>();
		IDToVertice = new ST<>();
		parse();
	}

	public void parse() throws IOException, ParseException {
		JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(pathName));
		JSONArray array = (JSONArray) jo.get("nodes");

		for(int i = 0; i < array.size(); i++){ // For each user, give it a unique vertex number
			JSONObject currentElement = (JSONObject) array.get(i);
			Vertice vertice = new Vertice((String)currentElement.get("name"), (Long)currentElement.get("id"), nameToVertice.getSize(), (String)currentElement.get("continent"));
			if(vertice.continent == null){
				vertice.continent = "";
			}
			nameToVertice.put((String)currentElement.get("name"), vertice);
			IDToVertice.put((Long)currentElement.get("id"), vertice);
			//System.out.println(vertice.V);
		}

		keys = new String[nameToVertice.getSize()];
		Object[] STkeys = (Object[]) nameToVertice.getKeys();
		for(int i = 0; i < STkeys.length; i++){
			if(STkeys[i] == null) continue;
			String key = (String) STkeys[i];
			keys[nameToVertice.get(key).V] = key;
		}

		graph = new Graph(nameToVertice.getSize());

		for(int i = 0; i < array.size(); i++){ // Build the graph
			JSONObject currentElement = (JSONObject) array.get(i);
			JSONArray neighbors = (JSONArray) currentElement.get("neighbors");
			for(Object neighbor:neighbors){
				long neighborID = (Long) neighbor;
				long currentID = (Long) currentElement.get("id");
				graph.addEdge(IDToVertice.get(currentID).V, IDToVertice.get(neighborID).V);
			}
		}
	}

	public boolean contains(String s) {
		return nameToVertice.get(s) != null;
	}

	public int index(String s) {
		return nameToVertice.get(s).V;
	}

	public String continent(String s){
		return nameToVertice.get(s).continent;
	}

	public String name(int v) {
		return keys[v];
	}

	public Graph graph() {
		return graph;
	}

	static class Vertice{
		String name;
		long ID;
		int V; // vertice
		String continent;
		public Vertice(String name, long ID, int V, String continent){
			this.name = name;
			this.ID = ID;
			this.V = V;
			this.continent = continent;
		}
	}

}
