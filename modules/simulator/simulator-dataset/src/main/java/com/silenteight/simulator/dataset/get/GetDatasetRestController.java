package com.silenteight.simulator.dataset.get;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class GetDatasetRestController {

  @NonNull
  private final DatasetQuery datasetQuery;

  @GetMapping("/v1/datasets/{datasetId}")
  @PreAuthorize("isAuthorized('LIST_DATASETS')")
  public ResponseEntity<DatasetDto> get(@PathVariable UUID datasetId) {
    return ok(datasetQuery.get(datasetId));
  }
}
