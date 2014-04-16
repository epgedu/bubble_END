package es.uned.epardo30.bubbleend.core.clients.goggle;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


/**
 * Client to google service search
 * 
 * @author eduardo.guillen
 *
 */
public class GoogleClient {
	
	static Logger logger = Logger.getLogger(GoogleClient.class);
	
	private Client client;
	private String host;
	private int port;
	private String googleApiKey;
	private String googleSearchEngineId;
	private String googleResourceProtocol;
	private String googleResourceContext;
	
	
	public GoogleClient(Client client, String host, int port, String googleApiKey, String googleSearchEngineId, String googleResourceProtocol, String googleResourceContext) {
		
		logger.debug("Initializing google client...");
		logger.debug("ip google service: "+host);
		logger.debug("port google service: "+port);
		logger.debug("google api key: "+googleApiKey);
		logger.debug("google search engine: "+googleSearchEngineId);
		logger.debug("protocol google service: "+googleResourceProtocol);
		logger.debug("context google service: "+googleResourceContext);
		
		this.client = client;
		this.host = host;
		this.port = port;
		this.googleApiKey = googleApiKey;
		this.googleSearchEngineId = googleSearchEngineId;
		this.googleResourceProtocol = googleResourceProtocol;
		this.googleResourceContext = googleResourceContext;
	}
	
	public JSONObject getResource(String textSearchFilter) {
		logger.debug("Google.client.getResource()...");
		
		//to change spaces to "+" symbol
		textSearchFilter = textSearchFilter.replace(" ", "+");
		
		String url = this.googleResourceProtocol+"://"+this.host+":"+this.port+this.googleResourceContext+"?key="+this.googleApiKey+"&cx="+this.googleSearchEngineId+"&q="+textSearchFilter;
		logger.debug("url service google: "+url);
		
		//process the response
		WebResource webResource = client.resource(url);
		
		ClientResponse response = webResource.accept("application/json").type("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		String output = response.getEntity(String.class);
		logger.debug("Server response .... \n");
		logger.debug(output);
		
		//process response
		JSONObject jsonObj = new JSONObject(output);
		
		return jsonObj;
	}
}
