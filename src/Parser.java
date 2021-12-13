import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {
	String pathName;

	public Parser(String pathName) throws IOException, ParseException {
		this.pathName = pathName;
		parse();
	}

	public void parse() throws IOException, ParseException {
		JSONObject jo = (JSONObject) new JSONParser().parse(new FileReader(pathName));

	}

}
