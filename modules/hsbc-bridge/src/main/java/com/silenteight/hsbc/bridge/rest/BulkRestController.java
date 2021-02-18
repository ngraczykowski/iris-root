package com.silenteight.hsbc.bridge.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.*;
import com.silenteight.hsbc.bridge.bulk.exception.BulkAlreadyCompletedException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.rest.model.input.Alerts;
import com.silenteight.hsbc.bridge.rest.model.output.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/async/bulk/v1")
@RequiredArgsConstructor
public class BulkRestController {

  private final CreateBulkUseCase createBulkUseCase;
  private final GetBulkResultsUseCase getBulkResultsUseCase;
  private final GetBulkStatusUseCase getBulkStatusUseCase;
  private final CancelBulkUseCase cancelBulkUseCase;

  @PostMapping("/recommendAlerts")
  public ResponseEntity<BulkAcceptedResponse> recommendAlerts(@RequestBody Alerts alerts) {
    return ResponseEntity.ok(createBulkUseCase.recommend(alerts));
  }

  @GetMapping("/{id}/result")
  public ResponseEntity<BulkSolvedAlertsResponse> getResult(@PathVariable UUID id) {
    return ResponseEntity.ok(getBulkResultsUseCase.getResults(id));
  }

  @GetMapping(value = "/{id}/result/file", produces = "application/x-octet-stream")
  public @ResponseBody byte[] getResultFile(@PathVariable UUID id) throws JsonProcessingException {
    var result = getBulkResultsUseCase.getResults(id);

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsBytes(result);
  }

  @GetMapping("/{id}/status")
  public ResponseEntity<BulkStatusResponse> getBulkStatus(@PathVariable UUID id) {
    return ResponseEntity.ok(getBulkStatusUseCase.getStatus(id));
  }

  @GetMapping("/{id}/cancel")
  public ResponseEntity<BulkCancelResponse> cancelBulk(@PathVariable UUID id) {
    return ResponseEntity.ok(cancelBulkUseCase.cancel(id));
  }

  @ExceptionHandler(BulkIdNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleBulkIdNotFoundException(
      BulkIdNotFoundException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BulkProcessingNotCompletedException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleBulkProcessingNotCompletedException(
      BulkProcessingNotCompletedException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(BulkAlreadyCompletedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleBulkAlreadyCompletedException(
      BulkAlreadyCompletedException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorResponse> getErrorResponse(String message, HttpStatus httpStatus) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .error(httpStatus.getReasonPhrase())
            .message(message)
            .status(httpStatus.value())
            .timestamp(LocalDateTime.now())
            .build(),
        httpStatus);
  }
}
