package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.StoreBulkUseCase.StoreBulkUseCaseCommand;
import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchWithGivenIdAlreadyCreatedException;
import com.silenteight.hsbc.bridge.bulk.rest.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/async/batch/v1")
@RequiredArgsConstructor
public class BulkRestController {

  private final AcknowledgeBulkDeliveryUseCase acknowledgeBulkDeliveryUseCase;
  private final CancelBulkUseCase cancelBulkUseCase;
  private final GetBulkStatusUseCase getBulkStatusUseCase;
  private final GetBulkResultsUseCase getBulkResultsUseCase;
  private final IngestRecommendationsUseCase ingestRecommendationsUseCase;
  private final StoreBulkUseCase storeBulkUseCase;

  private static final String[] OWS_HEADER =
      { "Alert-key", "Action", "Reference", "Ad Reason Code", "Alert Description" };
  private static final String OWS_FILE_NAME = "owsResponse.ows";

  @PostMapping("/{batchId}/recommend")
  public ResponseEntity<BatchAcceptedResponse> receiveBatch(
      @PathVariable String batchId, HttpServletRequest request) throws IOException {
    storeBulkUseCase.handle(
        StoreBulkUseCaseCommand.builder()
            .bulkId(batchId)
            .inputStream(request.getInputStream())
            .learning(false)
            .build());

    return ResponseEntity.ok(new BatchAcceptedResponse().batchId(batchId));
  }

  @PostMapping("/{batchId}/learning")
  public ResponseEntity<BatchAcceptedResponse> receiveLearningBatch(
      @PathVariable String batchId, HttpServletRequest request) throws IOException {
    storeBulkUseCase.handle(
        StoreBulkUseCaseCommand.builder()
            .bulkId(batchId)
            .inputStream(request.getInputStream())
            .learning(true)
            .build());

    return ResponseEntity.ok(new BatchAcceptedResponse().batchId(batchId));
  }

  @PostMapping("/ingestRecommendations")
  public ResponseEntity ingestRecommendations(
      @RequestBody @Valid BatchSolvedAlerts recommendations) {
    ingestRecommendationsUseCase.ingest(recommendations);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/ack")
  public ResponseEntity<BatchStatusResponse> acknowledgeDelivery(@PathVariable String id) {
    return ResponseEntity.ok(acknowledgeBulkDeliveryUseCase.apply(id));
  }

  @GetMapping("/{id}/result")
  public ResponseEntity<BatchSolvedAlerts> getResult(@PathVariable String id) {
    return ResponseEntity.ok(getBulkResultsUseCase.getResults(id));
  }

  @GetMapping("/processingStatus")
  public ResponseEntity<BatchProcessingStatusResponse> checkProcessingStatus() {
    var isProcessing = getBulkStatusUseCase.isProcessing();

    return ResponseEntity.ok(new BatchProcessingStatusResponse()
        .isAdjudicationEngineProcessing(isProcessing));
  }

  @GetMapping("/{id}/status")
  public ResponseEntity<BatchStatusResponse> getBulkStatus(@PathVariable String id) {
    return ResponseEntity.ok(getBulkStatusUseCase.getStatus(id));
  }

  @GetMapping("/{id}/cancel")
  public ResponseEntity<BatchCancelResponse> cancelBulk(@PathVariable String id) {
    return ResponseEntity.ok(cancelBulkUseCase.cancel(id));
  }

  @ExceptionHandler({ BatchIdNotFoundException.class, BatchProcessingNotCompletedException.class })
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponse> handleExceptionWithNotFoundStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({ BatchWithGivenIdAlreadyCreatedException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleExceptionWithBadRequestStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ IOException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleIOExceptionWithBadRequestStatus(
      IOException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ MethodArgumentNotValidException.class })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException exception) {

    var message = exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .findFirst()
        .map(x -> x.getField() + " " + x.getDefaultMessage())
        .orElse("");

    return getErrorResponse(message, HttpStatus.BAD_REQUEST);
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
