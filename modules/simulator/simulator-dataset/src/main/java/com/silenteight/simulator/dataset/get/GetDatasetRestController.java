package com.silenteight.simulator.dataset.get;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.*;
import static com.silenteight.simulator.dataset.domain.DomainConstants.DATASET_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = DATASET_ENDPOINT_TAG)
class GetDatasetRestController {

  @NonNull
  private final DatasetQuery datasetQuery;

  @GetMapping("/v1/datasets/{datasetId}")
  @PreAuthorize("isAuthorized('LIST_DATASETS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION,
          content = @Content())
  })
  public ResponseEntity<DatasetDto> get(@PathVariable UUID datasetId) {
    log.info("Getting dataset details for datasetId={}", datasetId);
    return ok(datasetQuery.get(datasetId));
  }
}
