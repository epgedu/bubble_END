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
			File xmlFile = new File("c:/uned/bubble/conf/tiny-afc.xml");
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
	 * 
	 * @return
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
			//creamos el latticeDto
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
						contentObjectDto.setValue(idValueObject.get(valueExtensionId));
						formalConceptDto.getExtension().add(contentObjectDto);
						logger.debug("extensionId: "+valueExtensionId+ " extensionValue: "+idValueObject.get(valueExtensionId));
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
						contentDescriptorDto.setValue(idValueAttribute.get(valueIntensionId));
						formalConceptDto.getIntension().add(contentDescriptorDto);
						logger.debug("intensionId: "+valueIntensionId+ " intensionValue: "+idValueAttribute.get(valueIntensionId));
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
			
			//That way, we have parsed the lattice. But the lattice has not got the values of objetos tab, only provides the id object but not the value. The same happens with descriptores, i.e
			//we can get the descriptores ids for one formal object but we don't know the value descriptores. 
			//In order to get the values (objetos and descriptores), we have the tabs "objetosContenido" where the value is related with the id object.
			//And we have the tabs "descrptores contenido", where we can get the relation between value descriptor and id descriptor
			//Therefore, one first option would be, parser the tags "descriptores contenido" and "objetos contenido" and send them througth dto object toward the bubble gui. With this option, we will have
			//to get the relation between "objetos" and its values and the same for descrptores , on javascript code. 
			//Therefore, we will change the lattice structure and for each formal concept:
			//					- For each id extension, we include its objeto value(description, url, scripter). We know the value from objetoContenido tag
			//                  - For each id intension, we include its descriptor value (descriptor name). We know the value from descriptorContenido tag
			//                  - For each id parents, we don't include the value, becasue the id links with another formal concept (in lattice)
			//                  - For each id children, we don't include the value, because the id links with another formal concept (in lattice)
			//
			//Therefore, maybe we are sending more amount of informatio, because we are sending the same values in differents formal concept but we have two adventages:
			//                  - We don't need to send the information about "objeto contenido" and "descriptor contenido". Neither the information about "objetos" tag, "atributos" tag and "relacion" tag, due to
			//					  all this information is alaready on the lattice.
			
			
			//MEMORIA: FASE 4.3. Incluir todo el javadoc que desarrollemos para el codigo de la 4.3 y la explicacion de cada paso del parseado del xml. Incluir la explicacion de  arriba, la que viene a decir 
			// que: El lattice del xml devuelto por afc, contiene toda la informacion que necesitamos para la interfaz (el id de los objetos , el id de los descriptores , la relacion entre los objetos y 
			//descriptores mediante el objeto concepto formal), por lo tanto no es necesario que enviemos la informacion contenida en las etiquetas "objetos" , pq los ids estan en el lattice, tampo "atributos" pq los ids
			//estan en el lattice, y tampoco contexto (las relaciones entre objetos y descriptores) porque esto tambien se encuentra en el lattice. Toda esta informacion externa al lattice es el xml que le enviamos
			//como entrada al servicio afc, generado a partir del analisis sintactico de los resultados del servicio de google:
			//	Podemos decir que: Google obtiene los resultados, el servicio de analisis, establece las relaciones entre los atributos y resultados y afc me proporciona el objetivo de la practica que es la organizacon de resultados de 
			//busqueda. 
			// Pues bien, ademas del lattice, tambien necesitariamos enviar a la interfaz el contenido de la etiqueta "contenido" que contiene el contenido de cada descriptor (id descriptor) y de cada objeto (id objeto). 
			//Aqui es donde hacemos un pequeno cambio y vamoms rellenando el objeto latticeDto, no solo con la informacion del lattice sino que tambien incluimos el valor correspondiente a cada objeto y descriptor. 
			//Con esto tenemos un punto negativo y es que enviaremos informacion duplicada a la interfaz, ya que un objeto y descriptor pueden estar en uno o mas objetos formales. 
			//Logicamente evitamos enviar el contenido de las etiquetas "contenido" porque lo acabamos de incluir dentro del lattice. En terminos de base de datos, podriamos decir que estamos desformalizando la estructura de la informacion, 
			//enviado informacion duplicada. Cuando de la otra forma solo se duplican los id's pero los valores se envian una unica vez y por separado del lattice. 
			// Pero el motivo de disminuir el nivel de formalizacion del lattice, es para conseguir que la relacion id-valor este resulta en el backend cuyo rendimiento es superior al frontend. Es decir, la interfaz no debe buscar 
			//el valor asociado a un detrminado id, recorriendo una largo vector hasta encontrar el id buscado, sino que esa busqueda se realiza previamente en java, cuyo accesos a estrcutura de datos y manejo de informacion es mas rapido que 
			// que hacerlo en el propio cliente. Por lo tanto, aumentamos el trafico de informacion entre ambas arquitecturas pero disminuimos el tiempo de procesado en la interfaz y con ello el tiempo de latencia de la peticion. 
			// TODO: Incluir tambien en la memoria un esquema del xml devuelto por el afc, definiendo que partes son enviadas a la interfaz y que partes no. 
			
			//Por lo tanto, hasta ahora tenemos el latticeDto con los ids objetos, ids descriptores, las relaciones entre ellos(obj-descriptor)mediante los conceptos formales y las relaciones entre los conceptos formales (parents and children)
			//Lo unico que tenemos que rellenar es en cada concepto formal, el valor de cada id objeto y cada id descriptor. 
			
			//Por lo tanto, lo primero sera extraer la informacio de etiqueta contenido. Pero lo vamos hacer antes de calcular el lattice, de esa forma cuando contruya el lattice ya tengo las estrucutras de datos almacenando los valores
			// y al mismo tiempo que construimos el lattice inlcuimos los valores de objetos y descriptores. Por lo tanto, lo que ibamos hacer a continuacion , lo hacemos al ppio del metodo, obteniedo valores de objetos y descriptores.
			//Para ello, utilizamos los recursos de java para el manejo de estructuras de datos. Si guardasemos la relacion id-valor tanto para objetos como para descriptores en dos ArrayList normales, cuando quisieramos obtener el valor para un 
			//determinado id (para rellenar el reticulo con los valores),tendriamos que buscar en toda la lista donde guardamos los objetos hasta encontrar el id que buscamos y lo mismo para los descriptores con su respectiva lista
			//Para evitar esto, http://lineadecodigo.com/java/usar-una-hashtable-java/ vamos hacer uso de la funcion hash... en el enlace anterior esta documentado.
			
			//Sin embargo para para guardar las relaciones entre conceptos formales (padres e hijos), guardamos unicamente el id del concepto relacionado. Ya que si guardasemos todo el concepto formal relacionado, tendriamos una
			//estructura de datos de un peso muy elevado y en este punto ya no conviene el aumento de datos a transferir con el el tiempo de busqueda en el frontend. 
			
			//Para llevar a cabo las pruebas, creamos un xml mucho mas reducido tiny-afc.xml y vamos comprobandos los datos (DOJO Objects) que recibimos en el frontend.
			//Mostrar en la memoria el xml utilizado para las pruebas. No solo el xml, sino haces una representacion grafica de los conceptos formales que contiene el lattice y la relacion (padres e hijos entre ellos).
		
			return latticeDto;
		}
		catch(Exception exception) {
			logger.error("Exception on proccessAfcOut() method: ", exception);
			throw new InternalServerException("Exception on bubble engine creating afc out: "+exception.getMessage());
		}
		
	}
	
	

}
