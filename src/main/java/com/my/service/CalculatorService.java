/**
 * 
 */
package com.my.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.my.model.CalculatedValue;
import com.my.model.InputValue;
import com.my.model.OutputValue;
import com.my.model.SubExpression;
import com.my.util.CalculatorConstants;

/**
 * @author Sushil
 *
 */
@Service
public class CalculatorService {

	/**
	 * @param args
	 */
	
	private static Logger logger =LoggerFactory.getLogger(CalculatorService.class);
	
	private CalculatedValue calculatedValue;
	private InputValue inputValue;
	
	public static void main(String[] args) {
	  int expressionCount=1;
	  String expressionValues[]= {"7+(6*5^2+3-4/2)"};//158//{"2+3*4/2"};//8,
	  CalculatorService calService=new CalculatorService();
	  calService.inputValue=new InputValue();
	  calService.inputValue.setGivenExpressions(expressionValues);
	  calService.inputValue.setNumberOfExpressions(expressionCount);
	  calService.calculateTotals(calService.inputValue);
	  

	}
	
	public CalculatedValue calculateTotals(InputValue inputValue) {
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
					outputValue.setCalculatedTotal(applyRules(strExpr));
					outputValue.setMessage(CalculatorConstants.CASE_NUMBER+(i+1)+":"+outputValue.getCalculatedTotal());
				}else {
					outputValue.setMessage(CalculatorConstants.CASE_NUMBER+(i+1)+":"+CalculatorConstants.INVALID_EXPRESSION_MSG);	
				}
				
