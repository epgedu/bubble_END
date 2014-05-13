package es.uned.epardo30.bubbleend.externalresource.afc.mapper;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.uned.epardo30.bubbleend.externalresource.google.dto.ItemGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ResultsGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.AttributeDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.RelationDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.ResultsTextAlyticsDto;

/**
 * Unit test for MapperAfcDtoToXml.
 * We are testing the mapping between the objects dto (resultsGoogleDto and resultsTextAlyticsDto) toward xml request to afc service
 * 
 * @author eduardo.guillen
 *
 */
public class MapperAfcDtoToXmlTest {

	@Test
	public void map() throws ParserConfigurationException, TransformerException, IOException, SAXException {
		MapperAfcDtoToXml mapperAfcDtoToXml = new MapperAfcDtoToXml();
		
		ResultsGoogleDto resultsGoogleDto = new ResultsGoogleDto(new ArrayList<ItemGoogleDto>());
		resultsGoogleDto.getItemsGoogleDto().add(new ItemGoogleDto("object 1", "url object 1", "snippet object 1"));
		resultsGoogleDto.getItemsGoogleDto().add(new ItemGoogleDto("object 2", "url object 2", "snippet object 2"));
		resultsGoogleDto.getItemsGoogleDto().add(new ItemGoogleDto("object 3", "url object 3", "snippet object 3"));
		
		ResultsTextAlyticsDto resultsTextAlyticsDto = new ResultsTextAlyticsDto();
		List<AttributeDto> attributeDtos = new ArrayList<AttributeDto>();
		attributeDtos.add(new AttributeDto(1, "descriptor 1", "type descriptor 1"));
		attributeDtos.add(new AttributeDto(2, "descriptor 2", "type descriptor 2"));
		attributeDtos.add(new AttributeDto(3, "descriptor 3", "type descriptor 3"));
		List<RelationDto> relationDtos = new ArrayList<RelationDto>();
		relationDtos.add(new RelationDto(1, 1));
		relationDtos.add(new RelationDto(1, 3));
		relationDtos.add(new RelationDto(2, 3));
		relationDtos.add(new RelationDto(3, 2));
		relationDtos.add(new RelationDto(3, 3));
		resultsTextAlyticsDto.setAttributesDto(attributeDtos);
		resultsTextAlyticsDto.setContextDto(relationDtos);
		
		String xmlRequestAfc = mapperAfcDtoToXml.map(resultsGoogleDto, resultsTextAlyticsDto);
		String xmlRequesHope = getResource();
		
		assertThat(xmlRequestAfc).isEqualTo(xmlRequesHope);
	}
	
	/**
	 * Getting the resource (xml file) whose content is the expected content after to execute the test mapping method
	 * @return String
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private String getResource() throws IOException, ParserConfigurationException, SAXException {
		InputStream resourceIS =	this.getClass().getResourceAsStream("/xml/afcRequest.xml");
		DocumentBuilderFactory factory = null;
	    DocumentBuilder builder = null;
	    Document ret = null;
	    factory = DocumentBuilderFactory.newInstance();
	    builder = factory.newDocumentBuilder();
	    ret = builder.parse(new InputSource(resourceIS));
	    OutputFormat format = new OutputFormat(ret);
        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(ret);
        return out.toString();
	}
}
