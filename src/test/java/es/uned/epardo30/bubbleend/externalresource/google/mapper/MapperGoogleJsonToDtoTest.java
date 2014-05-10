package es.uned.epardo30.bubbleend.externalresource.google.mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Test;
import static org.fest.assertions.api.Assertions.assertThat;

import es.uned.epardo30.bubbleend.externalresource.goggle.mapper.MapperGoogleJsonToDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ItemGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ResultsGoogleDto;

/**
 * Unit test for MapperGoogleJsonToDto
 * 
 * @author eduardo.guillen
 *
 */
public class MapperGoogleJsonToDtoTest {
	
	@Test
	public void map() throws IOException {
		String googleResponseString = getResource();
		JSONObject googleResponseJson = new JSONObject(googleResponseString);
		MapperGoogleJsonToDto mapperGoogleJsonToDto = new MapperGoogleJsonToDto();
		ResultsGoogleDto resultsGoogleDto = mapperGoogleJsonToDto.map(googleResponseJson);
		
		ResultsGoogleDto resultHope = new ResultsGoogleDto(new ArrayList<ItemGoogleDto>());
		resultHope.getItemsGoogleDto().add(new ItemGoogleDto("title del primer resultado", "url del primer resultado", "snippet del primer resultado"));
		resultHope.getItemsGoogleDto().add(new ItemGoogleDto("title del segundo resultado", "url del segundo resultado", "snippet del segundo resultado"));
		resultHope.getItemsGoogleDto().add(new ItemGoogleDto("title del tercer resultado", "url del tercer resultado", "snippet del tercer resultado"));
		
		assertThat(resultHope).isEqualTo(resultsGoogleDto);
		
	}
	
	private String getResource() throws IOException {
		InputStream resourceIS =	this.getClass().getResourceAsStream("/json/googleResponse.json");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceIS));
        // reads each line
        String auxStr;
        String resourceStr = "";
        while((auxStr = bufferedReader.readLine()) != null) {
        	resourceStr = resourceStr + auxStr;
        } 
        resourceIS.close();
        return resourceStr;
	}

}
