package es.uned.epardo30.bubbleend.externalresource.afc.core;

import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * Afc client to call the external resource AFC services web.
 * 
 * @author eduardo.guillen
 *
 */
public class AfcClient {
	
	static Logger logger = Logger.getLogger(AfcClient.class);
	
	private Client client;
	private String host;
	private int port;
	private String afcProtocol;
	private String afcContext;
	
	/**
	 * /**
	 * 
	 * @param client : Client JerseyClientBuilder has been set on BubbleEndPointService.run 
	 * @param host : IP afc service server
	 * @param port : Port afc service server
	 * @param afcProtocol : Protocol afc service
	 * @param afcContext : Context to afc service environment
	 * 
	 * @see es.uned.epardo30.bubbleend.BubbleEndPointService
	 */
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
	
	/**
	 * 
	 * @param entranceAfc : The String param contains the xml entrance to afc service 
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public String getResource(String entranceAfc) throws UnsupportedEncodingException, SocketTimeoutException {
		
		logger.debug("AfcClient.getResource()...");
		logger.debug("Xml sent to afc service: "+entranceAfc);
		
		String entranceAfcEncode = URLEncoder.encode(entranceAfc, "UTF-8");
		
		String url = this.afcProtocol+"://"+this.host+":"+this.port+this.afcContext+"?ficheroXMLEntrada="+entranceAfcEncode;
		logger.debug("url service afc: "+entranceAfc);
		//process the response
		WebResource webResource = client.resource(url);
		
		ClientResponse response = webResource.type("application/x-www-form-urlencoded").get(ClientResponse.class);
		if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "+ response.getStatus());
		}
		String output = response.getEntity(String.class);
		logger.debug("Server response .... \n");
		logger.debug(output);
		
		return output;
	}
	
	
}
