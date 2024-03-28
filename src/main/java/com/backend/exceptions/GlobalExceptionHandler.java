package com.backend.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorObject> handleProductNotFoundException(ProductNotFoundException ex, WebRequest request){
		ErrorObject errorObject = new ErrorObject();
		
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());
		
		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
		System.out.println("User not found but this is not running");
		ErrorObject errorObject = new ErrorObject();
				
		errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());
		
		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorObject> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request){
		ErrorObject errorObject = new ErrorObject();
				
		errorObject.setStatusCode(HttpStatus.CONFLICT.value());
		errorObject.setMessage(ex.getMessage());
		errorObject.setTimestamp(new Date());
		
		return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.CONFLICT);
	}

	

}
