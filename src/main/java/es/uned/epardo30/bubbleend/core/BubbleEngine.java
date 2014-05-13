package es.uned.epardo30.bubbleend.core;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONObject;

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
	public LatticeDto workflowEngine(String textSearchFilter, GoogleClient googleClient, TextAlyticsClient textAlyticsClient, double relevanceConf, AfcClient afcClient) {
		logger.debug("Initializing workflowEngine...");
		ResultsGoogleDto resultsGoogleDto = this.callToGoogleClient(googleClient, textSearchFilter);
		if(resultsGoogleDto.getItemsGoogleDto().isEmpty()) {
			logger.debug("Return empty lattice");
			return new LatticeDto();
		}
		else {
			ResultsTextAlyticsDto resultsTextAlyticsDto = this.callTextalyticsProcess(resultsGoogleDto, textAlyticsClient, relevanceConf);
			LatticeDto latticeDto = this.callToAfcClient(resultsGoogleDto, resultsTextAlyticsDto, afcClient);
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
	private ResultsGoogleDto callToGoogleClient(GoogleClient googleClient, String textSearchFilter) {
		//calling to resources.GoogleClient service
		MapperGoogleJsonToDto mapperGoogleJsonToDto = new MapperGoogleJsonToDto();
		JSONObject googleResponse;
		try {
			logger.debug("BubbleEngine.CallToGoogleClient()...");
			googleResponse = googleClient.getResource(textSearchFilter);
			return mapperGoogleJsonToDto.map(googleResponse);
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
	public ResultsTextAlyticsDto callTextalyticsProcess(ResultsGoogleDto resultsGoogleDto, TextAlyticsClient textAlyticsClient, double relevanceConf) {
		logger.debug("BubbleEngine.callTextalyticsProcess");
		ItemGoogleDto item;
		String textToSend;
		MapperTextAlyticsXmlToDto mapperTextAlyticsXmlToDto;
		ResultsTextAlyticsDto resultsTextAlyticsDto;
		try {
			mapperTextAlyticsXmlToDto = new MapperTextAlyticsXmlToDto();
			resultsTextAlyticsDto = new ResultsTextAlyticsDto();
			//resultsTextAlyticsDto.setObjectsDto(new ArrayList<ObjectDto>());
			resultsTextAlyticsDto.setAttributesDto(new ArrayList<AttributeDto>());
			resultsTextAlyticsDto.setContextDto(new ArrayList<RelationDto>());
			
			int maxLoopTextAlytics;
			if(logger.isDebugEnabled()) {
				//just process the three first results from google
				maxLoopTextAlytics = 3;
			}
			else {
				maxLoopTextAlytics = resultsGoogleDto.getItemsGoogleDto().size();
			}
			
			for (int i = 0; i < maxLoopTextAlytics; i++) {
				item = resultsGoogleDto.getItemsGoogleDto().get(i);
				//join the tittle, snnipet and description
				textToSend = item.getTitle()+" "+item.getSnnipet();
				if(!textToSend.isEmpty()) {
					//if text to process is not empty for this object
					//call the textAlytics service
					String response = textAlyticsClient.getResource(textToSend);
					//process the textAlytics response
					logger.debug("Proccesing attributes for object: "+item.getTitle());
					mapperTextAlyticsXmlToDto.map(response, resultsTextAlyticsDto, i+1, relevanceConf);
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
			for(int i=0; i<resultsGoogleDto.getItemsGoogleDto().size(); i++) {
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
	private LatticeDto callToAfcClient(ResultsGoogleDto resultsGoogleDto, ResultsTextAlyticsDto resultsTextAlyticsDto, AfcClient afcClient) {
		MapperAfcDtoToXml mapperAfcDtoToXml = new MapperAfcDtoToXml();
		MapperAfcXmlToDto mapperAfcXmlToDto = new MapperAfcXmlToDto();
		try {
			//mapping from ResultsTextAlyticsDto to xml file
			String xmlString = mapperAfcDtoToXml.map(resultsGoogleDto, resultsTextAlyticsDto);
			String afcResult = afcClient.getResource(xmlString);
			return mapperAfcXmlToDto.map(afcResult);
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
