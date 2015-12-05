package com.chrisdufort.exceptions;

/**
 * Custom Exception type used to indicate that the format of an expression is invalid.
 * This Exception is thrown to indicate that the requirement for a proper Infix or Postfix expression was not met.
 * This Exception is used primarily by the EvaluatorUtilities Class.
 * 
 * @author Christopher Dufort
 * @since JDK 1.8
 * @see com.chrisdufort.evaluator.EvaluatorUtilities
 * @version 1.0.0-RELEASE , last modified 2015-12-05
 */
public class InvalidExpressionFormatException extends Exception {

	/**
	 * Not implemented to completion (simple example)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public InvalidExpressionFormatException() {
		super();
	}

	/**
	 * @param message
	 */
	public InvalidExpressionFormatException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public InvalidExpressionFormatException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidExpressionFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InvalidExpressionFormatException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
