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
	  int expressionCount=3;
	  String expressionValues[]= {"7+(6*5^2+3-4/2)","7+(67(56*2))","(8*5/8)-(3/1)-5"};//"7+(6*5^2+3-4/2)"};//158//{"2+3*4/2"};//8,
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
		
//		if(exprss.indexOf('(')>-1){
//			while(exprss.indexOf('(')>-1){
//				int startIndx=exprss.lastIndexOf('(');
//				int endIndx=exprss.lastIndexOf(')');
//				
//				String actualstrExpr=exprss.substring(startIndx+1,endIndx);
//				System.out.println("CalculatorService.applyRules()"+actualstrExpr);
//				//calculatedTotal=totalSum(actualstrExpr);
//				//calculatedTotal=getTotalSum(actualstrExpr);
//				String strSub1=exprss.substring(0,startIndx);
//				String strSub2=exprss.substring(endIndx+1,exprss.length());
//				System.out.println("CalculatorService.applyRules() sbstr1"+strSub1);
//				System.out.println("CalculatorService.applyRules() sbstr2"+strSub2);
//				
//				exprss=strSub1+strSub2;
//				
//			}
//		}
		//else{*/
			
			//calculatedTotal=totalSum(exprss);
		exprss=exprss.replace("(", "");
		exprss=exprss.replace(")", "");
			calculatedTotal=getTotalSum(exprss);
		//}
		
		return calculatedTotal;
		
	}
	
	private int getPriorityPosition(char chr){
		int precendence=0;
			switch(chr) {
			case '*':
				precendence=5;
				break;
			case '/':
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
		
		calculatedTotal=getTotal(actualstrExpr, positionList);
		
		//last calculations
		/*if(calculatedTotal>0){
			firstVal=Long.toString(calculatedTotal);
		}*/
		return calculatedTotal;//getSubTotal(foundOperand, firstVal, secondVal);
	}
	
	private long getTotal(String actualstrExpr,List <SubExpression> positionList) {
		String firstVal="";
		String secondVal="";
		boolean first=false;
		String tmpVal="";
		long calculatedTotal=0;
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
		if(subTotalList.size()>1) {
			getTotal(actualstrExpr, subTotalList);
		}else {
			SubExpression subExpre =subTotalList.get(0);
			calculatedTotal=subExpre.getTotal();
		}
		return calculatedTotal;
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
	
	private long getTotalSum(String actualstrExpr){
		long calculatedTotal=0;
		
		String firstVal="";
		String secondVal="";
		boolean isFirstFound=false;
		char foundOperand=1;
		boolean isOperandFound=false;
		
		boolean isProcess=true;
		while(isProcess) {
			int precedence=-1;
			int indx=0;
			char [] actualExpr=actualstrExpr.toCharArray();
			char selectedChar=1;
			for(int i=0;i<actualExpr.length;i++) {
				char chr=actualExpr[i];
				if(!isValidNumber(""+chr)) {
					if(precedence<getPriorityPosition(chr)) {
						precedence=getPriorityPosition(chr);
						indx=i;
						selectedChar=chr;
						System.out.println("CalculatorService.getTotalSum() precedence "+precedence);
						System.out.println("CalculatorService.getTotalSum() index "+indx);
					}
				}
			}
			
			String prevVal=actualstrExpr.substring(0,indx);
			/*char preChr[]=prevVal.toCharArray();
			
			for(int i=0;i<preChr.length;i++) {
				char chr=preChr[i];
				if(!isValidNumber(""+chr)) {
					if(precedence<getPriorityPosition(chr)) {
						precedence=getPriorityPosition(chr);
						indx=i;
						selectedChar=chr;
						System.out.println("CalculatorService.getTotalSum() precedence "+precedence);
						System.out.println("CalculatorService.getTotalSum() index "+indx);
					}
				}
			}*/
			
			String nxtVal=actualstrExpr.substring(indx+1, actualstrExpr.length());
			char prevChr[]=prevVal.toCharArray();
			firstVal="";
			secondVal="";
			//to remove previous value
			for(int i=prevChr.length-1;i>=0;i--) {
				char chr=prevChr[i];
				if(isValidNumber(""+chr)) {
					firstVal=chr+firstVal;
				}else {
					break;
				}
			}
			
			/*for(int i=0;i<prevChr.length;i++) {
				char chr=prevChr[i];
				if(isValidNumber(""+chr)) {
					firstVal=firstVal+chr;
				}else {
					break;
				}
			}*/
			/*if(isCondition) {
				actualstrExpr=actualstrExpr.substring(prevIndx,actualstrExpr.length());
			}else {
				actualstrExpr=actualstrExpr.substring(0,prevIndx+1);
			}*/
			
			char nxtChr[]=nxtVal.toCharArray();
			for(int i=0;i<nxtChr.length;i++) {
				char chr=nxtChr[i];
				if(isValidNumber(""+chr)) {
					secondVal=secondVal+chr;
				}else {
					break;
				}
			}
			//actualstrExpr=actualstrExpr+actualstrExpr.substring(secIndx,nxtValStr.length());
			
			long subtotal=getSubTotal(selectedChar, firstVal, secondVal);
			String replacestr=firstVal+selectedChar+secondVal;
			actualstrExpr=actualstrExpr.replace(replacestr, ""+subtotal);
			if(isValidNumber(actualstrExpr)) {
				isProcess=false;
			}
			//actualstrExpr=actualstrExpr+subtotal+nxtValStr.substring(secIndx+1,nxtValStr.length());;			
		}
		
		/*for(char chr:actualExpr) {
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
		}*/
		//return getSubTotal(foundOperand, firstVal, secondVal);
		return Long.valueOf(actualstrExpr);
	}
	
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
		boolean isBracketValid=true;
		for(int i=0;i<charExpr.length;i++) {
			char chr=charExpr[i];
			if(chr=='(') {
				countOpeningBrackets++;
				if(i>charExpr.length-1 && charExpr[i+1]==chr) {
					isBracketValid=false;
					break;
				}
			}else if(chr==')') {
				countClosingBrackets++;
				if(i>charExpr.length-1 && charExpr[i+1]==chr) {
					isBracketValid=false;
					break;
				}
			}
		}
		
		if(countOpeningBrackets==countClosingBrackets && isBracketValid) {
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
