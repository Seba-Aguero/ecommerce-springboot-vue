package ecommerce_springboot_vue.exception;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails {

  private HttpStatus status;

  private String message;

  private String path;

  private List<String> errors;
}
