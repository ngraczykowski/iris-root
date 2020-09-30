package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class BulkChangeRestController {

  @NonNull
  private final CreateBulkChangeUseCase createBulkChangeUseCase;

  @PostMapping("/bulk-changes")
  @PreAuthorize("isAuthorized('CREATE_BULK_CHANGE')")
  public ResponseEntity<Void> create(
      @RequestBody @Valid BulkChangeDto dto,
      @RequestHeader(CORRELATION_ID_HEADER) UUID correlationId) {
    log.debug(CHANGE_REQUEST, "Requested to create Bulk Change. id={}", dto.getId());

    RequestCorrelation.set(correlationId);
    createBulkChangeUseCase.apply(
        CreateBulkChangeCommand.builder()
            .bulkChangeId(dto.getId())
            .reasoningBranchIds(dto.getReasoningBranchIds())
            .aiSolution(dto.getAiSolution())
            .active(dto.getActive())
            .cratedAt(dto.getCreatedAt())
            .build());

    log.debug(CHANGE_REQUEST, "Requested to create Bulk Change accepted. id={}", dto.getId());
    return accepted().build();
  }
}
