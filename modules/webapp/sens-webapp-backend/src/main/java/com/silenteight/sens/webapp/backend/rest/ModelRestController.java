package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.presentation.dto.model.ModelResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class ModelRestController {

  @GetMapping("/models")
  public ResponseEntity<ModelResponseDto> getModels() {
    ModelResponseDto response = new ModelResponseDto(0, emptyList());
    return ResponseEntity.ok(response);
  }
}
