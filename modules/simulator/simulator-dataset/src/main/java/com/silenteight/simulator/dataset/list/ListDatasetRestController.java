package com.silenteight.simulator.dataset.list;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.dataset.domain.DatasetState;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class ListDatasetRestController {

  @NonNull
  private final DatasetQuery datasetQuery;

  @GetMapping("/v1/datasets")
  @PreAuthorize("isAuthorized('LIST_DATASETS')")
  public ResponseEntity<List<DatasetDto>> list(
      @RequestParam(required = false) DatasetState state) {

    return ok(datasetQuery.list(state));
  }
}
