package com.silenteight.simulator.dataset.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.dataset.create.dto.CreateDatasetRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class CreateDatasetRestController {

  @NonNull
  private final CreateDatasetUseCase useCase;

  @PostMapping("/v1/datasets")
  @PreAuthorize("isAuthorized('CREATE_DATASET')")
  public ResponseEntity<Void> create(
      @RequestBody @Valid CreateDatasetRequestDto dto, Authentication authentication) {

    CreateDatasetRequest request = CreateDatasetRequest.builder()
         .id(dto.getId())
         .datasetName(dto.getDatasetName())
         .description(dto.getDescription())
         .rangeFrom(dto.getQuery().getRangeFrom())
         .rangeTo(dto.getQuery().getRangeTo())
         .createdBy(authentication.getName())
         .build();
    useCase.activate(request);
    return status(CREATED).build();
  }
}
