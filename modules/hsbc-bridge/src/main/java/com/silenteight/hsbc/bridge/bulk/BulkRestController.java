package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.exception.BulkAlreadyCompletedException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.input.HsbcRecommendationRequest;
import com.silenteight.hsbc.bridge.bulk.rest.output.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequestMapping("/async/bulk/v1")
@RequiredArgsConstructor
public class BulkRestController {

  private final CreateBulkUseCase createBulkUseCase;
  private final GetBulkResultsUseCase getBulkResultsUseCase;
  private final GetBulkStatusUseCase getBulkStatusUseCase;
  private final CancelBulkUseCase cancelBulkUseCase;

  private static final String[] OWS_HEADER =
      { "Alert-key", "Action", "Reference", "Ad Reason Code", "Alert Description" };
  private static final String OWS_FILE_NAME = "owsResponse.ows";

  @PostMapping("/recommendAlerts")
  public ResponseEntity<BulkAcceptedResponse> recommendAlerts(
      @Valid @RequestBody HsbcRecommendationRequest request) {
    return ResponseEntity.ok(createBulkUseCase.createBulk(request));
  }

  @GetMapping("/{id}/result")
  public ResponseEntity<BulkSolvedAlertsResponse> getResult(@PathVariable String id) {
    return ResponseEntity.ok(getBulkResultsUseCase.getResults(id));
  }

  @GetMapping(value = "/{id}/result/owsFile", produces = "text/csv")
  public ResponseEntity<Resource> getResultFile(@PathVariable String id) {

    var result = getBulkResultsUseCase.getResults(id);

    var owsBody = result.getAlerts().stream()
        .map(this::createOwsRow)
        .collect(Collectors.toList());

    ByteArrayInputStream byteArrayOutputStream;

    try (
        var out = new ByteArrayOutputStream();
        var printer = new CSVPrinter(
            new PrintWriter(out),
            CSVFormat.DEFAULT.withHeader(OWS_HEADER))) {

      for (List<String> record : owsBody) {
        printer.printRecord(record);
      }

      printer.flush();

      byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    var fileInputStream = new InputStreamResource(byteArrayOutputStream);

    var headers = new HttpHeaders();
    headers.set(CONTENT_DISPOSITION, "attachment; filename=" + OWS_FILE_NAME);
    headers.set(CONTENT_TYPE, "text/csv");

    return new ResponseEntity<>(fileInputStream, headers, HttpStatus.OK);
  }

  private List<String> createOwsRow(com.silenteight.hsbc.bridge.bulk.rest.input.SolvedAlert a) {
    // TODO fill it with proper data
    return List.of(a.getId() + "", a.getRecommendation().name(), a.getComment(), "Code",
        "Description");
  }

  @GetMapping("/processingStatus")
  public ResponseEntity<BulkProcessingStatusResponse> checkProcessingStatus() {
    return ResponseEntity.ok(getBulkStatusUseCase.isProcessing());
  }

  @GetMapping("/{id}/status")
  public ResponseEntity<BulkStatusResponse> getBulkStatus(@PathVariable String id) {
    return ResponseEntity.ok(getBulkStatusUseCase.getStatus(id));
  }

  @GetMapping("/{id}/cancel")
  public ResponseEntity<BulkCancelResponse> cancelBulk(@PathVariable String id) {
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
