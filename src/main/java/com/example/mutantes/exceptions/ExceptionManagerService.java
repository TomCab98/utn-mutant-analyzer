package com.example.mutantes.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionManagerService {
  private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = new HashMap<>();

  static {
    EXCEPTION_STATUS_MAP.put(MutanteNoEncontradoException.class, HttpStatus.NOT_FOUND);
    EXCEPTION_STATUS_MAP.put(ArgumentoNoValidoException.class, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ExceptionManager.class)
  @ResponseBody
  public ResponseEntity<Map<String, Object>> handlerNotFoundException(ExceptionManager ex, HttpServletRequest request) {
    return createExceptionResponse(ex, request.getRequestURI(), ex.getMessage());
  }

  private ResponseEntity<Map<String, Object>> createExceptionResponse(
      Exception exception, String path, Object message) {
    HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("exception", exception.getClass().getName());
    errorResponse.put("message", message);
    errorResponse.put("status", status.value());
    errorResponse.put("path", path);
    errorResponse.put("error", status);

    return ResponseEntity.status(status).body(errorResponse);
  }
}
