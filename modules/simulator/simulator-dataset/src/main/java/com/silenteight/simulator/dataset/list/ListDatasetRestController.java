package com.silenteight.simulator.dataset.list;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.dataset.domain.DatasetState;
import com.silenteight.simulator.dataset.dto.DatasetDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.silenteight.simulator.common.web.rest.RestConstants.ROOT;
import static com.silenteight.simulator.dataset.domain.DomainConstants.DATASET_ENDPOINT_TAG;
import static java.util.Optional.ofNullable;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = DATASET_ENDPOINT_TAG)
class ListDatasetRestController {

  @NonNull
  private final DatasetQuery datasetQuery;

  @GetMapping("/v1/datasets")
  @PreAuthorize("isAuthorized('LIST_DATASETS')")
  public ResponseEntity<List<DatasetDto>> list(
      @RequestParam(required = false) DatasetState... state) {

    return ok(datasetQuery.list(asSet(state)));
  }

  private static Set<DatasetState> asSet(DatasetState... state) {
    return ofNullable(state)
        .map(Set::of)
        .orElse(new HashSet<>());
  }
}
