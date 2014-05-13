package es.uned.epardo30.bubbleend.externalresource.afc.dto;

/**
 * Represents the content of a descriptor which is used to create the lattice on afc service.
 * 		
 * @author eduardo.guillen
 *
 */
public class ContentDescriptorDto {

	private String id;
	private String value;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "ContentDescriptorDto [id="
									+this.id
									+", value="
									+this.value
									+"]";
	}
	
	@Override
	public boolean equals(Object object) {
		ContentDescriptorDto contentDescriptorDto = (ContentDescriptorDto) object;
		return this.getId().equals(contentDescriptorDto.getId()) &&
				this.getValue().equals(contentDescriptorDto.getValue());
		
	}
}
