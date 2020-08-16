package com.kanban.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public final ResponseEntity<Object> handleProjectIdException(ProjectIdException ex, WebRequest req){
		ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public final ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest req){
		ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public final ResponseEntity<Object> handleUsernameException(UsernameException ex, WebRequest req){
		UsernameExceptionResponse exceptionResponse = new UsernameExceptionResponse(ex.getMessage());
		return  new ResponseEntity(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
}
