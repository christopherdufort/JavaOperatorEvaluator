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

import static org.junit.Assert.*;

import com.chrisdufort.evaluator.EvaluatorUtilities;
import com.chrisdufort.exceptions.InvalidExpressionFormatException;

@RunWith(Parameterized.class)
public class EvaluatorTest {
	
	@Parameters (name="{index} eval[{0}={1}]")
	public static Collection<Object[]> paramaterizedData(){
			
		return Arrays.asList(new Object[][]{
			//0
           {new ArrayDeque<String>(Arrays.asList("0")),
            	new ArrayDeque<String>(Arrays.asList("0")), new BigDecimal("0")}, 
            //1
           {new ArrayDeque<String>(Arrays.asList("197","+","3")),
            	new ArrayDeque<String>(Arrays.asList("197","3","+")), new BigDecimal("200")},
            //2
            {new ArrayDeque<String>(Arrays.asList("(","-5",")")),
            	new ArrayDeque<String>(Arrays.asList("-5")), new BigDecimal("-5")},
            //3
            {new ArrayDeque<String>(Arrays.asList("5","x","5")),
                new ArrayDeque<String>(Arrays.asList("5","5","x")), new BigDecimal("25")},
            //4
            {new ArrayDeque<String>(Arrays.asList("7","X","6")),
                new ArrayDeque<String>(Arrays.asList("7","6","X")), new BigDecimal("42")},
            //5
            {new ArrayDeque<String>(Arrays.asList("1000000000","/","2")),
            	new ArrayDeque<String>(Arrays.asList("1000000000","2","/")), new BigDecimal("500000000")},
            //6
            {new ArrayDeque<String>(Arrays.asList("4","*","7")),
            	new ArrayDeque<String>(Arrays.asList("4","7","*")), new BigDecimal("28")},
            //7
            {new ArrayDeque<String>(Arrays.asList("100000000","/","10")),
                new ArrayDeque<String>(Arrays.asList("100000000","10","/")), new BigDecimal("10000000")},
            //8
            {new ArrayDeque<String>(Arrays.asList("(","0","-","1",")","+","5")),
                new ArrayDeque<String>(Arrays.asList("0","1","-","5","+")), new BigDecimal("4")}, 
            //9
            {new ArrayDeque<String>(Arrays.asList("7","-","3","-","2")),
                new ArrayDeque<String>(Arrays.asList("7","3","-","2","-")), new BigDecimal("2")}, 
            //10
            {new ArrayDeque<String>(Arrays.asList("(","(","1","+","2",")","+","3",")")), 
                new ArrayDeque<String>(Arrays.asList("1","2","+","3","+")), new BigDecimal("6")},
            //11
            {new ArrayDeque<String>(Arrays.asList("3","*","(","2","+","5",")")), 
                new ArrayDeque<String>(Arrays.asList("3","2","5","+","*")), new BigDecimal("21")},
            //12
            {new ArrayDeque<String>(Arrays.asList("(","4","/","(","1","+","4",")","*","2",")")), 
                new ArrayDeque<String>(Arrays.asList("4","1","4","+","/","2","*")), new BigDecimal("1.6")},
            //13
            {new ArrayDeque<String>(Arrays.asList("(","(","2","*","5",")","+","3",")")), 
                new ArrayDeque<String>(Arrays.asList("2","5","*","3","+")), new BigDecimal("13")},
            //14
            {new ArrayDeque<String>(Arrays.asList("12","+","60","-","23")), 
                new ArrayDeque<String>(Arrays.asList("12","60","+","23","-")), new BigDecimal("49")},
            //15
            {new ArrayDeque<String>(Arrays.asList("3","+","4","*","5","/","6","+","1","/","3","+","1","/","3")),
                new ArrayDeque<String>(Arrays.asList("3","4","5","*","6","/","+","1","3","/","+","1","3","/","+")), new BigDecimal("7")},
            //16
            {new ArrayDeque<String>(Arrays.asList("0.5","*","0.4","*",".5","*",".7")), 
                new ArrayDeque<String>(Arrays.asList("0.5","0.4","*",".5","*",".7","*")), new BigDecimal("0.0700")},
            //17
            {new ArrayDeque<String>(Arrays.asList("1000","/","100","/","10","/","1")), 
                new ArrayDeque<String>(Arrays.asList("1000","100","/","10","/","1","/")), new BigDecimal("1")},
            //18
            {new ArrayDeque<String>(Arrays.asList("1","+","1","+","1","+","1","+","1","+","1")), 
                new ArrayDeque<String>(Arrays.asList("1","1","+","1","+","1","+","1","+","1","+")), new BigDecimal("6")},
            //19       
            {new ArrayDeque<String>(Arrays.asList("1","+","2","-","3","*","4","/","5")), 
                new ArrayDeque<String>(Arrays.asList("1","2","+","3","4","*","5","/","-")),new BigDecimal("0.6")},
            //20
            {new ArrayDeque<String>(Arrays.asList("(","1","+","2",")","+","(","3","*","4",")","/","(","5","/","4",")")),
                new ArrayDeque<String>(Arrays.asList("1","2","+","3","4","*","5","4","/","/","+")), new BigDecimal("12.6")},
            //21
            {new ArrayDeque<String>(Arrays.asList("9","-","(","4",")","+","(","8",")","+","(","6",")")),
                new ArrayDeque<String>(Arrays.asList("9","4","-","8","+","6","+")), new BigDecimal("19")},
            //22
            {new ArrayDeque<String>(Arrays.asList("(","-7",")","+","(","-3",")")),
                new ArrayDeque<String>(Arrays.asList("-7","-3","+")), new BigDecimal("-10")},
            //23
            {new ArrayDeque<String>(Arrays.asList("100","*","100","*","100","*","2","+","34765","*","100")),
                new ArrayDeque<String>(Arrays.asList("100","100","*","100","*","2","*","34765","100","*","+")), new BigDecimal("5476500")},
            //24
            {new ArrayDeque<String>(Arrays.asList("-7","*","(","-3","+","3",")")),
               	new ArrayDeque<String>(Arrays.asList("-7","-3","3","+","*")), new BigDecimal("0")},
            //25
            {new ArrayDeque<String>(Arrays.asList("4","*","3","+","2","-","6","/","(","1","+","1",")")),
            	new ArrayDeque<String>(Arrays.asList("4","3","*","2","+","6","1","1","+","/","-")), new BigDecimal("11")},
            //26
            {new ArrayDeque<String>(Arrays.asList("(","300","+","23",")","*","(","43","-","21",")","/","84","+","7")),
               	new ArrayDeque<String>(Arrays.asList("300","23","+","43","21","-","*","84","/","7","+")), new BigDecimal("91.59523809523810")},
            //27
            {new ArrayDeque<String>(Arrays.asList("(","(","(","(","1","*","(","2","+","3",")",")","-","3",")","+","4",")","*","5",")")), 
               new ArrayDeque<String>(Arrays.asList("1","2","3","+","*","3","-","4","+","5","*")),new BigDecimal("30")},
            //28
            {new ArrayDeque<String>(Arrays.asList("(","4","+","8",")","*","6","-","5","/","(","3","-","2",")","*","(","2","+","2",")")),
               new ArrayDeque<String>(Arrays.asList("4","8","+","6","*","5","3","2","-","/","2","2","+","*","-")), new BigDecimal("52")},
            //29
            {new ArrayDeque<String>(Arrays.asList("(","-0.4",")","/","(","-19.6",")","+","(","32984.400",")","*","(","3476095.600",")")),
               new ArrayDeque<String>(Arrays.asList("-0.4","-19.6","/","32984.400","3476095.600","*","+")), new BigDecimal("114656927708.6604")} , 
            //30
            {new ArrayDeque<String>(Arrays.asList("5.987111","+","6.9125343555")),
               new ArrayDeque<String>(Arrays.asList("5.987111","6.9125343555","+")), new BigDecimal("12.8996453555")}          
		});
	}
	
	private Queue<String> infix;
	private Queue<String> expectedPostfix;
	private Queue<String> postfixToEvaluate;
	private BigDecimal expectedResult;
	
	public EvaluatorTest(ArrayDeque<String> infix, ArrayDeque<String> postfix, BigDecimal expectedResult){
		this.infix = infix;
		this.expectedPostfix = postfix;
		this.postfixToEvaluate = new ArrayDeque<String>(postfix);
		this.expectedResult = expectedResult;
	}
	
	//Have to use toString to do an equals check because ArrayDeque has not overwritten the equals method :(
	@Test
	public void testInfixToPostfix() throws InvalidExpressionFormatException{	
		assertEquals("Infix to Postfix method has failed: ", expectedPostfix.toString(), EvaluatorUtilities.infixToPostfix(infix).toString());
		
	}
	
	//Have to use assertTrue and compareTo because bigdecimal's equals checks precision and value.
	@Test
	public void testEvaluatePostfix() throws InvalidExpressionFormatException{	
		assertTrue("Evaluate Postfix method has failed: ", expectedResult.compareTo(EvaluatorUtilities.evaluatePostfix(postfixToEvaluate)) ==0);
	}
}
