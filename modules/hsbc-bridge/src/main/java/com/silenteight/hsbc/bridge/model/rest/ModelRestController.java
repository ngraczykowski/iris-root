package com.silenteight.hsbc.bridge.model.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.model.ModelServiceClient;
import com.silenteight.hsbc.bridge.model.SolvingModelDto;
import com.silenteight.hsbc.bridge.model.rest.output.SimpleModelResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class ModelRestController {

  private final ModelServiceClient modelServiceClient;

  @GetMapping
  public ResponseEntity<SimpleModelResponse> getModel() {
    var solvingModel = modelServiceClient.getSolvingModel();
    return ok(createSimpleModelResponse(solvingModel));
  }

  private SimpleModelResponse createSimpleModelResponse(SolvingModelDto model) {
    var response = new SimpleModelResponse();
    response.setName(model.getName());
    response.setPolicyName(model.getPolicyName());
    return response;
  }
}
