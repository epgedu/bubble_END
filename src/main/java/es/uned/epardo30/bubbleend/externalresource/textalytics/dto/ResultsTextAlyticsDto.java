package es.uned.epardo30.bubbleend.externalresource.textalytics.dto;

import java.util.List;

public class ResultsTextAlyticsDto {
	
	//private List<ObjectDto> objectsDto;
	private List<AttributeDto> attributesDto;
	private List<RelationDto> contextDto;
	
	public List<AttributeDto> getAttributesDto() {
		return attributesDto;
	}
	public void setAttributesDto(List<AttributeDto> attributesDto) {
		this.attributesDto = attributesDto;
	}
	public List<RelationDto> getContextDto() {
		return contextDto;
	}
	public void setContextDto(List<RelationDto> contextDto) {
		this.contextDto = contextDto;
	}
	
	@Override
	public boolean equals(Object object) {
		ResultsTextAlyticsDto resultsTextAlyticsDto = (ResultsTextAlyticsDto)object;
		return this.getAttributesDto().equals(resultsTextAlyticsDto.getAttributesDto()) &&
				this.getContextDto().equals(resultsTextAlyticsDto.getContextDto());
	}
	
}
