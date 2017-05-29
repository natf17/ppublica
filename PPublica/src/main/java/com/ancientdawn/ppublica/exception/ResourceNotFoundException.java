package com.ancientdawn.ppublica.exception;

public class ResourceNotFoundException extends RuntimeException {
	private String errorMessage;

	public ResourceNotFoundException(String message) {
		this.errorMessage = message;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
