package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.closed.ClosedChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.closed.ClosedChangeRequestQuery;
import com.silenteight.sens.webapp.backend.changerequest.pending.PendingChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.pending.PendingChangeRequestQuery;

import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Set;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.REJECTED;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RequiredArgsConstructor
class ChangeRequestQuery implements PendingChangeRequestQuery, ClosedChangeRequestQuery {

  @NonNull
  private final ChangeRequestRepository repository;

  @NonNull
  private final ChangeRequestProperties properties;

  @Override
  public List<PendingChangeRequestDto> listPending() {
    log.info(CHANGE_REQUEST, "Listing pending Change Requests.");

    List<ChangeRequest> changeRequests = repository.findAllByState(PENDING);

    log.info(
        CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());

    return changeRequests
        .stream()
        .map(ChangeRequestQuery::mapPending)
        .collect(toList());
  }

  @Override
  public List<ClosedChangeRequestDto> listClosed() {
    List<ChangeRequest> changeRequests = repository.findAllByStateIn(
        Set.of(APPROVED, REJECTED, CANCELLED),
        PageRequest.of(0, properties.getMaxClosed(), DESC, "decidedAt"));

    return changeRequests
        .stream()
        .map(ChangeRequestQuery::mapClosed)
        .collect(toList());
  }

  private static PendingChangeRequestDto mapPending(ChangeRequest changeRequest) {
    return PendingChangeRequestDto.builder()
        .id(changeRequest.getId())
        .bulkChangeId(changeRequest.getBulkChangeId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .comment(changeRequest.getCreatorComment())
        .build();
  }

  private static ClosedChangeRequestDto mapClosed(ChangeRequest changeRequest) {
    return ClosedChangeRequestDto.builder()
        .id(changeRequest.getId())
        .bulkChangeId(changeRequest.getBulkChangeId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .comment(changeRequest.getCreatorComment())
        .decidedBy(changeRequest.getDecidedBy())
        .deciderComment(changeRequest.getDeciderComment())
        .decidedAt(changeRequest.getDecidedAt())
        .state(changeRequest.getState().name())
        .build();
  }
}
