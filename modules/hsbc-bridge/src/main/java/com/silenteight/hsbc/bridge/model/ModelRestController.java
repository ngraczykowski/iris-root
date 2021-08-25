package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;
import com.silenteight.hsbc.bridge.model.dto.ModelDetails;
import com.silenteight.hsbc.bridge.model.dto.SolvingModelDto;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelInfoStatusRequest;
import com.silenteight.hsbc.bridge.model.rest.input.ModelType;
import com.silenteight.hsbc.bridge.model.rest.output.SimpleModelResponse;
import com.silenteight.hsbc.bridge.model.transfer.ModelManager;

import liquibase.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import static com.silenteight.hsbc.bridge.model.rest.input.ModelType.fromValue;
import static java.util.stream.Collectors.toList;
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
  public ResponseEntity<String> export(HttpServletRequest request) {
    var modelDetails = getModelDetailsFromUri(request.getRequestURI());
    var modelManager = getMatchingModelManager(fromValue(modelDetails.getType()));
    var model = modelManager.exportModel(modelDetails);
    return ok(convertJsonModelInBytesToString(model));
  }

  @PostMapping
  public void modelUpdate(@RequestBody ModelInfoRequest request) {
    var modelManager = getMatchingModelManager(request.getType());
    modelManager.transferModelFromJenkins(request);
  }

  @PutMapping
  public void modelStatus(@RequestBody ModelInfoStatusRequest request) {
    var modelManager = getMatchingModelManager(request.getType());
    modelManager.transferModelStatus(request);
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

  private ModelManager getMatchingModelManager(ModelType modelType) {
    var matchingManagers = modelManagers.stream()
        .filter(manager -> manager.supportsModelType(mapToModelType(modelType)))
        .distinct()
        .collect(toList());

    if (matchingManagers.isEmpty()) {
      throw new ModelNotRecognizedException(modelType.name());
    }
    return matchingManagers.get(0);
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

  private String convertJsonModelInBytesToString(byte[] jsonModel) {
    return new String(jsonModel, StandardCharsets.UTF_8);
  }
}
