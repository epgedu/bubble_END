package es.uned.epardo30.bubbleend.externalresource.afc.mapper;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	
	public String map(ResultsGoogleDto resultsGoogleDto, ResultsTextAlyticsDto resultsTextAlyticsDto) throws ParserConfigurationException, TransformerException {
		logger.debug("MapperAfcDtoToXml.map...");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("generadorContexto");
		doc.appendChild(rootElement);
		
		// afc elements
		Element afc = doc.createElement("afc");
		rootElement.appendChild(afc);
		
		// objects elements
		Element objects =  doc.createElement("objetos");
		afc.appendChild(objects);
		Element object;
		int idObject = 0;
		for (ItemGoogleDto objectDto : resultsGoogleDto.getItemsGoogleDto()) {
			object = doc.createElement("objeto");
			object.setAttribute("id", "o"+idObject);
			//just the url as content
			object.setTextContent(objectDto.getHtmlFormattedUrl());
			objects.appendChild(object);
			idObject++;
		}
 
		//descriptors elements
		Element descriptors =  doc.createElement("descriptores");
		afc.appendChild(descriptors);
		Element descriptor;
		for (AttributeDto attributeDto : resultsTextAlyticsDto.getAttributesDto()) {
			descriptor = doc.createElement("descriptor");
			descriptor.setAttribute("id", "d"+attributeDto.getId());
			//just the attribute name.
			descriptor.setTextContent(attributeDto.getForm());
			descriptors.appendChild(descriptor);
		}
		
		//context element
		Element context =  doc.createElement("contexto");
		afc.appendChild(context);
		Element relation;
		Element objectRelation;
		Element descriptorRelation;
		for (RelationDto relationDto : resultsTextAlyticsDto.getContextDto()) {
			relation = doc.createElement("relacion");
			context.appendChild(relation);
			//object element for the relation
			objectRelation = doc.createElement("objeto");
			relation.appendChild(objectRelation);
			objectRelation.setAttribute("idref", "o"+relationDto.getIdObject());
			//descriptor element for the relation
			descriptorRelation = doc.createElement("objeto");
			relation.appendChild(descriptorRelation);
			descriptorRelation.setAttribute("idref", "d"+relationDto.getIdDescriptor());
		}
		
		logger.debug("mapping xml to entrace Afc to String...");
		return this.mapDocToString(doc);	
	}
	
	private String mapDocToString(Document document) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        // below code to remove XML declaration
        // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
