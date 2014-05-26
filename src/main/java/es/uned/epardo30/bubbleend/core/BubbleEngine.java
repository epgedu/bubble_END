package es.uned.epardo30.bubbleend.core;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.naming.LimitExceededException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.ClientHandlerException;

import es.uned.epardo30.bubbleend.exceptions.DailyLimitExceededGoogleException;
import es.uned.epardo30.bubbleend.exceptions.InternalServerException;
import es.uned.epardo30.bubbleend.externalresource.afc.core.AfcClient;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.LatticeDto;
import es.uned.epardo30.bubbleend.externalresource.afc.mapper.MapperAfcDtoToXml;
import es.uned.epardo30.bubbleend.externalresource.afc.mapper.MapperAfcXmlToDto;
import es.uned.epardo30.bubbleend.externalresource.goggle.core.GoogleClient;
import es.uned.epardo30.bubbleend.externalresource.goggle.mapper.MapperGoogleJsonToDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ItemGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ResultsGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.core.TextAlyticsClient;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.AttributeDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.RelationDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.ResultsTextAlyticsDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.mapper.MapperTextAlyticsXmlToDto;

/**
 * 
 * This class executes the bubble workflow going through different stages: 
 * 		1- Call google client in order to get the results from google engine.
 *      2- Call textalytics client to analyse the syntactics concepts for each google result.
 *      3- Call afc client to get the final lattice  
 * 
 * @author eduardo.guillen
 *
 */
public class BubbleEngine {
	
	static Logger logger = Logger.getLogger(BubbleEngine.class);
	
	/**
	 * Execute the main workflow, starting with the filter string received from bubble GUI , this method returns the lattice
	 * which will be sent to GUI. 
	 * 
	 * @param textSearchFilter
	 * @param googleClient
	 * @param textAlyticsClient
	 * @param relevanceConf
	 * @param afcClient
	 * @return LatticeDto
	 */
	public LatticeDto workflowEngine(String textSearchFilter, GoogleClient googleClient, TextAlyticsClient textAlyticsClient, double relevanceConf, AfcClient afcClient, int queriesToProcess) {
		logger.debug("Initializing workflowEngine...");
		ResultsGoogleDto resultsGoogleDto = this.callToGoogleClient(googleClient, textSearchFilter, queriesToProcess);
		if(resultsGoogleDto.getItemsGoogleDto().isEmpty()) {
			logger.debug("Return empty lattice");
			return new LatticeDto();
		}
		else {
			ResultsTextAlyticsDto resultsTextAlyticsDto = this.callTextalyticsProcess(resultsGoogleDto, textAlyticsClient, relevanceConf, queriesToProcess);
			LatticeDto latticeDto = this.callToAfcClient(resultsGoogleDto, resultsTextAlyticsDto, afcClient, queriesToProcess);
			return latticeDto;
		}

	}
	
	/**
	 * Calling google client to get the result in Json format and mapping to ResultsGoogleDto format
	 * 
	 * @param googleClient
	 * @param textSearchFilter
	 * @return ResultsGoogleDto
	 */
	private ResultsGoogleDto callToGoogleClient(GoogleClient googleClient, String textSearchFilter, int queriesToProcess) {
		//calling to resources.GoogleClient service
		MapperGoogleJsonToDto mapperGoogleJsonToDto = new MapperGoogleJsonToDto();
		JSONObject googleResponse = null;
		ResultsGoogleDto resultsGoogleDto = new ResultsGoogleDto(new ArrayList<ItemGoogleDto>());
		int connectionTry = 10; //10 opportunities for all requests to google
		try {
			logger.debug("BubbleEngine.CallToGoogleClient()...");
			for(int i = 0; i < queriesToProcess; i++) {
				try {
					googleResponse = googleClient.getResource(textSearchFilter, i);
					mapperGoogleJsonToDto.map(googleResponse, resultsGoogleDto);
				}
				catch(ClientHandlerException clientHandlerException) {
					if(clientHandlerException.getCause().getClass().equals(SocketTimeoutException.class)) {
						if(connectionTry > 0) {
							connectionTry --;
							i --; //try again
							logger.warn("SocketTimeoutException in google call...Try connection again. There are chances left: "+connectionTry);
						}
						else {
							logger.warn("No more chances...");
							throw new InternalServerException("Timeout calling service google: "+clientHandlerException.getMessage());
						}
					}
					else {
						logger.error("ClientHandlerException on callToGoogleClient() method: ", clientHandlerException);
						throw new InternalServerException("ClientHandlerException on service google: "+clientHandlerException.getMessage());
					}
				}
				catch(JSONException exception) {
					//if the exception is raised because of the google response is empty then we throw the exception
					if(resultsGoogleDto.getItemsGoogleDto().isEmpty()) {
						logger.error("JSONException on callToGoogleClient() method: ", exception);
						throw new InternalServerException("JSONException on service google: "+exception.getMessage());
					}
					else {
						//if the response is empty but it is not the first request, doesn't matter. One request for each 10 results
						return resultsGoogleDto;
					}
				}
			}
			return resultsGoogleDto;
		}
		catch(DailyLimitExceededGoogleException dailyLimitExceededGoogleException) {
			throw dailyLimitExceededGoogleException;
		}
		catch(InternalServerException internalServerException) {
			throw internalServerException;
		}
		catch(Exception exception) {
			logger.error("Exception on callToGoogleClient() method: ", exception);
			throw new InternalServerException("Exception on service google: "+exception.getMessage());
		}
		finally {
			mapperGoogleJsonToDto = null;
			googleResponse = null;
		}
	
	}
	
