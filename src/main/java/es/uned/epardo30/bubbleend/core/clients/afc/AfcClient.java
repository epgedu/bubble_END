package es.uned.epardo30.bubbleend.core.clients.afc;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class AfcClient {
	
	static Logger logger = Logger.getLogger(AfcClient.class);
	
	private Client client;
	private String host;
	private int port;
	private String afcProtocol;
	private String afcContext;
	
	public AfcClient(Client client, String host, int port, String afcProtocol, String afcContext ) {
		
		logger.debug("Initializing afc client...");
		logger.debug("ip afc service: "+host);
		logger.debug("port afc service: "+port);
		logger.debug("protocol afc service: "+afcProtocol);
		logger.debug("context afc service: "+afcProtocol);
		
		this.client = client;
		this.host = host;
		this.port = port;
		this.afcProtocol = afcProtocol;
		this.afcContext = afcContext;
	}
	
	public JSONObject getResource() {
		
		logger.debug("Google.client.getResource()...");
		
		String url = this.afcProtocol+"://"+this.host+":"+this.port+this.afcContext;
		logger.debug("url service afc: "+url);
		
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
