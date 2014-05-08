package es.uned.epardo30.bubbleend.externalresource.afc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * TODO
 * 
 * @author Eduardo.Guillen
 *
 */
public class FormalConceptDto {
	
	private String conceptId;
	
	private List<ContentObjectDto> extension = new ArrayList<ContentObjectDto>();
	
	private List<ContentDescriptorDto> intension = new ArrayList<ContentDescriptorDto>();
	
	private List<String> parentsFormalConceptId =  new ArrayList<String>();
	
	private List<String> childrenFormalConceptId =  new ArrayList<String>();

	public String getConceptId() {
		return conceptId;
	}

	public void setConceptId(String conceptId) {
		this.conceptId = conceptId;
	}

	public List<ContentObjectDto> getExtension() {
		return extension;
	}

	public void setExtension(List<ContentObjectDto> extension) {
		this.extension = extension;
	}

	public List<ContentDescriptorDto> getIntension() {
		return intension;
	}

	public void setIntension(List<ContentDescriptorDto> intension) {
		this.intension = intension;
	}

	public List<String> getParentsFormalConceptId() {
		return parentsFormalConceptId;
	}

	public void setParentsFormalConceptId(List<String> parentsFormalConceptId) {
		this.parentsFormalConceptId = parentsFormalConceptId;
	}

	public List<String> getChildrenFormalConceptId() {
		return childrenFormalConceptId;
	}

	public void setChildrenFormalConceptId(List<String> childrenFormalConceptId) {
		this.childrenFormalConceptId = childrenFormalConceptId;
	}

	@Override
	public String toString() {
		return "FormalConceptDto [conceptId="
									+this.conceptId
									+", extension="
									+this.extension
									+", intension="
									+this.intension
									+", parentsFormalConceptId="
									+this.parentsFormalConceptId
									+", children="
									+this.childrenFormalConceptId
									+"]";
	}
}