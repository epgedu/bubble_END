package es.uned.epardo30.bubbleend.externalresource.textalytics.mapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.AttributeDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.RelationDto;
import es.uned.epardo30.bubbleend.externalresource.textalytics.dto.ResultsTextAlyticsDto;

public class MapperTextAlyticsXmlToDto {
	
	private static Logger logger = Logger.getLogger(MapperTextAlyticsXmlToDto.class);
	
	public void map(String xmlResponse, ResultsTextAlyticsDto resultsTextAlyticsDto, int indexItemGoogle, double relevanceConf) throws ParserConfigurationException, SAXException, IOException {
		logger.debug("MapperTextAlyticsXmlToDto.map");
		Document doc;
        Element response_node;
        DocumentBuilderFactory docBuilderFactory;
        DocumentBuilder docBuilder;
        NodeList status_list;
        Node status;
        NamedNodeMap attributes;
        Node code;
        List<AttributeDto> relevanceListAttribute = new ArrayList<AttributeDto>();
		// getting the attributes for this object
        docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docBuilderFactory.newDocumentBuilder();
		
        doc = docBuilder.parse(new ByteArrayInputStream(xmlResponse.getBytes()));
        doc.getDocumentElement().normalize();
        response_node = doc.getDocumentElement();
        logger.debug("\nTokens:");
		status_list = response_node.getElementsByTagName("status");
        status = status_list.item(0);
        attributes = status.getAttributes();
        code = attributes.item(0);
        if(!code.getTextContent().equals("0")) {
        	  logger.debug("not content");
        } else {   
        	NodeList tokenss = response_node.getElementsByTagName("token_list");
            if(tokenss.getLength() > 0) {
            	//get the temporal array which contains the attributes from xml response
          	    List<AttributeDto> temporalListAttribute = new ArrayList<AttributeDto>();
          	    this.extractTemporalListAttribute(tokenss.item(0), temporalListAttribute);
            	if(temporalListAttribute.isEmpty()) {
            		logger.debug("temporal list empty");  
            	}
            	else {
            		//select the most relevance attributes
            	    relevanceListAttribute = this.extractRelevanceListAttribute(temporalListAttribute, relevanceConf);
            	    //check if the attribute exist on main list
            	    for(int i=0; i<relevanceListAttribute.size(); i++ ) {
            	    	boolean itemFound = false;
            	    	AttributeDto attributeFound = null;
            	    	for(int j=0; j<resultsTextAlyticsDto.getAttributesDto().size() && !itemFound; j++) {
            	    		if(relevanceListAttribute.get(i).getForm().equals(resultsTextAlyticsDto.getAttributesDto().get(j).getForm()) 
            	    			&& 	relevanceListAttribute.get(i).getType().equals(resultsTextAlyticsDto.getAttributesDto().get(j).getType()) ) {
            	    			itemFound = true;
            	    			attributeFound = resultsTextAlyticsDto.getAttributesDto().get(j);
            	    		}
	            	    }
            	    	//set the data structure
        				if(!itemFound) {
        					//logger.debug("attribute not found");
        					//add the item on attributes for  final result
        					int idNewAttribute = resultsTextAlyticsDto.getAttributesDto().size()+1;
        					resultsTextAlyticsDto.getAttributesDto().add(new AttributeDto(idNewAttribute, relevanceListAttribute.get(i).getForm(), relevanceListAttribute.get(i).getType()));
        					//add the context
        					resultsTextAlyticsDto.getContextDto().add(new RelationDto(indexItemGoogle, idNewAttribute ));
        				}
        				else {
        					//add the context
        					//logger.debug("attribute found");
        					resultsTextAlyticsDto.getContextDto().add(new RelationDto(indexItemGoogle, attributeFound.getId()));
        				}
        			}
	            }
            }	
        }
	}
	
