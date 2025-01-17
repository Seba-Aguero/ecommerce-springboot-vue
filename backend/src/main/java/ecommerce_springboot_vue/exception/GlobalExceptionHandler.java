package ecommerce_springboot_vue.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorDetails> handleResourceNotFoundException(
    ResourceNotFoundException ex,
    WebRequest request)
  {
    ErrorDetails errorDetails = new ErrorDetails(
      HttpStatus.NOT_FOUND,
      ex.getMessage(),
      request.getDescription(false),
      null
    );
    return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorDetails> handleBadRequestException(
    BadRequestException ex,
    WebRequest request)
  {
    ErrorDetails errorDetails = new ErrorDetails(
      HttpStatus.BAD_REQUEST,
      ex.getMessage(),
      request.getDescription(false),
      null
    );
    return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDetails> handleValidationExceptions(
    MethodArgumentNotValidException ex,
    WebRequest request)
  {
    List<String> errors = ex.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(error -> error.getField() + ": " + error.getDefaultMessage())
      .toList();

    ErrorDetails errorDetails = new ErrorDetails(
      HttpStatus.BAD_REQUEST,
      "Validation failed",
      request.getDescription(false),
      errors
    );
    return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDetails> handleAllExceptions( // Manage all other exceptions
    Exception ex,
    WebRequest request)
  {
    ErrorDetails errorDetails = new ErrorDetails(
      HttpStatus.INTERNAL_SERVER_ERROR,
      ex.getMessage(),
      request.getDescription(false),
      null
    );
    return new ResponseEntity<>(errorDetails, errorDetails.getStatus());
  }
}