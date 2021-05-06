package com.silenteight.serp.governance.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.details.ChangeRequestDetailsQuery;
import com.silenteight.serp.governance.changerequest.details.dto.ChangeRequestDetailsDto;
import com.silenteight.serp.governance.changerequest.domain.exception.ChangeRequestNotFoundException;
import com.silenteight.serp.governance.changerequest.list.ListChangeRequestsQuery;
import com.silenteight.serp.governance.changerequest.list.dto.ChangeRequestDto;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class ChangeRequestQuery implements ListChangeRequestsQuery, ChangeRequestDetailsQuery {

  @NonNull
  private final ChangeRequestRepository repository;

  @Override
  public Collection<ChangeRequestDto> list(Set<ChangeRequestState> states) {
    return repository
        .findAllByStateInOrderByDecidedAtDesc(states)
        .stream()
        .map(ChangeRequestQuery::toDto)
        .collect(toList());
  }

  private static ChangeRequestDto toDto(ChangeRequest changeRequest) {
    return ChangeRequestDto.builder()
        .id(changeRequest.getChangeRequestId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .creatorComment(changeRequest.getCreatorComment())
        .decidedBy(changeRequest.getDecidedBy())
        .deciderComment(changeRequest.getDeciderComment())
        .decidedAt(changeRequest.getDecidedAt())
        .state(changeRequest.getState().name())
        .modelName(changeRequest.getModelName())
        .build();
  }

  @Override
  public ChangeRequestDetailsDto details(@NonNull UUID changeRequestId) {
    return repository
        .findByChangeRequestId(changeRequestId)
        .map(ChangeRequestQuery::toDetailsDto)
        .orElseThrow(() -> new ChangeRequestNotFoundException(changeRequestId));
  }

  private static ChangeRequestDetailsDto toDetailsDto(ChangeRequest changeRequest) {
    return ChangeRequestDetailsDto.builder()
        .id(changeRequest.getChangeRequestId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .creatorComment(changeRequest.getCreatorComment())
        .decidedBy(changeRequest.getDecidedBy())
        .deciderComment(changeRequest.getDeciderComment())
        .decidedAt(changeRequest.getDecidedAt())
        .state(changeRequest.getState().name())
        .modelName(changeRequest.getModelName())
        .build();
  }
}