	private List<AttributeDto> extractRelevanceListAttribute(List<AttributeDto> temporalList, double relevanceConf) {
		logger.debug("MapperTextAlyticsXmlToDto.extractRelevanceListAttribute");
		logger.debug("Temporal list In:");
		for(int i=0;  i < temporalList.size(); i++) {
			logger.debug(temporalList.get(i).getForm() +" / " +temporalList.get(i).getType());
		}
		
		List<AttributeDto> relevanceListAttribute = new ArrayList<AttributeDto>();
		int nTotalAttributes = temporalList.size();
		//check every attribute in order to get the relevance
		int nItem = 1;//at least once
		for(int i = 0; i < temporalList.size(); i++) {
			nItem = 1;
			AttributeDto attributeDto = temporalList.get(i);
			String attributeItemForm = attributeDto.getForm();
			String attributeItemType = attributeDto.getType();
			for(int j = 0; j < temporalList.size(); j++) {
				if(i != j) {
					//compare form and type
					if( attributeItemForm.equals(temporalList.get(j).getForm()) && attributeItemType.equals(temporalList.get(j).getType()) ) {
						nItem ++;
						//delete the found item
						temporalList.remove(j);
						//if remove then catch the position up
						j--;
					}
				}
			}
			//get the percentage . 
			double relevance = (nItem * 100)/nTotalAttributes;
			//relevance parameter is get from yml template
			if(relevance >= relevanceConf) {
				//add to final list
				relevanceListAttribute.add(attributeDto);
			}
		}
		//check if the relevance list is empty due to there are too many attributes but just one for each. In this case, all attributes will be relevance.
		if(relevanceListAttribute.isEmpty() && !temporalList.isEmpty()) {
			for(int i = 0; i < temporalList.size(); i++) {
				relevanceListAttribute.add(temporalList.get(i));
			}
		}
		
		logger.debug("Relevance list out list In:");
		for(int i=0;  i < relevanceListAttribute.size(); i++) {
			logger.debug(relevanceListAttribute.get(i).getForm() +" / " +relevanceListAttribute.get(i).getType());
		}
		return relevanceListAttribute;
	}
	
	private void extractTemporalListAttribute(Node sentence, List<AttributeDto> temporalListAttribute) {
		
		NodeList tokens_list = sentence.getChildNodes();
		for(int i=0; i<tokens_list.getLength(); i++){
	        Node token = tokens_list.item(i);
	        NodeList child_token = token.getChildNodes();
	        boolean hasTokens = false;
	        int index = -1;
	        for(int j=0; j<child_token.getLength(); j++){
	        	Node n = child_token.item(j);
	        	String name = n.getNodeName();
	        	if(name.equals("token_list")) {
	        		hasTokens = true;
	        		index = j;
	        	} else if(name.equals("topic_list")) {
	        		NodeList tokenContent = n.getChildNodes();
	        		for(int k=0; k<tokenContent.getLength(); k++){
		            	if(tokenContent.item(k).getNodeName().equals("entity_list")) {
		            		Node topicList = tokenContent.item(k);
		            		NodeList topicListContent = topicList.getChildNodes();
		            		for(int l=0; l<topicListContent.getLength(); l++) {
		            			if(topicListContent.item(l)!= null) {
		            				if(topicListContent.item(l).getNodeName().equals("entity")) {
		            					Node entityList = topicListContent.item(l);
		            					NodeList entityListContent = entityList.getChildNodes();
		            					String formTemporalAttribute = null;
		            					String typeTemporalAttribute = null;
		            					for(int m=0; m<entityListContent.getLength(); m++) {
		            						int idtemporalAttribute = j;
		            						if(entityListContent.item(m) != null) {
		            							if(entityListContent.item(m).getNodeName().equals("form")) {
			            							  logger.debug("entity.form: "+entityListContent.item(m).getTextContent());
			            							  formTemporalAttribute = entityListContent.item(m).getTextContent();
		            							}
			            				  	  	if(entityListContent.item(m).getNodeName().equals("sementity")) {
			            				  	  		if(entityListContent.item(m)!=null) {
			            				  	  			Node sementity = entityListContent.item(m); 
			            				  	  			NodeList sementityContent = sementity.getChildNodes();
			            				  	  			for(int o=0; o<sementityContent.getLength(); o++) {
			            				  	  				if(sementityContent.item(o)!=null) {
			            				  	  					if(sementityContent.item(o).getNodeName().equals("type")) {
			            				  	  						logger.debug("entity type: "+sementityContent.item(o).getTextContent());
			            				  	  						typeTemporalAttribute = sementityContent.item(o).getTextContent();
			            				  	  						temporalListAttribute.add(new AttributeDto(idtemporalAttribute, formTemporalAttribute, typeTemporalAttribute));
			            				  	  					}
			            				  	  				}
			            				  	  			}
			            				  	  		}
			            				  	  	}
			            			  	  	}
		            					}
		            				}
		            			}	  
		            		}
		            	}
	        		}
	        	}
	        }
	        if(hasTokens) {
	        	 extractTemporalListAttribute(child_token.item(index), temporalListAttribute);
	        }
		}
	}
	
	

}
