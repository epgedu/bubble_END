package es.uned.epardo30.bubbleend.externalresource.textalytics.mapper;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.AttributeDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.RelationDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.ResultsTextAlyticsDto;

/**
 * Unit test for MapperTextAlyticsXmlToDto
 * We are testing the mapping between the xml file returned from TextAlytics service toward Dto object
 * 
 * @author eduardo.guillen
 *
 */
public class MapperTextAlyticsXmlToDtoTest {

	@Test
	public void map() throws IOException, ParserConfigurationException, SAXException {
		String textAlyticsResponseString = getResource();
		MapperTextAlyticsXmlToDto mapperTextAlyticsXmlToDto = new MapperTextAlyticsXmlToDto();
		ResultsTextAlyticsDto resultsTextAlyticsDto = new ResultsTextAlyticsDto();
		resultsTextAlyticsDto.setAttributesDto(new ArrayList<AttributeDto>());
		resultsTextAlyticsDto.setContextDto(new ArrayList<RelationDto>());
		
		mapperTextAlyticsXmlToDto.map(textAlyticsResponseString, resultsTextAlyticsDto, 1, 20);
		
		ResultsTextAlyticsDto resultsTextAlyticsHope = new ResultsTextAlyticsDto();
		List<AttributeDto> attributeDtos = new ArrayList<AttributeDto>();
		attributeDtos.add(new AttributeDto(1, "Salud", "Top>Person>FirstName"));
		attributeDtos.add(new AttributeDto(2, "AS.com", "Top>Organization>Company>ConsumerGoodsCompany>Discretionary>ConsumerServicesCompany>MediaCompany"));
		attributeDtos.add(new AttributeDto(3, "UNED", "Top>Organization>EducationalOrganization>University"));
		List<RelationDto> relationDtos = new ArrayList<RelationDto>();
		relationDtos.add(new RelationDto(1, 1));
		relationDtos.add(new RelationDto(1, 2));
		relationDtos.add(new RelationDto(1, 3));
		resultsTextAlyticsHope.setAttributesDto(attributeDtos);
		resultsTextAlyticsHope.setContextDto(relationDtos);
		
		assertThat(resultsTextAlyticsHope).isEqualTo(resultsTextAlyticsDto);
	}
	
	/**
	 * Getting the external xml file which will be used as entrance to mapping method
	 * @return String
	 * @throws IOException
	 */
	private String getResource() throws IOException {
		InputStream resourceIS =	this.getClass().getResourceAsStream("/xml/textAlyticsResponse.xml");
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
