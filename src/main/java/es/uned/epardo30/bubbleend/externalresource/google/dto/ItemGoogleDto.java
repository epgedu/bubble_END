package es.uned.epardo30.bubbleend.externalresource.google.dto;

/**
 * Dto to saved each items returned from google service search
 * @author eduardo.guillen
 *
 */
public class ItemGoogleDto {
	
	private String title;
	private String htmlFormattedUrl;
	private String snnipet;
	
	public ItemGoogleDto(String title, String htmlFormattedUrl, String snnipet) {
		this.title = title;
		this.htmlFormattedUrl = htmlFormattedUrl;
		this.snnipet = snnipet;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHtmlFormattedUrl() {
		return htmlFormattedUrl;
	}
	public void setHtmlFormattedUrl(String htmlFormattedUrl) {
		this.htmlFormattedUrl = htmlFormattedUrl;
	}
	public String getSnnipet() {
		return snnipet;
	}
	public void setSnnipet(String snnipet) {
		this.snnipet = snnipet;
	}
	
	@Override
	public boolean equals(Object object) {
		ItemGoogleDto itemGoogleDto = (ItemGoogleDto)object;
		return this.getTitle().equals(itemGoogleDto.getTitle()) && this.getHtmlFormattedUrl().equals(itemGoogleDto.getHtmlFormattedUrl()) && this.getSnnipet().equals(itemGoogleDto.getSnnipet());
	}
	

}
