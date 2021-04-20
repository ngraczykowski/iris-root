package com.silenteight.serp.governance.model.get;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class GetModelRestController {

  @NonNull
  private final GetModelDetailsQuery getModelDetailsQuery;

  @GetMapping("/v1/models/{modelId}")
  @PreAuthorize("isAuthorized('LIST_MODELS')")
  public ResponseEntity<ModelDto> get(@PathVariable UUID modelId) {
    return ok(getModelDetailsQuery.get(modelId));
  }
}
