package com.ancientdawn.ppublica.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ancientdawn.ppublica.errors.ErrorResource;
import com.ancientdawn.ppublica.errors.FieldErrorResource;
import com.ancientdawn.ppublica.errors.GlobalErrorResource;
import com.ancientdawn.ppublica.exception.InvalidRequestException;
import com.ancientdawn.ppublica.exception.ResourceNotFoundException;

@ControllerAdvice
public class PpublicaExceptionHandler extends ResponseEntityExceptionHandler {
	@Autowired
	MessageSource messageSource;
	
	@ExceptionHandler({InvalidRequestException.class})
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		InvalidRequestException ex = (InvalidRequestException)e;
		List<FieldErrorResource> fieldErrorResources = new ArrayList<FieldErrorResource>();
		List<GlobalErrorResource> globalErrorResources = new ArrayList<GlobalErrorResource>();
		
		List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();
	    for (FieldError fieldError : fieldErrors) {
	    	FieldErrorResource fieldErrorResource = new FieldErrorResource();
	        fieldErrorResource.setResource(fieldError.getObjectName());
	        fieldErrorResource.setField(fieldError.getField());
	        fieldErrorResource.setCode(messageSource.getMessage(fieldError, null));
	        fieldErrorResource.setMessage(fieldError.getDefaultMessage());
            fieldErrorResources.add(fieldErrorResource);
        }
	    
	    List<ObjectError> globalErrors = ex.getErrors().getGlobalErrors();
	    for (ObjectError globalError : globalErrors) {
	    	GlobalErrorResource globalErrorResource = new GlobalErrorResource();
	    	globalErrorResource.setResource(globalError.getObjectName());
	    	globalErrorResource.setObject(globalError.getObjectName());
	    	globalErrorResource.setCode(messageSource.getMessage(globalError, null));
	    	globalErrorResource.setMessage(globalError.getDefaultMessage());
            globalErrorResources.add(globalErrorResource);
        }
	    
	    
	    
	    
		
		ErrorResource error = new ErrorResource("InvalidRequest", ex.getMessage());
		
		error.setFieldErrors(fieldErrorResources);
		error.setGlobalErrors(globalErrorResources);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
		
	}
	
	@ExceptionHandler({ResourceNotFoundException.class})
	protected ResponseEntity<Object> handleResourceNotFound(RuntimeException e, WebRequest request) {
		ResourceNotFoundException ex = (ResourceNotFoundException)e;
		ErrorResource error = new ErrorResource("NotFound", ex.getErrorMessage());

		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

		
        return handleExceptionInternal(e, error, headers, HttpStatus.NOT_FOUND, request);
	}
}