				calculatedValue.getCalculatedTotalList().add(outputValue);
			}
			
		}
		//print values
		for(OutputValue outputVal:calculatedValue.getCalculatedTotalList()){
			System.out.println(outputVal.getMessage());
		}
		return calculatedValue;
		
	}
	
	private long applyRules(String exprss) {
		long calculatedTotal=0;
		
		if(exprss.indexOf('(')>-1){
			while(exprss.indexOf('(')>-1){
				int startIndx=exprss.lastIndexOf('(');
				int endIndx=exprss.indexOf(')');
				
				String actualstrExpr=exprss.substring(startIndx+1,endIndx);
				System.out.println("CalculatorService.applyRules()"+actualstrExpr);
				calculatedTotal=totalSum(actualstrExpr);
				String strSub1=exprss.substring(0,startIndx);
				String strSub2=exprss.substring(endIndx+1,exprss.length());
				System.out.println("CalculatorService.applyRules() sbstr1"+strSub1);
				System.out.println("CalculatorService.applyRules() sbstr2"+strSub2);
				
				exprss=strSub1+calculatedTotal+strSub2;
				
			}
		}else{
			
			calculatedTotal=totalSum(exprss);
		}
		
		return calculatedTotal;
		
	}
	
	private int getPriorityPosition(char chr){
		int precendence=0;
			switch(chr) {
			case '/':
				precendence=5;
				break;
			case '*':
				precendence=4;
				break;
				
			case '+':
				precendence=3;
				break;
				
			case '-':
				precendence=2;
				break;
			
			case '^':
				precendence=1;
				break;
			default:
				break;
			
			}
			return precendence;
		
	}
	
	private long totalSum(String actualstrExpr){
		long calculatedTotal=0;
		char [] actualExpr=actualstrExpr.toCharArray();
		String firstVal="";
		String secondVal="";
		boolean isFirstFound=false;
		char foundOperand=1;
		boolean isOperandFound=false;
		
		List <SubExpression> positionList=new ArrayList<SubExpression>();
		int positionOne=0;
		int startIndx=-1;
		int endIndx=-1;
		for(int i=0;i<actualExpr.length;i++) {
			char chr=actualExpr[i];
			if(isValidNumber(""+chr)) {
				if(!isFirstFound) {
					if(startIndx==-1){
						startIndx=i;
					}
					firstVal=firstVal+chr;
					
				}else {
					if(endIndx==-1){
						endIndx=i;
					}
					secondVal=secondVal+chr;
				}
			}else {
				isFirstFound=true;
				if(!secondVal.equals("")) {
					
					SubExpression subExpression=new SubExpression();
					subExpression.setStartIndx(startIndx);
					subExpression.setEndIndx(endIndx);
					subExpression.setPriority(positionOne);
					
					subExpression.setFirstVal(Long.valueOf(firstVal));
					subExpression.setSecondVal(Long.valueOf(secondVal));
					subExpression.setOperand(foundOperand);
					positionList.add(subExpression);
					
					isOperandFound=false;
					firstVal=secondVal;
					secondVal="";
					startIndx=-1;
					endIndx=-1;
				}
				
				if(!isOperandFound){
					isOperandFound=true;
					foundOperand=chr;
					positionOne=getPriorityPosition(chr);
				}
				
			}
				
				if(i==actualExpr.length-1){
					if(!secondVal.equals("")) {
						SubExpression subExpression=new SubExpression();
						subExpression.setStartIndx(startIndx);
						subExpression.setEndIndx(endIndx);
						subExpression.setPriority(positionOne);
						
						
						subExpression.setFirstVal(Long.valueOf(firstVal));
						subExpression.setSecondVal(Long.valueOf(secondVal));
						subExpression.setOperand(foundOperand);
						positionList.add(subExpression);
						
						isOperandFound=false;
						firstVal=secondVal;
						secondVal="";
						startIndx=-1;
						endIndx=-1;
					}
				}
			
		}
		//System.out.println("CalculatorService.totalSum() "+positionList.size());
		Collections.sort(positionList);
		boolean first=false;
		String tmpVal="";
		List <SubExpression> subTotalList=new ArrayList<SubExpression>();
		for(SubExpression subExpression:positionList){
			firstVal=""+subExpression.getFirstVal();
			secondVal=""+subExpression.getSecondVal(); 
			
			SubExpression subExpre =new SubExpression();
			char operand=subExpression.getOperand();
			if(first && tmpVal.equals(firstVal)){
				secondVal=""+calculatedTotal;
				calculatedTotal=getSubTotal(operand, firstVal, secondVal);
			}else{
				int indx=-1;
				if(subExpression.getStartIndx()==0){
					indx=actualstrExpr.indexOf(secondVal);
					indx++;
				}else{
					indx=actualstrExpr.indexOf(firstVal);
					indx--;
				}
				subExpre.setOperand(actualstrExpr.charAt(indx));
				subExpre.setTotal(getSubTotal(operand, firstVal, secondVal));
				subExpre.setFirstVal(subExpre.getTotal());
				subExpre.setTotal(getSubTotal(operand, firstVal, secondVal));
			}
			subTotalList.add(subExpre);
			tmpVal=firstVal;
			first=true;
			
			
		}
		//last calculations
		/*if(calculatedTotal>0){
			firstVal=Long.toString(calculatedTotal);
		}*/
		return calculatedTotal;//getSubTotal(foundOperand, firstVal, secondVal);
	}
	/*private long totalSum(String actualstrExpr){
		long calculatedTotal=0;
		char [] actualExpr=actualstrExpr.toCharArray();
		String firstVal="";
		String secondVal="";
		boolean isFirstFound=false;
		char foundOperand=1;
		boolean isOperandFound=false;
		for(char chr:actualExpr) {
			if(isValidNumber(""+chr)) {
				if(!isFirstFound) {
					isOperandFound=false;
					firstVal=firstVal+chr;
				}else {
					secondVal=secondVal+chr;
				}
			}else {
				
				if(!secondVal.equals("")) {
					if(calculatedTotal>0){
						firstVal=Long.toString(calculatedTotal);
					}
					calculatedTotal=getSubTotal(foundOperand, firstVal, secondVal);
					secondVal="";
					firstVal="";
				}
				
				if(!isOperandFound){
					isOperandFound=true;
					foundOperand=chr;
					isFirstFound=true;
				}
				
				isOperandFound=false;
				
				
			}
			
		}
		//last calculations
		if(calculatedTotal>0){
			firstVal=Long.toString(calculatedTotal);
		}
		return getSubTotal(foundOperand, firstVal, secondVal);
	}*/
	
	private long getSubTotal(char chr,String firstVal,String secondVal){
		long subTotal=0;
		switch(chr) {
		case '^':
			subTotal=Long.parseLong(firstVal)^Long.parseLong(secondVal);
			break;
				
		case '/':
			subTotal=Long.parseLong(firstVal)/Long.parseLong(secondVal);
				break;
		case '*':
			subTotal=Long.parseLong(firstVal) * Long.parseLong(secondVal);
			break;
			
		case '+':
			subTotal=Long.parseLong(firstVal) + Long.parseLong(secondVal);
			break;
			
		case '-':
			subTotal=Long.parseLong(firstVal) - Long.parseLong(secondVal);
			break;
		
		}
		return subTotal;
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
				countClosingBrackets++;
			}
		}
		
		if(countOpeningBrackets==countClosingBrackets) {
			isValid=true;
		}
		return isValid;
	}
	
	private boolean validateOperators(String strExpr) {
		String strValue=new String (strExpr);
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
