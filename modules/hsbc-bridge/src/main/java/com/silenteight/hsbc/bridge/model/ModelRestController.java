package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;
import com.silenteight.hsbc.bridge.model.dto.ModelDetails;
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelType;
import com.silenteight.hsbc.bridge.model.rest.output.ExportModelResponse;
import com.silenteight.hsbc.bridge.model.rest.output.SimpleModelResponse;
import com.silenteight.hsbc.bridge.model.transfer.ModelManager;

import liquibase.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

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

  @GetMapping("/export/**")
  public ResponseEntity<ExportModelResponse> export(HttpServletRequest request) {
    var modelDetails = getModelDetailsFromUri(request.getRequestURI());
    var managers = getMatchingModelManagers(ModelType.fromValue(modelDetails.getType()));
    var modelManager = managers.stream().findFirst().get();
    return ok(modelManager.exportModel(modelDetails));
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

  @ExceptionHandler({ ModelNotRecognizedException.class, RequestNotValidException.class })
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

  private Set<ModelManager> getMatchingModelManagers(ModelType modelType) {
    var matchingManagers = modelManagers.stream()
        .filter(manager -> manager.supportsModelType(mapToModelType(modelType)))
        .collect(Collectors.toSet());

    if (matchingManagers.isEmpty()) {
      throw new ModelNotRecognizedException(modelType.name());
    }
    return matchingManagers;
  }

  private com.silenteight.hsbc.bridge.model.dto.ModelType mapToModelType(ModelType type) {
    return com.silenteight.hsbc.bridge.model.dto.ModelType.valueOf(type.name());
  }

  private ModelDetails getModelDetailsFromUri(String requestUri) {
    checkRequestUri(requestUri);
    var typeWithVersion = requestUri.split("export/")[1];
    var splitTypeAndVersion = typeWithVersion.split("/", 2);
    return ModelDetails.builder()
        .type(splitTypeAndVersion[0])
        .version(splitTypeAndVersion[1])
        .build();
  }

  private void checkRequestUri(String name) {
    if (StringUtil.isEmpty(name)) {
      throw new RequestNotValidException(name);
    }
  }

  private SimpleModelResponse createSimpleModelResponse(SolvingModelDto model) {
    var response = new SimpleModelResponse();
    response.setName(model.getName());
    response.setPolicyName(model.getPolicyName());
    return response;
  }
}
