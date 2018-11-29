/**
 * 
 */
package com.my.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.my.model.CalculatedValue;
import com.my.model.InputValue;
import com.my.model.OutputValue;
import com.my.util.CalculatorConstants;

/**
 * @author Sushil
 *
 */
public class CalculatorService {

	/**
	 * @param args
	 */
	
	private static Logger logger =LoggerFactory.getLogger(CalculatorService.class);
	
	private CalculatedValue calculatedValue;
	private InputValue inputValue;
	
	public static void main(String[] args) {
	  int expressionCount=2;
	  String expressionValues[]= {"7+(67(56*2))","5/2*4"};
	  CalculatorService calService=new CalculatorService();
	  calService.inputValue=new InputValue();
	  calService.inputValue.setGivenExpressions(expressionValues);
	  calService.inputValue.setNumberOfExpressions(expressionCount);
	  calService.calculateTotals(calService.inputValue);
	  

	}
	
	private CalculatedValue calculateTotals(InputValue inputValue) {
		calculatedValue =new CalculatedValue();
		calculatedValue.setCalculatedTotalList(new ArrayList<OutputValue>());
		if(inputValue.getNumberOfExpressions()!=inputValue.getGivenExpressions().length) {
			OutputValue outputValue=new OutputValue();
			outputValue.setMessage(CalculatorConstants.CALCULATED_INCORRECT_NUMBEROF_EXPRESSIONS_MSG);
			calculatedValue.getCalculatedTotalList().add(outputValue);
		}else {
			String express[]=inputValue.getGivenExpressions();
			for(int i=0;i<express.length;i++) {
				String strExpr=express[i];
				OutputValue outputValue=new OutputValue();
				outputValue.setGivenExpression(strExpr);
				if(validateExpressions(strExpr)) {
					
				}else {
					outputValue.setMessage(CalculatorConstants.CASE_NUMBER+i+CalculatorConstants.INVALID_EXPRESSION_MSG);	
				}
			}
			
		}
		return calculatedValue;
		
	}
	
	private long applyRules(String exprss) {
		long calculatedTotal=0;
		char [] charExpr=exprss.toCharArray();
		int startIndx=exprss.lastIndexOf('(');
		String strVal=exprss.substring(startIndx,exprss.length());
		int endIndx=strVal.indexOf(')');
		
		String actualstrExpr=exprss.substring(startIndx+1,endIndx);
		System.out.println("CalculatorService.applyRules()"+actualstrExpr);
		char [] actualExpr=actualstrExpr.toCharArray();
		String firstVal="";
		String secondVal="";
		boolean isFirstFound=false;
		boolean isOperatorFound=false;
		char foundOperand;
		for(char chr:actualExpr) {
			if(isValidNumber(""+chr)) {
				if(!isFirstFound) {
					firstVal=firstVal+chr;
				}else {
					secondVal=secondVal+chr;
				}
			}else {
				foundOperand=chr;
			}
			switch(chr) {
			case '/':
				isFirstFound=true;
				if(!secondVal.equals("")) {
					isOperatorFound=true;
					calculatedTotal=Long.parseLong(firstVal)/Long.parseLong(secondVal);
				}
				break;
			case '*':
				isFirstFound=true;
				if(!secondVal.equals("")) {
					isOperatorFound=true;
					calculatedTotal=Long.parseLong(firstVal)*Long.parseLong(secondVal);
				}
				break;
			
			}
		}
		return calculatedTotal;
		
	}
	
	private boolean validateExpressions(String expression) {
		boolean isValid=false;
		if(validateBrackets(expression)) {
			if(validateOperators(expression)){
				isValid=true;
			}
		}
		
		return isValid;
		
	}
	
	private boolean validateBrackets(String strExpr) {
		char[] charExpr=strExpr.toCharArray();
		int countOpeningBrackets=0;
		int countClosingBrackets=0;
		boolean isValid=false;
		for(char chr:charExpr) {
			if(chr=='(') {
				countOpeningBrackets++;
			}else if(chr==')') {
				countOpeningBrackets++;
			}
		}
		
		if(countOpeningBrackets==countClosingBrackets) {
			isValid=true;
		}
		return isValid;
	}
	
	private boolean validateOperators(String strExpr) {
		String strValue=new String (strExpr);
		strValue=strExpr.replaceAll("(", "");
		strValue=strExpr.replaceAll(")", "");
		char[] charExpr=strValue.toCharArray();
		boolean isValid=true;
		
		outer:
		for(int i=1;i<charExpr.length;i++) {
			switch (charExpr[i]) {
			case'+':
				if(isSequentialOperators(charExpr[i-1],charExpr[i],charExpr[i+1])) {
					isValid=false;
					break outer;
				}
				break;
				
			case'-':
				if(isSequentialOperators(charExpr[i-1],charExpr[i],charExpr[i+1])) {
					isValid=false;
					break outer;
				}
				break;
				
			case'*':
				if(isSequentialOperators(charExpr[i-1],charExpr[i],charExpr[i+1])) {
					isValid=false;
					break outer;
				}
				break;
				
			case'/':
				if(isSequentialOperators(charExpr[i-1],charExpr[i],charExpr[i+1])) {
					isValid=false;
					break outer;
				}
				break;
				
			case'^':
				if(isSequentialOperators(charExpr[i-1],charExpr[i],charExpr[i+1])) {
					isValid=false;
					break outer;
				}
				break;
				
			}
			    
		}
		return isValid;
	}
	
	
	private boolean isSequentialOperators(char charPrev,char charActual, char charNext) {
		boolean isSequential=false;
		if(charPrev==charActual || charActual==charNext) {
			isSequential=true;
	    }
		return isSequential;
	}
	
	private boolean isValidNumber(String strValue) {
		boolean isNumeric=true;
		try {
			Long.parseLong(strValue);
			
		}catch(NumberFormatException ne) {
			isNumeric=false;
		}
		return isNumeric;
		
	}
	
	

}
