/* Copyright (C) SafePlanet Innovations, LLP - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by SafePlanet Innovations <spinnovations.india@gmail.com>, October 2015
 */
package com.spi.exception;

import com.spi.api.ErrorResponse;
import com.spi.api.ValidationError;

import javax.validation.ConstraintViolation;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 
 */
public class ValidationException extends WebApplicationException {

	private static final long serialVersionUID = 8472573814841809461L;
	private final int status = 400;
	private String errorMessage;
	private String developerMessage;
	private List<ValidationError> errors = new ArrayList<ValidationError>();

	public ValidationException() {
		errorMessage = "Validation Error";
		developerMessage = "The data passed in the request was invalid. Please check and resubmit";
	}

	public ValidationException(String message) {
		//super();
		errorMessage = message;
                ValidationError error = new ValidationError();
                error.setMessage(message);
	}
        
        public ValidationException(String message, String propertyName) {
		super();
		errorMessage = message;
                ValidationError error = new ValidationError();
                error.setMessage(message);
                error.setPropertyName(propertyName);
                errors.add(error);
	}

	public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
		this();
		for (ConstraintViolation<?> constraintViolation : violations) {
			ValidationError error = new ValidationError();
			error.setMessage(constraintViolation.getMessage());
			error.setPropertyName(constraintViolation.getPropertyPath().toString());
			error.setPropertyValue(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null);
			errors.add(error);
		}
	}

	@Override
	public Response getResponse() {
		return Response.status(status).type(MediaType.APPLICATION_JSON_TYPE)
				.entity(getErrorResponse()).build();
	}

	public ErrorResponse getErrorResponse() {
		ErrorResponse response = new ErrorResponse();
		response.setApplicationMessage(developerMessage);
		response.setConsumerMessage(errorMessage);
		response.setValidationErrors(errors);
		return response;
	}

}
