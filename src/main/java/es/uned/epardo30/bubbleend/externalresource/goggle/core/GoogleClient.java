package es.uned.epardo30.bubbleend.externalresource.goggle.core;

import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import es.uned.epardo30.bubbleend.exceptions.DailyLimitExceededGoogleException;


/**
 * Client to call external resource google service search
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
	private String googleSearchEngineIdEng;
	private String googleResourceProtocol;
	private String googleResourceContext;
	
	/**
	 * 
	 * 
	 * @param client : Client JerseyClientBuilder has been set on BubbleEndPointService.run
	 * @param host : IP google service server
	 * @param port : Port google service server
	 * @param googleResourceProtocol : Protocol google service
	 * @param googleResourceContext : Context to google service environment
	 * @param googleApiKey : Developer User Google Key
	 * @param googleSearchEngineId : ID google app engine "bubble-end" 
	 * 
	 * @see es.uned.epardo30.bubbleend.BubbleEndPointService
	 */
	public GoogleClient(Client client, String host, int port, String googleApiKey, String googleSearchEngineId, String googleResourceProtocol, String googleResourceContext, String googleSearchEngineIdEng) {
		
		logger.debug("Initializing google client...");
		logger.debug("ip google service: "+host);
		logger.debug("port google service: "+port);
		logger.debug("google api key: "+googleApiKey);
		logger.debug("google search engine: "+googleSearchEngineId);
		logger.debug("google search engine English: "+googleSearchEngineIdEng);
		logger.debug("protocol google service: "+googleResourceProtocol);
		logger.debug("context google service: "+googleResourceContext);
		
		this.client = client;
		this.host = host;
		this.port = port;
		this.googleApiKey = googleApiKey;
		this.googleSearchEngineId = googleSearchEngineId;
		this.googleResourceProtocol = googleResourceProtocol;
		this.googleResourceContext = googleResourceContext;
		this.googleSearchEngineIdEng = googleSearchEngineIdEng;
	}
	
	/**
	 * 
	 * @param textSearchFilter : The text was inserted by user and it's sent from bubble_GUI
	 * @return JSONObject : Object contains the google results
	 */
	public JSONObject getResource(String textSearchFilter, String languageSearch, int initPage) throws SocketTimeoutException{
		logger.debug("Google.client.getResource()...");
		client.setConnectTimeout(100000);
		client.setReadTimeout(100000);
		
		//to change spaces to "+" symbol
		textSearchFilter = textSearchFilter.replace(" ", "+");
		
		//start result list
		int start = (initPage * 10) + 1;//10 results per query. Always beginning by 1. https://support.google.com/customsearch/answer/1361951?hl=en
		
		//language
		String engineId ="";
		if(languageSearch.equals("spa")) engineId = this.googleSearchEngineId;
		else if(languageSearch.equals("eng")) engineId = this.googleSearchEngineIdEng;
		
		String url = this.googleResourceProtocol+"://"+this.host+":"+this.port+this.googleResourceContext+"?key="+this.googleApiKey+"&cx="+engineId+"&q="+textSearchFilter+"&start="+start;
		logger.debug("url service google: "+url);
		
		//process the response
		WebResource webResource = client.resource(url);
		
		ClientResponse response = webResource.accept("application/json").type("application/json").get(ClientResponse.class);
		if (response.getStatus() != 200) {
			if(response.getStatus() == 403) {
				throw new DailyLimitExceededGoogleException("Daily Limit Exceeded");
			}
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
