package es.uned.epardo30.bubbleend.externalresource.afc.dto;

/**
 * 
 * Represents the content of an object which is used to create the lattice on afc service. id to identify the object.
 * 
 * @author Eduardo.Guillen
 *
 */
public class ContentObjectDto {

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
		return "ContentObjectDto [id="
									+this.id
									+", value="
									+this.value
									+"]";
	}
	
	@Override
	public boolean equals(Object object) {
		ContentObjectDto contentObjectDto = (ContentObjectDto) object;
		return this.getId().equals(contentObjectDto.getId()) &&
				this.getValue().equals(contentObjectDto.getValue());
	}
}
