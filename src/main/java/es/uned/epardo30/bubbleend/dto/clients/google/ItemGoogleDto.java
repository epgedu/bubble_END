package es.uned.epardo30.bubbleend.dto.clients.google;

/**
 * Dto to saved each items returned from google service search
 * @author eduardo.guillen
 *
 */
public class ItemGoogleDto {
	
	private String title;
	private String htmlFormattedUrl;
	private String snnipet;
	private String description;
	
	public ItemGoogleDto(String title, String htmlFormattedUrl, String snnipet, String description) {
		this.title = title;
		this.htmlFormattedUrl = htmlFormattedUrl;
		this.snnipet = snnipet;
		this.description = description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