	/**
	 * For each result saved on ResultGoogleDto object, we call to textAlytics services in order to get the attributes or descriptors for each one and . 
	 * @param resultsGoogleDto
	 * @param textAlyticsClient 
	 */
	public ResultsTextAlyticsDto callTextalyticsProcess(ResultsGoogleDto resultsGoogleDto, TextAlyticsClient textAlyticsClient, double relevanceConf, int queriesToProcess) {
		logger.debug("BubbleEngine.callTextalyticsProcess");
		ItemGoogleDto item;
		String textToSend;
		MapperTextAlyticsXmlToDto mapperTextAlyticsXmlToDto;
		ResultsTextAlyticsDto resultsTextAlyticsDto;
		try {
			mapperTextAlyticsXmlToDto = new MapperTextAlyticsXmlToDto();
			resultsTextAlyticsDto = new ResultsTextAlyticsDto();
			resultsTextAlyticsDto.setAttributesDto(new ArrayList<AttributeDto>());
			resultsTextAlyticsDto.setContextDto(new ArrayList<RelationDto>());
			
			int maxLoopTextAlytics;
			if(logger.isDebugEnabled()) {
				//just process one request (10 results)
				maxLoopTextAlytics = 10; 
			}
			else {
				//process 10 (results per query) * queriesToProcess parameter 
				maxLoopTextAlytics = 10 * queriesToProcess;
			}
			
			//maybe there are less than the wished amount
			if(resultsGoogleDto.getItemsGoogleDto().size() < maxLoopTextAlytics) {
				maxLoopTextAlytics = resultsGoogleDto.getItemsGoogleDto().size();
			}
			
			int connectionTry = 10; //10 opportunities for all requests to textAlytics
			for (int i = 0; i < maxLoopTextAlytics; i++) {
				item = resultsGoogleDto.getItemsGoogleDto().get(i);
				//join the tittle, snnipet and description
				textToSend = item.getTitle()+" "+item.getSnnipet();
				if(!textToSend.isEmpty()) {
					//if text to process is not empty for this object
					//call the textAlytics service
					try {
						String response = textAlyticsClient.getResource(textToSend);
						//process the textAlytics response
						logger.debug("Proccesing attributes for object: "+item.getTitle());
						mapperTextAlyticsXmlToDto.map(response, resultsTextAlyticsDto, i+1, relevanceConf);
					}
					catch(ClientHandlerException clientHandlerException) {
						if(clientHandlerException.getCause().getClass().equals(SocketTimeoutException.class)) {
							if(connectionTry > 0) {
								connectionTry --;
								i --; //try again
								logger.warn("SocketTimeoutException on textAlytic call...Try connection again. There are chances left: "+connectionTry);
							}
							else {
								logger.warn("No more chances...");
								throw new InternalServerException("Timeout calling service textAlytics: "+clientHandlerException.getMessage());
							}
						}
						else {
							logger.error("ClientHandlerException on callTextalyticsProcess() method: ", clientHandlerException);
							throw new InternalServerException("ClientHandlerException on service textAlytics: "+clientHandlerException.getMessage());
						}
					}
				}
			}
			logger.debug("out from textalytics process: ");
			logger.debug("attributes:");
			for(int i=0; i<resultsTextAlyticsDto.getAttributesDto().size(); i++) {
				logger.debug(resultsTextAlyticsDto.getAttributesDto().get(i).getId());
				logger.debug(resultsTextAlyticsDto.getAttributesDto().get(i).getForm());
				logger.debug(resultsTextAlyticsDto.getAttributesDto().get(i).getType());
			}
			logger.debug("objects:");
			for(int i=0; i<maxLoopTextAlytics; i++) {
				logger.debug(i);
				logger.debug(resultsGoogleDto.getItemsGoogleDto().get(i).getTitle());
				logger.debug(resultsGoogleDto.getItemsGoogleDto().get(i).getSnnipet());
				logger.debug(resultsGoogleDto.getItemsGoogleDto().get(i).getHtmlFormattedUrl());
			}
			logger.debug("context:");
			for(int i=0; i<resultsTextAlyticsDto.getContextDto().size(); i++) {
				logger.debug("object: "+resultsTextAlyticsDto.getContextDto().get(i).getIdObject());
				logger.debug("attribute: "+resultsTextAlyticsDto.getContextDto().get(i).getIdDescriptor());
			}
			return resultsTextAlyticsDto;
			
		}
		catch(InternalServerException internalServerException) {
			throw internalServerException;
		}
		catch(Exception exception) {
			logger.error("Exception on callTextalyticsProcess() method: ", exception);
			throw new InternalServerException("Exception on Textalytics process: "+exception.getMessage());
		}
		finally {
			item = null;
			textToSend = null;
			mapperTextAlyticsXmlToDto = null;
			resultsTextAlyticsDto = null;
		}
	}
	
