package es.uned.epardo30.bubbleend.core;

import java.io.IOException;

import org.apache.log4j.Logger;

import es.uned.epardo30.bubbleend.BubbleEndPointService;
import es.uned.epardo30.bubbleend.exceptions.InternalServerException;


public class BubbleEngine {
	
	static Logger logger = Logger.getLogger(BubbleEngine.class);
	
	public void workflowEngine() {
		//llamaremos al local method callToGoogleClient
		//este nos devuelve un xml el cual tiene que ser procesadoo para obtener los descriptores y devuelve la entrada al AFC
		//llamamos al local method callToAfcClient
		//llamamos al metodo que procesa el XML de salida del AFC para quedarnos con la informacion que necesitamos y contruir el objeto Searching,
		//que es el objeto JSON que pasaremos a la interface. 
		
		
			this.callToGoogleClient();
			this.processGoogleOut();
			this.callTextalyticsProcess();
			this.createAfcIn();
			this.callToAfcClient();
			this.proccessAfcOut();
		
	}
	
	private void callToGoogleClient() {
		//llamaremos al servicio resources.GoogleClient
		try {
			//int i = 6/0;
		}
		catch(Exception exception) {
			logger.error("Exception on callToGoogleClient() method: ", exception);
			throw new InternalServerException("Exception on service google: "+exception.getMessage());
		}
	
	}
	
	
	private void processGoogleOut() {
		//procesamos el xml de salida del servicio de busqueda, para obtener los descriptores. Devuelve el xml de entrada a AFC
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on processGoogleOut() method: ", exception);
			throw new InternalServerException("Exception on bubble engine processing: "+exception.getMessage());
		}
	}
	
	public void callTextalyticsProcess() {
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on callTextalyticsProcess() method: ", exception);
			throw new InternalServerException("Exception on Textalytics process: "+exception.getMessage());
		}
	}
	
	public void createAfcIn() {
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on createAfcIn() method: ", exception);
			throw new InternalServerException("Exception on bubble engine creating afc in: "+exception.getMessage());
		}
	}
	
	
	private void callToAfcClient() {
		//llamamos al cliente del servicio AFC, obteniedo el XML como resultado
		try {
			
			//int i = 5 / 0;
		}
		catch(Exception exception) {
			logger.error("Exception on callToAfcClient() method: ", exception);
			throw new InternalServerException("Exception on AFC service: "+exception.getMessage());
		}
	}
	
	private void proccessAfcOut() {
		//Devuelve el searching object in order to pass it to interface. 
		
		//donde tengo que leer el xml de ejemplo
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on proccessAfcOut() method: ", exception);
			throw new InternalServerException("Exception on bubble engine creating afc out: "+exception.getMessage());
		}
		
	}
	
	

}
