package com.ancientdawn.ppublica.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
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
	
	@ExceptionHandler(InvalidRequestException.class)
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
		System.out.println("CUSTOM INVOKED" + e);
		InvalidRequestException ex = (InvalidRequestException)e;

		List<FieldErrorResource> fieldErrorResources = new ArrayList<FieldErrorResource>();
		List<GlobalErrorResource> globalErrorResources = new ArrayList<GlobalErrorResource>();

		List<FieldError> fieldErrors = ex.getErrors().getFieldErrors();
		//System.out.println(fieldErrors.size());

	    for (FieldError fieldError : fieldErrors) {
			//System.out.println("CREATING ERRORRESPURCE1");

	    	FieldErrorResource fieldErrorResource = new FieldErrorResource();
			//System.out.println("CREATING ERRORRESPURCE2");

	        fieldErrorResource.setResource(fieldError.getObjectName());
			//System.out.println("CREATING ERRORRESPURCE3");

	        fieldErrorResource.setField(fieldError.getField());
			//System.out.println("CREATING ERRORRESPURCE4");
			//System.out.println(fieldError);
			//System.out.println(messageSource.getMessage(fieldError, null));
	        fieldErrorResource.setCode(messageSource.getMessage(fieldError, null));
			//System.out.println("CREATING ERRORRESPURCE5");

	        fieldErrorResource.setMessage(fieldError.getDefaultMessage());
			//System.out.println("CREATING ERRORRESPURCE6");

            fieldErrorResources.add(fieldErrorResource);
    		//System.out.println("CREATING ERRORRESPURCE7");

        }
		//System.out.println("CREATING ERRORRESPURCE2");

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
	
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		System.out.println("BAD REQUEST");
		ErrorResource error = new ErrorResource("Bad Request", ex.getMessage());

        headers.setContentType(MediaType.APPLICATION_JSON);

		
        return handleExceptionInternal(ex, error, headers, HttpStatus.BAD_REQUEST, request);
	}

}
