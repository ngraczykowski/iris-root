package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.output.ExportModelResponse;
import com.silenteight.hsbc.bridge.model.rest.output.SimpleModelResponse;
import com.silenteight.hsbc.bridge.model.transfer.ModelManager;
import com.silenteight.hsbc.bridge.model.transfer.ModelType;

import liquibase.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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
    checkRequestUri(request.getRequestURI());
    var name = request.getRequestURI().split("export/")[1];
    var exportModelResponseDto = modelServiceClient.exportModel(name);
    return ok(createExportModelResponse(exportModelResponseDto));
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

  private ExportModelResponse createExportModelResponse(ExportModelResponseDto exportModel) {
    var response = new ExportModelResponse();
    response.setModelJson(new String(exportModel.getModelJson(), StandardCharsets.UTF_8));
    return response;
  }
}
