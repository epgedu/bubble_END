package es.uned.epardo30.bubbleend.externalresource.afc.mapper;

import java.io.StringReader;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.uned.epardo30.bubbleend.exceptions.InternalServerException;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.ContentDescriptorDto;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.ContentObjectDto;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.FormalConceptDto;
import es.uned.epardo30.bubbleend.externalresource.afc.dto.LatticeDto;

/**
 * Mapping the xml which has been returned from AFC service to DTO object. This object will be returned to Bubble_GUI
 * 
 * @author eduardo.guillen
 *
 */

public class MapperAfcXmlToDto {
	
	private static Logger logger = Logger.getLogger(MapperAfcXmlToDto.class);
	
	/**
	 * Return a LatticeDto object which contains the information about the returned lattice from AFC service.
	 *		
	 * @return LatticeDto
	 */
	public LatticeDto map(String xmlStr) {
		
		logger.debug("MapperAfcXmlToDto.map");
		try {
			//reading the xml file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			//Document document = dBuilder.parse(xmlFile);
			Document document = dBuilder.parse(new InputSource( new StringReader( xmlStr )));
			
			//optional, but recommended, normalize text representation
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			document.getDocumentElement().normalize();
		 	
			//extract the root element. The root node has to be AFC node
			Element root = document.getDocumentElement();
			logger.debug("Root element :" + document.getDocumentElement().getNodeName());

			
			//extract the values from object and attributes
			//extract the "contenido" node (only there is one)
			Element content = (Element)root.getElementsByTagName("contenido").item(0);
			
			//getting relations id-value on objects
			//hashtable where the value is saved with a key (object id)
			Hashtable<String, String> idValueObject = new Hashtable<String, String>();
			//extract the "objetosContenido" tag (only there is one)
			Element objectContents = (Element)content.getElementsByTagName("objetosContenido").item(0);
			//extract the list of "objetoContenido" tags
			NodeList objectContent = objectContents.getElementsByTagName("objetoContenido");
			logger.debug("Total no of objetoContenido tag: "+objectContents.getElementsByTagName("objetoContenido").getLength());
			for(int i=0; i<objectContent.getLength(); i++) {
				Node nodeAux = objectContent.item(i);
				if(nodeAux.getNodeType() == Node.ELEMENT_NODE) {
					//extract the id and value
					Element element = (Element)nodeAux; 
					//extract id (there is one)
					Node idObject = element.getElementsByTagName("id").item(0);
					//extract value (there is one)
					Node valueObject = element.getElementsByTagName("valor").item(0);
					//saved the relation
					logger.debug("idObject: "+idObject.getTextContent()+" valueNode: "+valueObject.getTextContent());
					idValueObject.put( idObject.getTextContent() , valueObject.getTextContent());
				}
			}
			
			
			//getting relations id-value on attributes
			Hashtable<String, String> idValueAttribute = new Hashtable<String, String>();
			//extract the "objetosContenido" tag (only there is one)
			Element descriptorContents = (Element)content.getElementsByTagName("descriptoresContenido").item(0);
			//extract the list of "descriptorContenido" tags
			NodeList descriptorContent = descriptorContents.getElementsByTagName("descriptorContenido");
			logger.debug("Total no of descriptorContenido tag: "+objectContents.getElementsByTagName("descriptorContenido").getLength());
			for(int i=0; i<descriptorContent.getLength(); i++) {
				Node nodeAux = descriptorContent.item(i);
				if(nodeAux.getNodeType() == Node.ELEMENT_NODE) {
					//extract the id and value
					Element element = (Element)nodeAux;
					//extract id (there is one)
					Node idDescriptor = element.getElementsByTagName("id").item(0);
					//extract value (there is one)
					Node valueDescriptor = element.getElementsByTagName("valor").item(0);
					//saved the relation
					logger.debug("idDescriptor: "+idDescriptor.getTextContent()+" valueDescriptor: "+valueDescriptor.getTextContent());
					idValueAttribute.put( idDescriptor.getTextContent() , valueDescriptor.getTextContent());
				}
				
			}
			
			//building lattice
			FormalConceptDto formalConceptDto;
			ContentObjectDto contentObjectDto;
			ContentDescriptorDto contentDescriptorDto;
					 	
			//extract the lattice node (only there is one)
			NodeList lattices = root.getElementsByTagName("lattice");
			Element lattice = (Element)lattices.item(0);
			//create latticeDto object
			LatticeDto latticeDto = new LatticeDto();
			
			//add the total number of results. Is is not the same than the amount of formal concepts. We need the amount of processed object to show on screen, the total results
			latticeDto.setTotalResult(objectContent.getLength());
			
			//extract every formal concept from lattice
			NodeList formalConcepts = lattice.getElementsByTagName("formalConcept");
			logger.debug("Total no of formalConcept tag: "+lattice.getElementsByTagName("formalConcept").getLength());
			for(int i=0; i<formalConcepts.getLength(); i++) {

				//create the new formalConceptDto to represente teh fomalConcept tag
				formalConceptDto = new FormalConceptDto();
			
				Node nodeAux = formalConcepts.item(i);
				if(nodeAux.getNodeType() == Node.ELEMENT_NODE) {
					Element formalConcept = (Element) nodeAux;
						
					//extract concept id from formal concept. Only there is one
					Node conceptId = formalConcept.getElementsByTagName("conceptId").item(0);
					String valueConceptId = conceptId.getTextContent();
					formalConceptDto.setConceptId(valueConceptId);
					logger.debug("conceptId: "+valueConceptId);
					
					//extract extension from concept formal. Only there is one tab as extension and inside of this, we have the list of id's (objetos tab) whose building the extension
					Element extension = (Element)formalConcept.getElementsByTagName("extension").item(0);
					//extract the id's from extension
					NodeList extensionIds = extension.getElementsByTagName("id");
					for(int j=0; j<extensionIds.getLength(); j++) {
						Node extensionId = extensionIds.item(j);
						String valueExtensionId = extensionId.getTextContent();
						contentObjectDto = new ContentObjectDto();
						contentObjectDto.setId("o"+valueExtensionId);//add "o" as first letter in order to difference with descriptor id
						//insert the related value
						contentObjectDto.setValue(idValueObject.get("o"+valueExtensionId));
						formalConceptDto.getExtension().add(contentObjectDto);
						logger.debug("extensionId: "+valueExtensionId+ " extensionValue: "+idValueObject.get("o"+valueExtensionId));
					}
					
					//extract intension from formal concept. Only there is one tab as intension and inside of this, we have the list of id's (descriptores tab) whose building the intension
					Element intension = (Element)formalConcept.getElementsByTagName("intension").item(0);
					//extract the id's from intension
					NodeList intensionIds = intension.getElementsByTagName("id");
					for(int j=0; j<intensionIds.getLength(); j++) {
						Node intensionId = intensionIds.item(j);
						String valueIntensionId = intensionId.getTextContent();
						contentDescriptorDto = new ContentDescriptorDto();
						contentDescriptorDto.setId("d"+valueIntensionId);//add "d" as first letter in order to difference with object id
						contentDescriptorDto.setValue(idValueAttribute.get("d"+valueIntensionId));
						formalConceptDto.getIntension().add(contentDescriptorDto);
						logger.debug("intensionId: "+valueIntensionId+ " intensionValue: "+idValueAttribute.get("d"+valueIntensionId));
					}
					
					//extract parents from formal concept. Only there is one tab as parents and inside of this, we have the list of id's (formal concepts) whose are parents
					Element parents = (Element)formalConcept.getElementsByTagName("parents").item(0);
					//extract the id's from parents
					NodeList parentsIds = parents.getElementsByTagName("id");
					for(int j=0; j<parentsIds.getLength(); j++) {
						Node parentId = parentsIds.item(j);
						String valueParentId = parentId.getTextContent();
						formalConceptDto.getParentsFormalConceptId().add(valueParentId);
						logger.debug("parentId: "+valueParentId);
					}
					
					//extract children from formal concept. Only there is one tab as children and inside of this, we have the list of id's (formal concepts) whose are the children
					Element children = (Element)formalConcept.getElementsByTagName("children").item(0);
					//extract the id's from children
					NodeList childrenIds = children.getElementsByTagName("id");
					for(int j=0; j<childrenIds.getLength(); j++) {
						Node childrenId = childrenIds.item(j);
						String valueChildrenId = childrenId.getTextContent();
						formalConceptDto.getChildrenFormalConceptId().add(valueChildrenId);
						logger.debug("childrenId: "+valueChildrenId);
					}
				}
				//save the formalConceptDto in the lattice
				latticeDto.getContentObjects().add(formalConceptDto);
			}
			return latticeDto;
		}
		catch(Exception exception) {
			logger.error("Exception on proccessAfcOut() method: ", exception);
			throw new InternalServerException("Exception on bubble engine creating afc out: "+exception.getMessage());
		}
	}

}
