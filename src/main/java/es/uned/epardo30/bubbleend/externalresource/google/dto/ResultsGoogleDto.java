package es.uned.epardo30.bubbleend.externalresource.google.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto to saved the results returned from google service search
 * @author eduardo.guillen
 *
 */
public class ResultsGoogleDto {
	
	private List<ItemGoogleDto> itemsGoogleDto;
	
	/**
	 * 
	 * @param itemsGoogleDto :  ArrayList<ItemGoogleDto> List where the items will be saved
	 */
	public ResultsGoogleDto(ArrayList<ItemGoogleDto> itemsGoogleDto) {
		this.itemsGoogleDto = itemsGoogleDto;
	}
	
	/**
	 * Get all items saved in ResultGoogleDto
	 * @return List<ItemGoogleDto>
	 */
	public List<ItemGoogleDto> getItemsGoogleDto() {
		return itemsGoogleDto;
	}

	public void setItemsGoogleDto(List<ItemGoogleDto> itemsGoogleDto) {
		this.itemsGoogleDto = itemsGoogleDto;
	}
	
	@Override
	public boolean equals(Object resultsGoogleDto) {
		
		ResultsGoogleDto resultsGoogle =(ResultsGoogleDto)resultsGoogleDto;
		boolean equals = true;
		
		if(this.getItemsGoogleDto().size() == resultsGoogle.getItemsGoogleDto().size()) {
			for(int i=0; i<this.getItemsGoogleDto().size() && equals; i++) {
				equals = this.getItemsGoogleDto().get(i).equals(resultsGoogle.getItemsGoogleDto().get(i));
			}
		}
		return equals;
	}
	
	
	
	
}
