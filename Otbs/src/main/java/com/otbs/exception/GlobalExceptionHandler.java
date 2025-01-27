package com.otbs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEntityException(InvalidEntityException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleGenericException(MethodArgumentNotValidException ex) { //MethodArgumentNotValidException
        Map<String, String> errors = new HashMap<>();
        BindingResult result = ((BindException) ex).getBindingResult();      // we are typecast becasue we aare taking Exception as argument 
        List<FieldError> fieldErrors = result.getFieldErrors();
        for(FieldError error : fieldErrors) {
        	errors.put(error.getField(), error.getDefaultMessage());
        }
//        error.put("message", "An unexpected error occurred: " + ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
    
    
    
    
    
    
    
    
    
    
    
    
//    old code...
	
//	@ExceptionHandler(InvalidEntityException.class)
//	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
//	    Map<String, String> errors = new HashMap<>();
//	    ex.getBindingResult().getFieldErrors().forEach(error -> 
//	        errors.put(error.getField(), error.getDefaultMessage())
//	    );
//	    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
//	    Map<String, String> error = new HashMap<>();
//	    error.put("message", "An unexpected error occurred.");
//	    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

	
    
}
