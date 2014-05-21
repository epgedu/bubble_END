package es.uned.epardo30.bubbleend.externalresource.afc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Our service needs to conform to an industry standard, RFC 1149, which specifies the following JSON representation:
 * { LatticeDto [contentObjects=contentObject Json	]"; 
 * }
 * 
 * Represents the lattice which will be returned from bubble_END toward bubble_GUI
 * 
 * @author Eduardo.Guillen
 *
 */
public class LatticeDto {
	
	List<FormalConceptDto> contentObjects = new ArrayList<FormalConceptDto>();
	int totalResult;

	/**
	 * Contains every formal concept who's made the lattice
	 * @return List<FormalConceptDto>
	 * @see FormalConceptDto
	 */
	public List<FormalConceptDto> getContentObjects() {
		return contentObjects;
	}

	public void setContentObjects(List<FormalConceptDto> contentObjects) {
		this.contentObjects = contentObjects;
	}
	
	
	/**
	 * Return the amount of results whose have been processed
	 * @return int
	 */
	public int getTotalResult() {
		return totalResult;
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	@Override
	public String toString() {
		return "LatticeDto [contentObjects="
							+this.contentObjects
							+",totalResults="
							+this.totalResult
							+"]";
	}
	
	@Override
	public boolean equals(Object object) {
		LatticeDto latticeDto = (LatticeDto)object;
		return (this.getContentObjects().equals(latticeDto.getContentObjects()) && this.getTotalResult() == latticeDto.getTotalResult() );
	}
}
