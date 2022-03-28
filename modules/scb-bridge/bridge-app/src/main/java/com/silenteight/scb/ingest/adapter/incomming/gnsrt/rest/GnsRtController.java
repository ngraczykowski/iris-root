package com.silenteight.scb.ingest.adapter.incomming.gnsrt.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.ErrorResponse;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response.GnsRtRecommendationResponse;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation.GnsRtRecommendationUseCase;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation.InvalidGnsRtRequestDataException;

import io.grpc.StatusRuntimeException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/v1/gnsrt/recommendation")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "silenteight.scb-bridge.gns-rt.enabled", havingValue = "true")
public class GnsRtController {

  private final GnsRtRecommendationUseCase useCase;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<GnsRtRecommendationResponse>> getRecommendation(
      @Valid @RequestBody GnsRtRecommendationRequest request) {
    return useCase
        .recommend(request)
        .map(GnsRtController::makeResponse);
  }

  private static ResponseEntity<GnsRtRecommendationResponse> makeResponse(
      GnsRtRecommendationResponse gnsRtRecommendationResponse) {

    return ResponseEntity.ok(gnsRtRecommendationResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public final ResponseEntity<ErrorResponse> gnsRtRecommendationRequestValidationException(
      MethodArgumentNotValidException exception) {

    List<String> errorMessages = exception
        .getBindingResult()
        .getAllErrors()
        .stream()
        .map(GnsRtController::getErrorMessage)
        .collect(Collectors.toList());

    return createBadRequestErrorResponse(String.join(";\n", errorMessages));
  }

  @Nonnull
  private static String getErrorMessage(ObjectError error) {
    if (error instanceof FieldError)
      return ((FieldError) error).getField() + " - " + error.getDefaultMessage();
    else
      return error.getObjectName() + " - " + error.getDefaultMessage();
  }

  private static ResponseEntity<ErrorResponse> createBadRequestErrorResponse(String message) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message(message)
            .status(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build(),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public final ResponseEntity<ErrorResponse> jsonParseError(
      HttpMessageNotReadableException exception) {

    return createBadRequestErrorResponse(exception.getMessage());
  }

  @ExceptionHandler(InvalidGnsRtRequestDataException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleInvalidGnsRtRequestDataException(
      InvalidGnsRtRequestDataException exception) {
    return createBadRequestErrorResponse(exception.getMessage());
  }

  @ExceptionHandler(StatusRuntimeException.class)
  @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
  public ResponseEntity<ErrorResponse> handleStatusRuntimeException(
      StatusRuntimeException exception) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .error(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase())
            .message(exception.getMessage())
            .status(HttpStatus.SERVICE_UNAVAILABLE.value())
            .timestamp(LocalDateTime.now())
            .build(),
        HttpStatus.SERVICE_UNAVAILABLE);
  }
}
