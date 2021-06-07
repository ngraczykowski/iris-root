package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.output.SimpleModelResponse;
import com.silenteight.hsbc.bridge.model.transfer.ModelManager;
import com.silenteight.hsbc.bridge.model.transfer.ModelType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelRestController {

  private final List<ModelManager> modelManagers;
  private final ModelServiceClient modelServiceClient;

  @GetMapping
  public ResponseEntity<SimpleModelResponse> getModel() {
    var solvingModel = modelServiceClient.getSolvingModel();
    return ok(createSimpleModelResponse(solvingModel));
  }

  @PostMapping
  public void modelUpdate(@RequestBody ModelInfoRequest request) {
    getMatchingModelManagers(request.getType())
        .forEach(manager -> manager.transferModelFromJenkins(request));
  }

  @PutMapping
  public void modelStatus(@RequestBody ModelInfoStatusRequest request) {
    getMatchingModelManagers(request.getType())
        .forEach(manager -> manager.transferModelStatus(request));
  }

  @ExceptionHandler({ ModelNotRecognizedException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleExceptionWithBadRequestStatus(
      RuntimeException exception) {
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

  private Set<ModelManager> getMatchingModelManagers(
      com.silenteight.hsbc.bridge.model.rest.input.ModelType modelType) {
    var matchingManagers = modelManagers.stream()
        .filter(manager -> manager.supportsModelType(mapToModelType(modelType)))
        .collect(Collectors.toSet());

    if (matchingManagers.isEmpty()) {
      throw new ModelNotRecognizedException(modelType.name());
    }
    return matchingManagers;
  }

  private ModelType mapToModelType(
      com.silenteight.hsbc.bridge.model.rest.input.ModelType requestType) {
    return ModelType.valueOf(requestType.name());
  }

  private SimpleModelResponse createSimpleModelResponse(SolvingModelDto model) {
    var response = new SimpleModelResponse();
    response.setName(model.getName());
    response.setPolicyName(model.getPolicyName());
    return response;
  }
}
