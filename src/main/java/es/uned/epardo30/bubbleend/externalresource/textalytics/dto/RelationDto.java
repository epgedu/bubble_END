package es.uned.epardo30.bubbleend.externalresource.textalytics.dto;

/**
 * Represents the relations between the objects (returned from google service) and the attributes (syntactic concepts) extracted for each object from textanalytics 
 * @author eduardo.guillen
 *
 */
public class RelationDto {
	
	private int idObject;
	private int idDescriptor;
	
	public RelationDto(int idObject, int idDescriptor) {
		this.idObject = idObject;
		this.idDescriptor = idDescriptor;
	}
	
	public int getIdObject() {
		return idObject;
	}
	public void setIdObject(int idObject) {
		this.idObject = idObject;
	}
	public int getIdDescriptor() {
		return idDescriptor;
	}
	public void setIdDescriptor(int idDescriptor) {
		this.idDescriptor = idDescriptor;
	}
	
	@Override
	public boolean equals(Object object) {
		RelationDto relationDto = (RelationDto) object;
		return this.getIdObject() == relationDto.getIdObject() &&
				this.getIdDescriptor() == relationDto.getIdDescriptor();
	}
}
 