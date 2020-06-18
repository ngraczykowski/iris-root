package com.silenteight.sens.webapp.backend.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.domain.dto.ChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.pending.PendingChangeRequestQuery;

import java.util.List;

import static com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
class ChangeRequestQuery implements PendingChangeRequestQuery {

  @NonNull
  private final ChangeRequestRepository repository;

  @Override
  public List<ChangeRequestDto> listPending() {
    log.info(CHANGE_REQUEST, "Listing pending Change Requests.");

    List<ChangeRequest> changeRequests = repository.findAllByState(PENDING);

    log.info(
        CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());

    return changeRequests
        .stream()
        .map(ChangeRequestQuery::map)
        .collect(toList());
  }

  private static ChangeRequestDto map(ChangeRequest changeRequest) {
    return ChangeRequestDto.builder()
        .id(changeRequest.getId())
        .bulkChangeId(changeRequest.getBulkChangeId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .comment(changeRequest.getCreatorComment())
        .build();
  }
}
