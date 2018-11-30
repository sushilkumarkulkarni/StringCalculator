/**
 * 
 */
package com.my.model;

/**
 * @author SU360282
 *
 */
public class SubExpression implements Comparable<SubExpression> {
	
	private int priority;
	private String expression;
	private int operandPosition;
	private char operand;
	private int startIndx;
	private int endIndx;
	
	private long firstVal;
	public long getFirstVal() {
		return firstVal;
	}
	public void setFirstVal(long firstVal) {
		this.firstVal = firstVal;
	}
	public long getSecondVal() {
		return secondVal;
	}
	public void setSecondVal(long secondVal) {
		this.secondVal = secondVal;
	}
	private long secondVal;
	
	public int getStartIndx() {
		return startIndx;
	}
	public void setStartIndx(int startIndx) {
		this.startIndx = startIndx;
	}
	public int getEndIndx() {
		return endIndx;
	}
	public void setEndIndx(int endIndx) {
		this.endIndx = endIndx;
	}
		
	
	public char getOperand() {
		return operand;
	}
	public void setOperand(char operand) {
		this.operand = operand;
	}
	public int getOperandPosition() {
		return operandPosition;
	}
	public void setOperandPosition(int operandPosition) {
		this.operandPosition = operandPosition;
	}
	private long total;
	
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	@Override 
	public int compareTo(SubExpression subExpr) { 
		return  subExpr.priority-this.priority; 
	} 
	@Override 
	public int hashCode() { 
		final int prime = 31; 
		int result = 1; 
		result = prime * result + priority+startIndx+endIndx; 
		return result; 
	} 
	@Override 
	public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			} if (obj == null) {
				return false; 
			} 
			if (getClass() != obj.getClass()) {
				return false; 
				}
			SubExpression other = (SubExpression) obj; 
		
			if (startIndx!=other.startIndx || endIndx!=other.endIndx)
			{
				return false; 
			}
			if (priority != other.priority) {
				return false; 
			} 
		return true; 
		} 
	
	
		@Override 
		public String toString() {
			return String.format("%d: %d", startIndx, priority);
		}
}
