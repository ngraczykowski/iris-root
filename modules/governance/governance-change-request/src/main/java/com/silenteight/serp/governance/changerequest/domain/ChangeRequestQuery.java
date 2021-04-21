package com.silenteight.serp.governance.changerequest.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.closed.ClosedChangeRequestQuery;
import com.silenteight.serp.governance.changerequest.closed.dto.ClosedChangeRequestDto;
import com.silenteight.serp.governance.changerequest.pending.PendingChangeRequestQuery;
import com.silenteight.serp.governance.changerequest.pending.dto.PendingChangeRequestDto;

import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.CANCELLED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.REJECTED;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RequiredArgsConstructor
class ChangeRequestQuery implements PendingChangeRequestQuery, ClosedChangeRequestQuery {

  private static final int CLOSED_MAX_PAGE_SIZE = 20;
  private static final String SORT_CLOSED_BY_FIELD = "decidedAt";

  @NonNull
  private final ChangeRequestRepository repository;

  @Override
  public List<PendingChangeRequestDto> listPending() {
    return repository
        .findAllByState(PENDING)
        .stream()
        .map(ChangeRequestQuery::mapPending)
        .collect(toList());
  }

  @Override
  public List<ClosedChangeRequestDto> listClosed() {
    Collection<ChangeRequest> changeRequests = repository.findAllByStateIn(
        Set.of(APPROVED, REJECTED, CANCELLED),
        PageRequest.of(0, CLOSED_MAX_PAGE_SIZE, DESC, SORT_CLOSED_BY_FIELD));

    return changeRequests
        .stream()
        .map(ChangeRequestQuery::mapClosed)
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

  private static ClosedChangeRequestDto mapClosed(ChangeRequest changeRequest) {
    return ClosedChangeRequestDto.builder()
        .id(changeRequest.getChangeRequestId())
        .createdBy(changeRequest.getCreatedBy())
        .createdAt(changeRequest.getCreatedAt())
        .creatorComment(changeRequest.getCreatorComment())
        .decidedBy(changeRequest.getDecidedBy())
        .deciderComment(changeRequest.getDeciderComment())
        .decidedAt(changeRequest.getDecidedAt())
        .state(changeRequest.getState().name())
        .build();
  }
}
