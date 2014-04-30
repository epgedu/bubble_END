package es.uned.epardo30.bubbleend.dto.clients.textalytics;

import java.util.List;

public class ResultsTextAlyticsDto {
	
	private List<ObjectDto> objectsDto;
	private List<AttributeDto> attributesDto;
	private List<RelationDto> contextDto;
	
	public List<ObjectDto> getObjectsDto() {
		return objectsDto;
	}
	public void setObjectsDto(List<ObjectDto> objectsDto) {
		this.objectsDto = objectsDto;
	}
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
	

}