	/**
	 * Client to call AFC Service which return lattice on xml format. 
	 * There are three steps:
	 * 		- MapperAfcDtoToXml: Mapping to create the xml entrance to afc service
	 * 		- Call to afc service: Return the lattice on xml format 
	 * 		- MapperAfcXmlToDto: Transform the lattice from xml to dto format
 
	 * 
	 * @param resultsGoogleDto
	 * @param resultsTextAlyticsDto
	 * @param afcClient
	 * @return LatticeDto
	 */
	private LatticeDto callToAfcClient(ResultsGoogleDto resultsGoogleDto, ResultsTextAlyticsDto resultsTextAlyticsDto, AfcClient afcClient, int queriesToProcess) {
		MapperAfcDtoToXml mapperAfcDtoToXml = new MapperAfcDtoToXml();
		MapperAfcXmlToDto mapperAfcXmlToDto = new MapperAfcXmlToDto();
		try {
			//mapping from ResultsTextAlyticsDto to xml file
			String xmlString = mapperAfcDtoToXml.map(resultsGoogleDto, resultsTextAlyticsDto, queriesToProcess);
			int connectionTry = 10; //10 opportunities for all requests to afc service
			String afcResult = null;
			while (connectionTry > 0) {
				try {
					afcResult = afcClient.getResource(xmlString);
					//no exceptions
					connectionTry = 0;//don't try again
				}
				catch(ClientHandlerException clientHandlerException) {
					if(clientHandlerException.getCause().getClass().equals(SocketTimeoutException.class)) {
						if(connectionTry > 0) {
							connectionTry --;
							logger.warn("SocketTimeoutException on afc call...Try connection again. There are chances left: "+connectionTry);
						}
						else {
							logger.warn("No more chances...");
							throw new InternalServerException("Timeout calling afc service: "+clientHandlerException.getMessage());
						}
					}
					else {
						logger.error("ClientHandlerException on callToAfcClient() method: ", clientHandlerException);
						throw new InternalServerException("ClientHandlerException on service afc: "+clientHandlerException.getMessage());
					}
				}
			}
			return mapperAfcXmlToDto.map(afcResult);
		}
		catch(InternalServerException internalServerException) {
			throw internalServerException;
		}
		catch(Exception exception) {
			logger.error("Exception on callToAfcClient() method: ", exception);
			throw new InternalServerException("Exception on AFC service: "+exception.getMessage());
		}
		finally {
			mapperAfcDtoToXml = null;
			mapperAfcXmlToDto = null;
		}
	}

}
