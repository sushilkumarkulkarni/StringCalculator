/**
 * 
 */
package com.my.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.my.model.CalculatedValue;
import com.my.model.InputValue;
import com.my.model.OutputValue;
import com.my.service.CalculatorService;
import com.my.util.CalculatorConstants;

/**
 * @author Sushil
 *
 */
@RestController
public class CalculatorController {
	@Autowired
	private CalculatorService calculatorService;
	
	@RequestMapping (path="/calculate")
	public String calculateTotals(@RequestBody String strValues) {
		Gson gson =new Gson();
		String strResults="";
		try {
			InputValue inputValue=gson.fromJson(strValues,InputValue.class);
			CalculatedValue calculatedValue =calculatorService.calculateTotals(inputValue);
			strResults=gson.toJson(calculatedValue);
		}catch(JsonParseException je) {
			CalculatedValue calculatedValue =new CalculatedValue();
			calculatedValue.setCalculatedTotalList(new ArrayList<OutputValue>());
			OutputValue outputValue =new OutputValue();
			outputValue.setMessage(CalculatorConstants.JSON_FORMAT_EXCEPTION);
			strResults=gson.toJson(calculatedValue);
		}
		return strResults;
		
	}

}
