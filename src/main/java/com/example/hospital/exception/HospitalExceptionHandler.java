package com.example.hospital.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class HospitalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object>validation(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.toList();
		return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	private Map<String, List<String>> getErrorsMap(List<String> errors) {
		Map<String, List<String>> errorResponse = new HashMap<>();
		errorResponse.put("errors", errors);
		return errorResponse;
	}
                                    
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		String message = "HTTP method not supported: " + ex.getMethod();
		return new ResponseEntity<>(message, HttpStatus.METHOD_NOT_ALLOWED);
	}
		
	@SuppressWarnings("unchecked")  
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<JSONObject> handleNoProductPresent(IdNotFoundException ex){
		JSONObject response = new JSONObject();
		JSONArray details = new JSONArray();		
			response.put("code","Failed");
			response.put("message", "Id not found!!!"); 
			response.put("details", details);
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
	}
	
	@SuppressWarnings("unchecked")
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<JSONObject> handleValidationExceptions(HttpClientErrorException ex) {
		JSONObject response = new JSONObject();
		JSONArray details = new JSONArray();
		    response.put("code", "VALERRCOD");
			response.put("message", "Incorrect Password");   
			response.put("details", details);
			response.put("access_token","No token");
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);   
	}

	@SuppressWarnings("unchecked")
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<JSONObject> handleValidationExceptions(IllegalArgumentException ex) {
		JSONObject response = new JSONObject();
		JSONArray details = new JSONArray();
		    response.put("code", "NULLCODE");
			response.put("message", "Incorrect UserName");   
			response.put("details", details);
			response.put("access_token","No token");
			return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);   
	}	
}
