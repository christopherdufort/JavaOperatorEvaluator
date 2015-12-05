package com.chrisdufort.evaluator;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Queue;

import com.chrisdufort.exceptions.InvalidExpressionFormatException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Evaluation Utility Class used to create Postfix expressions from provided Infix expressions.
 * Also provides an approxiamate solution accurate to 16 places of a postfix expression.
 * 
 * Infix ~ Postfix converters with evaluation used for example:
 * http://scriptasylum.com/tutorials/infix_postfix/infix_postfix.html
 * http://www.meta-calculator.com/learning-lab/how-to-build-scientific-calculator/infix-to-postifix-convertor.php
 * 
 * Program to interface (Queue) -> maintain efficiency (ArrayDeque). 
 * Interface Queue(Virtual Queue) -> Implementation ArrayDeque
 * Interface Deque(Virtual Stack) -> Implementation ArrayDeque
 * 
 * @author Christopher Dufort
 * @since JDK 1.8
 * @version 1.0.0-RELEASE , last modified 2015-12-05
 *
 */
public final class EvaluatorUtilities {

	/**
	 * Private constructor used to prevent instantiation.
	 */
	private EvaluatorUtilities(){
		/*This class is a utility class, therefore the constructor is private.
		Private Constructor will prevent the instantiation of this class directly.*/
	}

	/**
	 * EvalutatorUtilities method for transforming infix expressions into postfix expressions.
	 * Postfix expressions are returned without parenthesis in a format that can easily be evaluated.
	 * 
	 * Precondition: This method is expecting valid data:
	 * 
	 * Numbers within bigdecimal's range of values (won't overflow/accurate precision)
	 * Only Acceptable values = { 0-9, * or x/X , + , - , /,   Negative numbers(-5) , Decimals(5.0) , Decimals without 0 (.5) }
	 * 
	 *  Non Exception throwing methods:
	 *  --------ArrayDeque Implementation class:---------
	 *  Push	 |	Pop	 |	Peek	
	 *  
	 *  ------------Queue Interface:--------
	 *  Offer |	Poll	|	Peek
	 *  
	 *  -----------Deque Interface:---------------
	 *  getFirst | getLast | Peek first, Peek Last
	 * 
	 * @param infix
	 * 			A Queue of strings infix expression to be transformed into a postfix expression.
	 * @return	A postfix expression returned as a Queue of strings to be evaluated.
	 * 
	 * @throws InvalidExpressionFormatException 
	 */
	public static Queue<String> infixToPostfix(Queue<String> infix) throws InvalidExpressionFormatException {
		
		//Operands go here
		Queue<String> postfixQueue = new ArrayDeque<String>();		
		//Operators go here
		Deque<String> operatorStack = new ArrayDeque<String>();
		
		//Value to work with (1 index of the queue/stack)
		String value;
		Character ch;
		
		//Check length
		//>1 = a number (0.5 , .5 , -1 . -1.5 22, )
		//=1 = a symbol( get its precedence) or a single digit (0-9)
		
		//Loop while values remain in the queue.
		while (infix.peek() != null)
		{
			//Retrieve value from front of the queue
			value = infix.poll();
			if (value.length() > 1) //If the value is a number
			{
				postfixQueue.offer(value); //add it to the end of the queue
			}
			else //Value is one character long (0-9 OR an Operand)
			{
				//Turn the first index of the string into a character
				ch = value.charAt(0);
				if (Character.isDigit(ch)) //Value is a digit (0-9)
				{
					postfixQueue.offer(value); //add it to the end of the queue
				}
				else //Value is an operand.
				{
					//If operator stack is empty or value is a open parenthesis push the operator onto the stack
					if (operatorStack.peek() == null || precedenceOf(value)== -1)
					{
						operatorStack.push(value);
					}
					//Value is a closing parenthesis, trigger the loop to pop off the operator stack.
					else if (precedenceOf(value) == 2)
					{
						//loop through taking all contents of the parenthesis transfer from stack to queue
						while(operatorStack.peek()!= "(" )
						{
							try{
								postfixQueue.offer(operatorStack.pop());
							}catch(NoSuchElementException ex){
								throw new InvalidExpressionFormatException("Missing matching parenthesis", ex.getCause());
							}
						}
						//pop off the opening parenthesis and do not add to queue.
						operatorStack.pop();
					}
					else if(precedenceOf(value) > precedenceOf(operatorStack.peek()) ) //value is > top of stack
					{
						operatorStack.push(value);
					}
					else //Swap the value with the top of the stack
					{
						//Loop checking top of stack and pop them if they are less than or equal to the value
						do
						postfixQueue.offer(operatorStack.pop());
						while(operatorStack.peek()!= null && (precedenceOf(value) <= precedenceOf(operatorStack.peek())));
						//add the new value to the stack
						operatorStack.push(value);		
					}
				}	
			}
		}
		//Infix queue is now empty fill the postfixQueue with the remainder of the stack
		while(operatorStack.peek() != null)
		{
			try{
				//Check if the postfixQueue is only of length one(don't check for operators.)
				if (postfixQueue.size() > 1 ){
					postfixQueue.offer(operatorStack.pop());
				}	
			}catch (NoSuchElementException ex){
				throw new InvalidExpressionFormatException("Missing operator.", ex.getCause());
			}			
		}
		return postfixQueue;	
	}
	
