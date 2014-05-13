package es.uned.epardo30.bubbleend.externalresource.afc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a formal concept in the lattice which has been created in afc service
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

	/**
	 * Get the object list which represents the associated documents to the formal concept.
	 * @return List<ContentObjectDto>
	 * @see ContentObjectDto
	 */
	public List<ContentObjectDto> getExtension() {
		return extension;
	}

	public void setExtension(List<ContentObjectDto> extension) {
		this.extension = extension;
	}
	
	/**
	 * Get the descriptor list which represents the associated descriptors to the formal concept
	 * @return List<ContentDescriptorDto>
	 * @see ContentDescriptorDto
	 */
	public List<ContentDescriptorDto> getIntension() {
		return intension;
	}

	public void setIntension(List<ContentDescriptorDto> intension) {
		this.intension = intension;
	}

	/**
	 * Get the parents nodes for the formal concept
	 * @return List<String>
	 * 
	 */
	public List<String> getParentsFormalConceptId() {
		return parentsFormalConceptId;
	}

	public void setParentsFormalConceptId(List<String> parentsFormalConceptId) {
		this.parentsFormalConceptId = parentsFormalConceptId;
	}

	/**
	 * Get the children nodes for the formal concept
	 * @return List<String>
	 */
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
	
	@Override
	public boolean equals(Object object) {
		FormalConceptDto formalConceptDto = (FormalConceptDto) object;
		return this.getConceptId().equals(formalConceptDto.getConceptId()) &&
				this.getExtension().equals(formalConceptDto.getExtension()) &&
				this.getIntension().equals(formalConceptDto.getIntension()) &&
				this.getParentsFormalConceptId().equals(formalConceptDto.getParentsFormalConceptId()) &&
				this.getChildrenFormalConceptId().equals(formalConceptDto.getChildrenFormalConceptId());
	}
}