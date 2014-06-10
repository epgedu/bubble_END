package es.uned.epardo30.bubbleend.externalresource.afc.mapper;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import es.uned.epardo30.bubbleend.externalresource.afc.dto.ContentDescriptorDto;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.ContentObjectDto;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.FormalConceptDto;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.LatticeDto;

/**
 * 
 * Unit test for MapperAfcXmlToDto
 * We are testing the mapping between the xml file which is got from Afc service toward LatticeDto which will be returned to bubble_GUI
 * 
 * @author eduardo.guillen
 *
 */
public class MapperAfcXmlToDtoTest {

	@Test
	public void map() throws IOException {
		MapperAfcXmlToDto mapperAfcXmlToDto = new MapperAfcXmlToDto();
		LatticeDto latticeDto = mapperAfcXmlToDto.map(getResource());
		//create the object that we hope to get
		LatticeDto latticeHope = new LatticeDto();
		//add the content - formal concept list
		FormalConceptDto formalConceptDto = new FormalConceptDto();
		//id formal concept
		formalConceptDto.setConceptId("29605");
		//extension
		ContentObjectDto contentObjectDto1 = new ContentObjectDto();
		contentObjectDto1.setId("o1");
		contentObjectDto1.setValue("Madrid Data /b Center snippetObjectBubble:Global Switch b Data /b Centre Spain urlObjectBubble:http://www.globalswitch.com/en/locations/madrid-data-center");
		formalConceptDto.getExtension().add(contentObjectDto1);
		ContentObjectDto contentObjectDto3 = new ContentObjectDto();
		contentObjectDto3.setId("o3");
		contentObjectDto3.setValue("Spanish prepositions snippetObjectBubble:Wikipedia, the free encyclopedia urlObjectBubble:http://en.wikipedia.org/wiki/Spanish_prepositions");
		formalConceptDto.getExtension().add(contentObjectDto3);
		ContentObjectDto contentObjectDto4 = new ContentObjectDto();
		contentObjectDto4.setId("o4");
		contentObjectDto4.setValue("madrigal meaning snippetObjectBubble:definition of madrigal by b Mnemonic /b Dictionary urlSnippetBubble:http://www.mnemonicdictionary.com/word/madrigal");
		formalConceptDto.getExtension().add(contentObjectDto4);
		//intension
		ContentDescriptorDto contentDescriptorDto7 = new ContentDescriptorDto();
		contentDescriptorDto7.setId("d7");
		contentDescriptorDto7.setValue("ANALYZE typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto7);
		ContentDescriptorDto contentDescriptorDto10 = new ContentDescriptorDto();
		contentDescriptorDto10.setId("d10");
		contentDescriptorDto10.setValue("EVALUATE typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto10);
		//parents
		//no parents
		//children
		formalConceptDto.getChildrenFormalConceptId().add("25203");
		formalConceptDto.getChildrenFormalConceptId().add("27559");
		//include the formal concept to lattice
		latticeHope.getContentObjects().add(formalConceptDto);
		
		formalConceptDto = new FormalConceptDto();
		//id formal concept
		formalConceptDto.setConceptId("25203");
		//extension
		ContentObjectDto contentObjectDto5 = new ContentObjectDto();
		contentObjectDto5.setId("o5");
		contentObjectDto5.setValue("Wedding Products snippetObjectBubble:b Mnemonic /b Photography | Aberdeen Wedding b ... /b urlObjectBubble:http://www.mnemonicphotography.co.uk/wedding-products/");
		formalConceptDto.getExtension().add(contentObjectDto5);
		ContentObjectDto contentObjectDto6 = new ContentObjectDto();
		contentObjectDto6.setId("o6");
		contentObjectDto6.setValue("Services snippetObjectBubble:Madrid /b urlObjectBubble:http://www.madridfe.com/Services.php");
		formalConceptDto.getExtension().add(contentObjectDto6);
		//intension
		ContentDescriptorDto contentDescriptorDto4 = new ContentDescriptorDto();
		contentDescriptorDto4.setId("d4");
		contentDescriptorDto4.setValue("CONCEPTUAL typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto4);
		formalConceptDto.getIntension().add(contentDescriptorDto7);
		formalConceptDto.getIntension().add(contentDescriptorDto10);
		//parents
		formalConceptDto.getParentsFormalConceptId().add("29605");
		//children
		formalConceptDto.getChildrenFormalConceptId().add("12679");
		//include the formal concept to lattice
		latticeHope.getContentObjects().add(formalConceptDto);
		
		formalConceptDto = new FormalConceptDto();
		//id formal concept
		formalConceptDto.setConceptId("27559");
		//extension
		formalConceptDto.getExtension().add(contentObjectDto1);
		formalConceptDto.getExtension().add(contentObjectDto4);
		formalConceptDto.getExtension().add(contentObjectDto6);
		ContentObjectDto contentObjectDto8 = new ContentObjectDto();
		contentObjectDto8.setId("o8");
		contentObjectDto8.setValue("Acting on Selected Observations: Constructing Conditions :: Step-by b ... /b snippetObjectBubble: urlObjectBubble:http://support.sas.com/documentation/cdl/en/basess/58133/HTML/default/a001773321.htm");
		formalConceptDto.getExtension().add(contentObjectDto8);
		ContentObjectDto contentObjectDto9 = new ContentObjectDto();
		contentObjectDto9.setId("o9");
		contentObjectDto9.setValue("Learn Spanish Blog snippetObjectBubble: urlObjectBubble:http://www.learn-spanish-help.com/learn-spanish-blog.html");
		formalConceptDto.getExtension().add(contentObjectDto9);
		//intension
		ContentDescriptorDto contentDescriptorDto1 = new ContentDescriptorDto();
		contentDescriptorDto1.setId("d1");
		contentDescriptorDto1.setValue("PROCEDURAL typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto1);
		ContentDescriptorDto contentDescriptorDto3 = new ContentDescriptorDto();
		contentDescriptorDto3.setId("d3");
		contentDescriptorDto3.setValue("STRATEGIC typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto3);
		ContentDescriptorDto contentDescriptorDto5 = new ContentDescriptorDto();
		contentDescriptorDto5.setId("d5");
		contentDescriptorDto5.setValue("APPLY typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto5);
		formalConceptDto.getIntension().add(contentDescriptorDto7);
		ContentDescriptorDto contentDescriptorDto8 = new ContentDescriptorDto();
		contentDescriptorDto8.setId("d8");
		contentDescriptorDto8.setValue("UNDERSTAND typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto8);
		ContentDescriptorDto contentDescriptorDto9 = new ContentDescriptorDto();
		contentDescriptorDto9.setId("d9");
		contentDescriptorDto9.setValue("CREATE typeAttributeBubble:type");
		formalConceptDto.getIntension().add(contentDescriptorDto9);
		formalConceptDto.getIntension().add(contentDescriptorDto10);
		//parents
		formalConceptDto.getParentsFormalConceptId().add("29605");
		formalConceptDto.getParentsFormalConceptId().add("25203");
		//childrens
		formalConceptDto.getChildrenFormalConceptId().add("12679");
		//include the formal concept to lattice
		latticeHope.getContentObjects().add(formalConceptDto);
		
		formalConceptDto = new FormalConceptDto();
		//id formal concept
		formalConceptDto.setConceptId("12679");
		//extension
		formalConceptDto.getExtension().add(contentObjectDto9);
		//intension
		formalConceptDto.getIntension().add(contentDescriptorDto8);
		formalConceptDto.getIntension().add(contentDescriptorDto9);
		formalConceptDto.getIntension().add(contentDescriptorDto10);
		//parents
		formalConceptDto.getParentsFormalConceptId().add("25203");
		formalConceptDto.getParentsFormalConceptId().add("27559");
		//children
		//not children
		//include the formal concept to lattice
		latticeHope.getContentObjects().add(formalConceptDto);
		
		//we hope 0 objetoContenido tags (just during the test)
		latticeDto.setTotalResult(0);
		
		//compare the objects
		assertThat(latticeHope).isEqualTo(latticeDto);
	}
	
	/**
	 * Getting the resource xml file, which will be the entrance to mapping test.
	 * @return String
	 * @throws IOException
	 */
	private String getResource() throws IOException {
		InputStream resourceIS =	this.getClass().getResourceAsStream("/xml/afcResponse.xml");
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
