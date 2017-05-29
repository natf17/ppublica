package com.ancientdawn.ppublica.errors;

import java.util.List;

public class ErrorResource {
    private String code;
    private String message;
    private List<FieldErrorResource> fieldErrors;
    private List<GlobalErrorResource> globalErrors;

    public ErrorResource() { }

    public ErrorResource(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }

    public List<FieldErrorResource> getFieldErrors() { return fieldErrors; }

    public void setFieldErrors(List<FieldErrorResource> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
    
    public List<GlobalErrorResource> getGlobalErrors() { return globalErrors; }

    public void setGlobalErrors(List<GlobalErrorResource> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
