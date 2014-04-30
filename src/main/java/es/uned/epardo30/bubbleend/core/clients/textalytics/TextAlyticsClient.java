package es.uned.epardo30.bubbleend.core.clients.textalytics;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

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
	private String textAlyticsLanguage;
	
	public TextAlyticsClient(Client client, String host, int port, String textalyticsKey, String textAlyticsProtocol,
							 String textAlyticsContext, String textAlyticsLanguage) {
		
		logger.debug("Initializing textalytics client...");
		logger.debug("ip textalytics service: "+host);
		logger.debug("port textalytics service: "+port);
		logger.debug("textalytics key: "+textalyticsKey);
		logger.debug("textAlyticsProtocol: "+textAlyticsProtocol);
		logger.debug("textAlyticsContext: "+textAlyticsContext);
		logger.debug("textAlyticsLanguage: "+textAlyticsLanguage);
			
		this.client = client;
		this.host = host;
		this.port = port;
		this.textalyticsKey = textalyticsKey;
		this.textAlyticsContext = textAlyticsContext;
		this.textAlyticsProtocol = textAlyticsProtocol;
		this.textAlyticsLanguage = textAlyticsLanguage;
	}
	
	public String getResource(String inputText) throws UnsupportedEncodingException {
		
		logger.debug("Google.client.getResource()...");
		
		String inputTextEncode = URLEncoder.encode(inputText, "UTF-8");
		String url = this.textAlyticsProtocol+"://"+this.host+":"+this.port+this.textAlyticsContext+"?"
				+ "key="+this.textalyticsKey+"&lang="+this.textAlyticsLanguage+"&mode=sa&tt=e&"
				+ "dic=chetsdpqr&of=xml&txt="+inputTextEncode+"&txtf=plain&url=&_tte=e&_ttc=c&dm=5&cont=&ud=";
		
		logger.debug("url service textalytics: "+url);
		
		//process the response
		WebResource webResource = client.resource(url);
		
		ClientResponse response = webResource.type("application/x-www-form-urlencoded").post(ClientResponse.class);
		if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		String output = response.getEntity(String.class);
		logger.debug("Server response ok \n");
		return output;
	}
		
}
