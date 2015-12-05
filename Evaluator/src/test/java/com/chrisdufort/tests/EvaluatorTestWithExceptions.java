package com.chrisdufort.tests;


import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.chrisdufort.evaluator.EvaluatorUtilities;
import com.chrisdufort.exceptions.InvalidExpressionFormatException;

@RunWith(Parameterized.class)
public class EvaluatorTestWithExceptions {
	
	/*
	 * These tests should result in exceptions as they are improperly formated
	 */
	@Parameters (name="{index} exceptions[{0}={1}]")
	public static Collection<Object[]> paramaterizedData(){
			
		return Arrays.asList(new Object[][]{
			//0
           {new ArrayDeque<String>(Arrays.asList("(")),
            	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")}, 
			//1     
			{new ArrayDeque<String>(Arrays.asList("(",")")),
            	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")}, 
			//2
			{new ArrayDeque<String>(Arrays.asList("(","3","+","5")),
            	new ArrayDeque<String>(Arrays.asList("3","+","5")), new BigDecimal("8")},
            //3
            {new ArrayDeque<String>(Arrays.asList("10","/","0")),
            	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},
            //4
            {new ArrayDeque<String>(Arrays.asList("5","+","+","3")),
                new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},
            //5
            {new ArrayDeque<String>(Arrays.asList("1","+","2",")")),
                new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},
            //6
            {new ArrayDeque<String>(Arrays.asList("5","6")),
            	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},    
			//7
            {new ArrayDeque<String>(Arrays.asList("9","+")),
           	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},  
           //8
            {new ArrayDeque<String>(Arrays.asList(")","5","+","3")),
            	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},  
           //9
           {new ArrayDeque<String>(Arrays.asList("(","(","4",")")),
           	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")}, 
            //10
            {new ArrayDeque<String>(Arrays.asList("(","3","(","(",")",")","2",")")),
            	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")}, 
           //11
           {new ArrayDeque<String>(Arrays.asList("1","2","3","4","5","6","7")),
               	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")}, 
           
           /* -Needs more testing           
            * //12
            {new ArrayDeque<String>(Arrays.asList("2","2","+")),
                	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")},
            //13
            {new ArrayDeque<String>(Arrays.asList("10","+","12","9","14")),
                    	new ArrayDeque<String>(Arrays.asList("")), new BigDecimal("0")}, */
		});
	}
	
	private Queue<String> infix;
	private Queue<String> expectedPostfix;
	private Queue<String> postfixToEvaluate;
	private BigDecimal expectedResult;
	
	public EvaluatorTestWithExceptions(ArrayDeque<String> infix, ArrayDeque<String> postfix, BigDecimal expectedResult){
		this.infix = infix;
		this.expectedPostfix = postfix;
		this.postfixToEvaluate = new ArrayDeque<String>(postfix);
		this.expectedResult = expectedResult;
	}
	
	/*
	 * NOTICE: Some of these are expected to FAIL (This is intended)as the tests include both methods
	 */
	//Have to use toString to do an equals check because ArrayDeque has not overwritten the equals method :(
	@Test(expected=InvalidExpressionFormatException.class)
	public void testInfixToPostfix(){
		//This should throw an exception due to improper formatting and that is correct.
		try {
			EvaluatorUtilities.infixToPostfix(infix);
		} catch (InvalidExpressionFormatException e) {
			//Exception was expected this is good!
		}
	}
	
	/*
	 * NOTICE: Some of these are expected to contain ERRORS (This is intended)as the tests include both methods
	 */
	@Test(expected=InvalidExpressionFormatException.class)
	public void testEvaluatePostfix(){	
		//If it makes it this far it should still throw an exception due to improper formatting and that is correct
		try {
			EvaluatorUtilities.evaluatePostfix(postfixToEvaluate);
		} catch (InvalidExpressionFormatException e) {
			//Exception was expected this is good!
		}
	}
}
