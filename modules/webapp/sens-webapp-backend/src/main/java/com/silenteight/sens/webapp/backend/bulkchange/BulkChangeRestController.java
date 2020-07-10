package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.parser.ReasoningBranchIdParser;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER_OR_BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.Authority.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class BulkChangeRestController {

  @NonNull
  private final BulkChangeQuery bulkChangeQuery;
  @NonNull
  private final CreateBulkChangeUseCase createBulkChangeUseCase;

  @GetMapping("/bulk-changes")
  @PreAuthorize(APPROVER_OR_BUSINESS_OPERATOR)
  public ResponseEntity<List<BulkChangeDto>> pendingBulkChanges() {
    log.debug(CHANGE_REQUEST, "Listing pending Bulk Changes");

    List<BulkChangeDto> bulkChanges = bulkChangeQuery.listPending();

    log.debug(CHANGE_REQUEST, "Found {} pending Bulk Changes", bulkChanges.size());

    return ok(bulkChanges);
  }

  @PostMapping("/bulk-changes")
  @PreAuthorize(BUSINESS_OPERATOR)
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

  @GetMapping("/bulk-changes/ids")
  @PreAuthorize(APPROVER_OR_BUSINESS_OPERATOR)
  public ResponseEntity<List<BulkChangeIdsForReasoningBranchDto>> getIds(
      @RequestParam List<String> reasoningBranchId) {
    log.debug(CHANGE_REQUEST, "Requested to get Bulk Change IDs, reasoningBranchId={}",
        reasoningBranchId);

    List<BulkChangeIdsForReasoningBranchDto> bulkChangeIds =
        bulkChangeQuery.getIds(toReasoningBranchIds(reasoningBranchId));

    log.debug(CHANGE_REQUEST, "Found {} Bulk Change IDs", bulkChangeIds.size());
    return ok(bulkChangeIds);
  }

  private static List<ReasoningBranchIdDto> toReasoningBranchIds(List<String> ids) {
    return ids
        .stream()
        .map(ReasoningBranchIdParser::parse)
        .map(ReasoningBranchIdDto::valueOf)
        .collect(toList());
  }
}
