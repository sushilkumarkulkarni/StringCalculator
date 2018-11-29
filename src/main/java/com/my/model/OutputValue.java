/**
 * 
 */
package com.my.model;

/**
 * @author Sushil
 *
 */
public class OutputValue {
	
	private String givenExpression;
	private Long calculatedTotal;
	private String message;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getGivenExpression() {
		return givenExpression;
	}
	public void setGivenExpression(String givenExpression) {
		this.givenExpression = givenExpression;
	}
	public Long getCalculatedTotal() {
		return calculatedTotal;
	}
	public void setCalculatedTotal(Long calculatedTotal) {
		this.calculatedTotal = calculatedTotal;
	}
	

}
