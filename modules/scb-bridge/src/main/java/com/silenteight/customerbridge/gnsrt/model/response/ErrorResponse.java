package com.silenteight.customerbridge.gnsrt.model.response;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

/**
 * The error information and details.
 */
@Validated
@Data
@Builder
public class ErrorResponse {

  @JsonProperty("status")
  private int status;

  @JsonProperty("error")
  @NotNull
  private String error;

  @JsonProperty("timestamp")
  @NotNull
  private LocalDateTime timestamp;

  @JsonProperty("message")
  private String message;
}
