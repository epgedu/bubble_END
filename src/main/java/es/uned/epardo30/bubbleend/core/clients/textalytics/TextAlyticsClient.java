package es.uned.epardo30.bubbleend.core.clients.textalytics;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TextAlyticsClient {

	static Logger logger = Logger.getLogger(TextAlyticsClient.class);
	
	private Client client;
	private String host;
	private int port;
	private String textalyticsKey;
	private String textAlyticsProtocol;
	private String textAlyticsContext;
	
	public TextAlyticsClient(Client client, String host, int port, String textalyticsKey, String textAlyticsProtocol, String textAlyticsContext) {
		
		logger.debug("Initializing textalytics client...");
		logger.debug("ip textalytics service: "+host);
		logger.debug("port textalytics service: "+port);
		logger.debug("textalytics key: "+textalyticsKey);
		logger.debug("textAlyticsProtocol: "+textAlyticsProtocol);
		logger.debug("textAlyticsContext: "+textAlyticsContext);
		
			
		this.client = client;
		this.host = host;
		this.port = port;
		this.textalyticsKey = textalyticsKey;
		this.textAlyticsContext = textAlyticsContext;
		this.textAlyticsProtocol = textAlyticsProtocol;
	}
	
	public JSONObject getResource(String inputText) {
		
		logger.debug("Google.client.getResource()...");
		
		String url = this.textAlyticsProtocol+"://"+this.host+":"+this.port+this.textAlyticsContext+"?key="+this.textalyticsKey+"&q="+inputText;
		logger.debug("url service textalytics: "+url);
		
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
