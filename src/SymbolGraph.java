import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class SymbolGraph {
	String pathName;
	Graph graph; // Uses IDs
	ST<String, Integer> nameToID;
	String[] keys;

	public SymbolGraph(String pathName) throws IOException, ParseException {
		this.pathName = pathName;
		nameToID = new ST<>();
		parse();
	}

	public void parse() throws IOException, ParseException {
		JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(pathName));
		JSONArray array = (JSONArray) jo.get("nodes");

		for(int i = 0; i < array.size(); i++){ // For each user, give it a unique vertex number
			JSONObject currentElement = (JSONObject) array.get(i);
			nameToID.put((String)currentElement.get("name"), nameToID.getSize());
		}

		keys = new String[nameToID.getSize()];
		Object[] STkeys = (Object[]) nameToID.getKeys();
		for(int i = 0; i < keys.length; i++){
			if(keys[i] == null) continue;
			String key = (String) STkeys[i];
			keys[nameToID.get(key)] = key;
		}

		graph = new Graph(nameToID.getSize());

		for(int i = 0; i < array.size(); i++){ // Build the graph
			JSONObject currentElement = (JSONObject) array.get(i);
			JSONArray neighbors = (JSONArray) currentElement.get("neighbors");
			for(Object neighbor:neighbors){
				int neighborID = (Integer) neighbor;
				int currentID = (Integer) currentElement.get("id");
				graph.addEdge(currentID, neighborID);
			}
		}
	}

	public boolean contains(String s) {
		return nameToID.get(s) != null;
	}

	public int index(String s) {
		return nameToID.get(s);
	}

	public String name(int v) {
		return keys[v];
	}

	public Graph graph() {
		return graph;
	}

}
