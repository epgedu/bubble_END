package es.uned.epardo30.bubbleend.externalresource.textalytics.dto;

/**
 * Represents every attribute (syntactic concept) extracted from the analysed text from textalytics 
 * 
 * @author eduardo.guillen
 *
 */
public class AttributeDto {
	
	private int id;
	private String form;
	private String type;
	
	public AttributeDto(int id, String form, String type) {
		this.id = id;
		this.form = form;
		this.type = type;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object object) {
		AttributeDto attributeDto = (AttributeDto)object;
		return this.getId() == attributeDto.getId() &&
				this.getForm().equals(attributeDto.getForm()) &&
				this.getType().equals(attributeDto.getType());
	}
	
}
