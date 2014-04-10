package es.uned.epardo30.bubbleend.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.uned.epardo30.bubbleend.BubbleEndPointService;
import es.uned.epardo30.bubbleend.dto.ContentDescriptorDto;
import es.uned.epardo30.bubbleend.dto.ContentObjectDto;
import es.uned.epardo30.bubbleend.dto.FormalConceptDto;
import es.uned.epardo30.bubbleend.dto.LatticeDto;
import es.uned.epardo30.bubbleend.exceptions.InternalServerException;


public class BubbleEngine {
	
	static Logger logger = Logger.getLogger(BubbleEngine.class);
	
	public LatticeDto workflowEngine() {
		//llamaremos al local method callToGoogleClient
		//este nos devuelve un xml el cual tiene que ser procesadoo para obtener los descriptores y devuelve la entrada al AFC
		//llamamos al local method callToAfcClient
		//llamamos al metodo que procesa el XML de salida del AFC para quedarnos con la informacion que necesitamos y contruir el objeto Searching,
		//que es el objeto JSON que pasaremos a la interface. 
		
		
			this.callToGoogleClient();
			this.processGoogleOut();
			this.callTextalyticsProcess();
			this.createAfcIn();
			File xmlFile = this.callToAfcClient();
			return this.processAfcOut(xmlFile);
		
	}
	
	private void callToGoogleClient() {
		//llamaremos al servicio resources.GoogleClient
		try {
			//int i = 6/0;
		}
		catch(Exception exception) {
			logger.error("Exception on callToGoogleClient() method: ", exception);
			throw new InternalServerException("Exception on service google: "+exception.getMessage());
		}
	
	}
	
	
	private void processGoogleOut() {
		//procesamos el xml de salida del servicio de busqueda, para obtener los descriptores. Devuelve el xml de entrada a AFC
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on processGoogleOut() method: ", exception);
			throw new InternalServerException("Exception on bubble engine processing: "+exception.getMessage());
		}
	}
	
	public void callTextalyticsProcess() {
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on callTextalyticsProcess() method: ", exception);
			throw new InternalServerException("Exception on Textalytics process: "+exception.getMessage());
		}
	}
	
	public void createAfcIn() {
		try {
		}
		catch(Exception exception) {
			logger.error("Exception on createAfcIn() method: ", exception);
			throw new InternalServerException("Exception on bubble engine creating afc in: "+exception.getMessage());
		}
	}
	
	
	private File callToAfcClient() {
		//llamamos al cliente del servicio AFC, obteniedo el XML como resultado
		try {
			//read the result xml from AFC services
			File xmlFile = new File("c:/uned/bubble/conf/salida.xml");
			return xmlFile;
			
				 	
		 	
		 	
		 	//set the content of every descriptor
		 
			
			
			
		}
		catch(Exception exception) {
			logger.error("Exception on callToAfcClient() method: ", exception);
			throw new InternalServerException("Exception on AFC service: "+exception.getMessage());
		}
	}
	
	
	/**
	 * Process the xml given from afc service
	 * That way, we have parsed the lattice. But the lattice has not got the values of objetos tab, only provides the id object but not the value. The same happens with descriptores, i.e
			we can get the descriptores ids for one formal object but we don't know the value descriptores. 
			In order to get the values (objetos and descriptores), we have the tabs "objetosContenido" where the value is related with the id object.
			And we have the tabs "descriptores contenido", where we can get the relation between value descriptor and id descriptor
			Therefore, first option would be, parser the tags "descriptores contenido" and "objetos contenido" and send them through dto object toward the bubble gui. With this option, we will have
			to get the relation between "objetos" and its values and the same for descrptores , on javascript code looking for on the json object until to find the id object or id descriptor and then
			to extract its value.    
			Therefore, we will change the lattice structure and for each formal concept:
								- For each id extension, we include its objeto value(description, url, scripter). We know the value from objetoContenido tag
			                    - For each id intension, we include its descriptor value (descriptor name). We know the value from descriptorContenido tag
			                    - For each id parents, we don't include the value, becasue the id links with another formal concept (in lattice)
			                    - For each id children, we don't include the value, because the id links with another formal concept (in lattice)
			
			Therefore, maybe we are sending more amount of informatio, because we are sending the same values in differents formal concept but we have two adventages:
			                  - We don't need to send the information about "objeto contenido" and "descriptor contenido". Neither the information about "objetos" tag, "atributos" tag and "relacion" tag, due to
		  					    all this information is alaready on the lattice.
			
	 * @return LatticeDto
	 */
	private LatticeDto processAfcOut(File xmlFile) {
		logger.debug("BubbleEngine.processAfcOut");
		try {
			//reading the xml file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(xmlFile);
			
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
