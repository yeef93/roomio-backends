package com.finpro.roomio_backends.responses;

import com.finpro.roomio_backends.properties.entity.dto.PropertiesResponseDto;
import lombok.Data;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class Response<T> {

  private int statusCode;
  private boolean success = false;
  private String statusMessage;
  private T data;

  // Constructor
  public Response(int statusCode, String statusMessage) {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
    if (statusCode == HttpStatus.OK.value()) {
      success = true;
    }
  }

  // Base failedResponse method
  public static <T> ResponseEntity<Response<T>> failedResponse(int statusCode, String statusMessage, T data) {
    Response<T> response = new Response<>(statusCode, statusMessage);
    response.setSuccess(false);
    response.setData(data);
    return ResponseEntity.status(statusCode).body(response);
  }

  // Overloaded failedResponse methods
  public static <T> ResponseEntity<Response<T>> failedResponse(int statusCode, String statusMessage) {
    return failedResponse(statusCode, statusMessage, null);
  }

  public static <T> ResponseEntity<Response<T>> failedResponse(String statusMessage) {
    return failedResponse(HttpStatus.BAD_REQUEST.value(), statusMessage, null);
  }

  // Base successfulResponse method
  public static <T> ResponseEntity<Response<T>> successfulResponse(int statusCode, String statusMessage, T data) {
    Response<T> response = new Response<>(statusCode, statusMessage);
    response.setSuccess(true);
    response.setData(data);
    return ResponseEntity.status(statusCode).body(response);
  }

  // Overloaded successfulResponse methods
  public static <T> ResponseEntity<Response<T>> successfulResponse(String statusMessage, T data) {
    return successfulResponse(HttpStatus.OK.value(), statusMessage, data);
  }

  public static <T> ResponseEntity<Response<T>> successfulResponse(T data) {
    return successfulResponse(HttpStatus.OK.value(), "Process has executed successfully", data);
  }

  // Method for handling paginated responses
  public static ResponseEntity<?> successfulResponse(int statusCode, String message, List<PropertiesResponseDto> data, int totalPages, long totalElements) {
    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("status", statusCode);
    responseBody.put("message", message);
    responseBody.put("data", data);
    responseBody.put("totalPages", totalPages);
    responseBody.put("totalElements", totalElements);
    return ResponseEntity.status(statusCode).body(responseBody);
  }
}