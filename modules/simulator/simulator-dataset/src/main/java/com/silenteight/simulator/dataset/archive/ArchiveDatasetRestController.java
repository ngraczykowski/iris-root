package com.silenteight.simulator.dataset.archive;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.*;
import static com.silenteight.simulator.dataset.domain.DomainConstants.DATASET_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
@Tag(name = DATASET_ENDPOINT_TAG)
class ArchiveDatasetRestController {

  @NonNull
  private final ArchiveDatasetUseCase useCase;

  @PostMapping("/v1/datasets/{id}:archive")
  @PreAuthorize("isAuthorized('ARCHIVE_DATASET')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public ResponseEntity<Void> archive(@PathVariable UUID id, Authentication authentication) {
    log.info("Archive dataset. datasetId={}.", id);

    ArchiveDatasetRequest request = ArchiveDatasetRequest.builder()
        .id(id)
        .archivedBy(authentication.getName())
        .build();
    useCase.activate(request);
    log.debug("Archive dataset request processed.");

    return noContent().build();
  }
}
