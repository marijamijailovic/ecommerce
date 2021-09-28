package com.demo.ecommerce.exception;

import com.demo.ecommerce.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private HttpHeaders httpHeaders = new HttpHeaders();

  public GlobalExceptionHandler() {
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
  }

  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
    List<ObjectError> objectErrorList = methodArgumentNotValidException.getAllErrors();
    ApiErrorResponse apiError = ApiErrorResponse.builder().message(objectErrorList.size() > 0 ? objectErrorList.get(0).getDefaultMessage() : "Argument is not valid").build();
    log.error(apiError.getMessage());
    return ResponseEntity.badRequest().headers(httpHeaders).body(apiError);
  }

  @ExceptionHandler(value = RequestNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleRequestNotValidException(RequestNotValidException requestNotValidException) {
    ApiErrorResponse apiError = ApiErrorResponse.builder().message(requestNotValidException.getException()).build();
    log.error(apiError.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(apiError);
  }

  @ExceptionHandler(value = NoEntityFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<Object> handleNoEntityFoundException(NoEntityFoundException noEntityFoundException) {
    ApiErrorResponse apiError = ApiErrorResponse.builder().message(noEntityFoundException.getException()).build();
    log.error(apiError.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(httpHeaders).body(apiError);
  }

  @ExceptionHandler(value = DoubleEntityFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Object> handleDoubleEntityFoundException(DoubleEntityFoundException doubleEntityFoundException) {
    ApiErrorResponse apiError = ApiErrorResponse.builder().message(doubleEntityFoundException.getException()).build();
    log.error(apiError.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(httpHeaders).body(apiError);
  }

}