	/**
	 * EvaluatorUtilities method used to evaluate a postfix expression.
	 * This utility method will accept a postfix expression in the form of a queue and will
	 * return a BigDecimal accurate (16 place) representation of the expression.
	 * 
	 * Precondition: This method is expecting valid data:
	 * 
	 * No parethesis or non numeric / operand values.
	 * Postfix expressions prepared by infixToPostfix methods are prefered.
	 * @see com.chrisdufort.evaluator.EvaluatorUtilities.infixToPostfix
	 * 
	 * 	-----------Deque Interface:---------------
	 *  getFirst | getLast | Peek first, Peek Last
	 *  
	 * @param postfix
	 * 			The Queue of Strings, postfix expression to be evaluated.
	 * @return	A BigDecimal representation of the solution accurate to 16 places.
	 * 
	 * @throws InvalidExpressionFormatException 
	 */
	public static BigDecimal evaluatePostfix(Queue<String> postfix) throws InvalidExpressionFormatException {
		//Operands go here
		Deque<String> operandStack = new ArrayDeque<String>();
		//Expressions for math go here
		Deque<String> expressionDeque = new ArrayDeque<String>();
		
		//Variables used for math.
		BigDecimal operand1;
		BigDecimal operand2;
		
		//Result of mathematical expression inside of expressionDeque
		BigDecimal result;
		
		//Value to work with (1 index of the queue/stack)
		String value;
		
		while (postfix.peek() != null)
		{
			//Retrieve value from front of the queue
			value = postfix.poll();
			if (value.length() > 1) //If the value is a number
			{
				operandStack.push(value);
			}
			else //Value is one character long (0-9 OR an Operand)
			{
				//Turn the first index of the string into a character
				Character ch = value.charAt(0);
				if (Character.isDigit(ch)) //Value is a digit (0-9)
				{
					operandStack.push(value);
				}
				else //Value is an operator (guaranteed 2 operands on stack at this point)
				{
					try{
						//Pop operand add to end
						expressionDeque.addLast(operandStack.pop());
					}catch (NoSuchElementException ex){
						throw new InvalidExpressionFormatException("Missing matching parenthesis", ex.getCause());
					}
					
					//add value(operator) to front
					expressionDeque.addFirst(value);			
					try{
						//Pop second operand add to front
						expressionDeque.addFirst(operandStack.pop());
					}catch(NoSuchElementException ex){
						throw new InvalidExpressionFormatException("Missing matching parenthesis OR too many operators", ex.getCause());
					}
					
					operand1 = new BigDecimal(expressionDeque.removeFirst());
					String operator = expressionDeque.removeFirst();
					operand2 = new BigDecimal(expressionDeque.removeFirst());
					
					//value is an operator (* , / , + , -)
					try{
						result = doMath(operand1, operator, operand2);
					}catch(ArithmeticException ex){
						throw new InvalidExpressionFormatException(ex.getMessage(), ex.getCause());
					}
					
					//return result to the stack.
					operandStack.push(String.valueOf(result));
				}	
			}
		}	
		//When all values in postfix queue are exhausted return contents of operandStack
		try{
			if (operandStack.size() > 1)
				throw new InvalidExpressionFormatException("Too much operands");
			return new BigDecimal(operandStack.pop(),MathContext.DECIMAL64);
		}catch (NoSuchElementException ex){
			throw new InvalidExpressionFormatException("Missing operand", ex.getCause());
		}
	}
	
	/**
	 * Private mathematics method used by the evaluator utility methods to perform operations on operands.
	 * Passed in through parameters are all three necessary ingredients to perform an operation.
	 * Operators are passed in as strings, and BigDecimal operators associated are performed.
	 * 
	 * @param operand1
	 * 			First BigDecimal Operand used in the operation.
	 * @param operator
	 * 			The String Operator used in the operation.
	 * @param operand2
	 * 			Second BigDecimal Operand used in the operation.
	 * @return	A BigDecimal representation with accurate precision of the result of operation.
	 */
	private static BigDecimal doMath(BigDecimal operand1, String operator, BigDecimal operand2) {
		BigDecimal result = new BigDecimal(0.0);
		switch (operator){
		case "-":
			result = operand1.subtract(operand2);
			break;
		case "+":
			result = operand1.add(operand2);
			break;
		case "*":
		case "x":
		case "X":
			result = operand1.multiply(operand2);
			break;
		case "/":
			result = operand1.divide(operand2,16, RoundingMode.HALF_UP); //In order to prevent non terminating decimals.
			break;
		}
		return result;
	}


	/**
	 * Private precedence finding method used by the evaluator utility methods.
	 * This method receives a string representation of an operand and returns its associated precedence. 
	 * Operator precedence is based on P>E>M>D>A>S & with changed made for use within this class.
	 * 
	 * @param operand
	 * 			String representation of the operand for which to check precedence.
	 * @return	Integer representation of the operator's precedence.
	 * 
	 */
	private static int precedenceOf(String operator) {
	    switch (operator) 
	    {
	        case "+":
	        case "-":
	        	return 0;
	        case "*":
	        case "x":
	        case "X":
	        case "/":
	        	return 1;
	        case "(":
	        	return -1;
	        case ")":
	        	return 2;
	        default:
	            throw new IllegalArgumentException("Operator unknown: " + operator);
	    }   
	}
}
