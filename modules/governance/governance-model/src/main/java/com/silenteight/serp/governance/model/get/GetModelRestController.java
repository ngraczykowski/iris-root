package com.silenteight.serp.governance.model.get;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.model.domain.dto.ModelDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class GetModelRestController {

  @NonNull
  private final ModelDetailsQuery getModelDetailsQuery;

  @GetMapping("/v1/solvingModels/{id}")
  @PreAuthorize("isAuthorized('LIST_MODELS')")
  public ResponseEntity<ModelDto> get(@PathVariable UUID id) {
    return ok(getModelDetailsQuery.get(id));
  }

  @GetMapping(value = "/v1/solvingModels", params = "policy")
  @PreAuthorize("isAuthorized('LIST_MODELS')")
  public ResponseEntity<List<ModelDto>> getByPolicy(@RequestParam String policy) {
    return ok(getModelDetailsQuery.getByPolicy(policy));
  }
}
