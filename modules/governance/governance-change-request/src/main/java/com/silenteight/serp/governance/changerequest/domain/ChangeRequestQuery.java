package com.silenteight.serp.governance.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.pending.PendingChangeRequestQuery;
import com.silenteight.serp.governance.changerequest.pending.dto.PendingChangeRequestDto;

import java.util.List;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ChangeRequestQuery implements PendingChangeRequestQuery {

  @NonNull
  private final ChangeRequestRepository changeRequestRepository;

  @Override
  public List<PendingChangeRequestDto> listPending() {
    return changeRequestRepository
        .findAllByState(PENDING)
        .stream()
        .map(ChangeRequestQuery::mapPending)
        .collect(toList());
  }

  private static PendingChangeRequestDto mapPending(ChangeRequest changeRequest) {
    return PendingChangeRequestDto.builder()
        .id(changeRequest.getChangeRequestId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .comment(changeRequest.getCreatorComment())
        .build();
  }
}
