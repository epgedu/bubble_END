package es.uned.epardo30.bubbleend.externalresource.textalytics.dto;

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
	
	
}
 