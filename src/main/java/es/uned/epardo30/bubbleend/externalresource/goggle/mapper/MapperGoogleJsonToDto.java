package es.uned.epardo30.bubbleend.externalresource.goggle.mapper;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import es.uned.epardo30.bubbleend.exceptions.InternalServerException;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ItemGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ResultsGoogleDto;

public class MapperGoogleJsonToDto {

	private static Logger logger = Logger.getLogger(MapperGoogleJsonToDto.class);
	
	/**
	 * Process json object in order to get the input for syntactic scanner 
	 * @param googleResultJson
	 * @return 
	 */
	public ResultsGoogleDto map(JSONObject googleResultJson) {
		logger.debug("mapping google response from json objetc to dto...");
		try {
			logger.debug("MapperGoogleJsonToDto.map...");
			//init dto structure
			ResultsGoogleDto resultsGoogleDto = new ResultsGoogleDto(new ArrayList<ItemGoogleDto>());
			
			//read the json from google service search
			JSONArray items = (JSONArray) googleResultJson.get("items");
			//process every item
			//Iterator<JSONObject> itemsJson = ((List<JSONObject>) items).iterator();
			String title;
			String htmlFormattedUrl;
			String snnipet;
			for(int i=0; i<items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				//saved the item title
				title = item.getString("title");
				//save the url
				htmlFormattedUrl = item.getString("formattedUrl");
				//save the snnipet
				snnipet = item.getString("snippet");
				
				//saved the data on dto results
				resultsGoogleDto.getItemsGoogleDto().add(new ItemGoogleDto(title, htmlFormattedUrl, snnipet));
			}
			
			return resultsGoogleDto;
		}
		catch(Exception exception) {
			logger.error("Exception on processGoogleOut() method: ", exception);
			throw new InternalServerException("Exception on bubble engine processing: "+exception.getMessage());
		}
	}
}
