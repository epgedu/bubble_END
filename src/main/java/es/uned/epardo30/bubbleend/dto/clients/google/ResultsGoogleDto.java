package es.uned.epardo30.bubbleend.dto.clients.google;

import java.util.ArrayList;
import java.util.List;

/**
 * Dto to saved the results returned from google service search
 * @author eduardo.guillen
 *
 */
public class ResultsGoogleDto {
	
	private List<ItemGoogleDto> itemsGoogleDto;
	
	public ResultsGoogleDto(ArrayList<ItemGoogleDto> itemsGoogleDto) {
		this.itemsGoogleDto = itemsGoogleDto;
	}

	public List<ItemGoogleDto> getItemsGoogleDto() {
		return itemsGoogleDto;
	}

	public void setItemsGoogleDto(List<ItemGoogleDto> itemsGoogleDto) {
		this.itemsGoogleDto = itemsGoogleDto;
	}
	
	
	
	
}
