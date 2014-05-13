package es.uned.epardo30.bubbleend.externalresource.textalytics.dto;

import java.util.List;

/**
 * Included all information which is returned from textalytics
 * 
 * @author eduardo.guillen
 *
 */
public class ResultsTextAlyticsDto {
	
	private List<AttributeDto> attributesDto;
	private List<RelationDto> contextDto;
	
	/**
	 * get all attributes (syntactic concept) which has been found during textalytics process
	 * @return List<AttributeDto>
	 */
	public List<AttributeDto> getAttributesDto() {
		return attributesDto;
	}
	public void setAttributesDto(List<AttributeDto> attributesDto) {
		this.attributesDto = attributesDto;
	}
	
	/**
	 * Get the relations list between objects and attributes
	 * @return List<RelationDto>
	 */
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
