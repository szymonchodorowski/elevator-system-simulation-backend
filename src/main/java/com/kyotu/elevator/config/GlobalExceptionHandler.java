package com.kyotu.elevator.config;

import com.kyotu.elevator.exceptions.InvalidFloorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

	@ExceptionHandler(InvalidFloorException.class)
	ResponseEntity<String> handleInvalidFloor(InvalidFloorException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
}
