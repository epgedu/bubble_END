package es.uned.epardo30.bubbleend.dto;

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
}
