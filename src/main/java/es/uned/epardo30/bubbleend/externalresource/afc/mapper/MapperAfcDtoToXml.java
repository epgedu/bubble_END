package es.uned.epardo30.bubbleend.externalresource.afc.mapper;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import es.uned.epardo30.bubbleend.externalresource.google.dto.ItemGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.google.dto.ResultsGoogleDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.AttributeDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.RelationDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.ResultsTextAlyticsDto;

/**
 * Mapping the dto object from TextAlytics results to xml file. This file will be the entrance to AFC service
 * 
 * @author eduardo.guillen
 *
 */
public class MapperAfcDtoToXml {

	private static Logger logger = Logger.getLogger(MapperAfcDtoToXml.class);
	
	/**
	 * Return a String which represents the xml file which is the entrance param to afc service. 
	 * The out is created using the saved information in google results and textalytics results
	 * 
	 * @param resultsGoogleDto
	 * @param resultsTextAlyticsDto
	 * @return String
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 * @see ResultsGoogleDto 
	 * @see ResultsTextAlyticsDto
	 * 
	 */
	public String map(ResultsGoogleDto resultsGoogleDto, ResultsTextAlyticsDto resultsTextAlyticsDto) throws ParserConfigurationException, TransformerException, IOException {
		logger.debug("MapperAfcDtoToXml.map...");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		DOMImplementation implementation = docBuilder.getDOMImplementation();
		Document doc = implementation.createDocument(null, "generadorContexto", null);
		doc.setXmlVersion("1.0");
		// root elements
		Element rootElement = doc.getDocumentElement();
		
		// afc elements
		Element afc = doc.createElement("afc");
		
		// objects elements
		Element objects =  doc.createElement("objetos");
		Element object;
		int idObject = 1;
		int maxLoopObjects;
		if(logger.isDebugEnabled()) {
			//just process the three first results from google
			maxLoopObjects = 3;
		}
		else {
			maxLoopObjects = resultsGoogleDto.getItemsGoogleDto().size();
		}
		ItemGoogleDto objectDto = null;
		for(int i=0; i<maxLoopObjects; i++) {
			objectDto = resultsGoogleDto.getItemsGoogleDto().get(i);
			object = doc.createElement("objeto");
			object.setAttribute("id", "o"+idObject);
			//just the url as content
			object.setTextContent(objectDto.getTitle()+" snippetObjectBubble:"+objectDto.getSnnipet()+" urlObjectBubble:"+objectDto.getHtmlFormattedUrl());
			objects.appendChild(object);
			idObject++;
		}
		afc.appendChild(objects);
		
		//descriptors elements
		Element descriptors = doc.createElement("descriptores");
		Element descriptor;
		for (AttributeDto attributeDto : resultsTextAlyticsDto.getAttributesDto()) {
			descriptor = doc.createElement("descriptor");
			descriptor.setAttribute("id", "d"+attributeDto.getId());
			//just the attribute name.
			descriptor.setTextContent(attributeDto.getForm()+" typeAttributeBubble:"+attributeDto.getType());
			descriptors.appendChild(descriptor);
		}
		afc.appendChild(descriptors);
		
		//context element
		Element context =  doc.createElement("contexto");
		Element relation;
		Element objectRelation;
		Element descriptorRelation;
		for (RelationDto relationDto : resultsTextAlyticsDto.getContextDto()) {
			relation = doc.createElement("relacion");
			//object element for the relation
			objectRelation = doc.createElement("objeto");
			objectRelation.setAttribute("idref", "o"+relationDto.getIdObject());
			relation.appendChild(objectRelation);
			//descriptor element for the relation
			descriptorRelation = doc.createElement("objeto");
			descriptorRelation.setAttribute("idref", "d"+relationDto.getIdDescriptor());
			relation.appendChild(descriptorRelation);
			context.appendChild(relation);
			
		}
		afc.appendChild(context);
		rootElement.appendChild(afc);
		logger.debug("mapping xml to entrace Afc to String...");
		return format(doc);
	}
	
	/**
	 * Transform a document org.w3c to String which contains a xml format content
	 * 
	 * @param document
	 * @return String
	 */
	public String format(Document document) {
        try {
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
