package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.springframework.data.domain.Pageable;

import java.util.*;

import static java.util.stream.Collectors.toList;

class InMemoryChangeRequestRepository
    extends BasicInMemoryRepository<ChangeRequest>
    implements ChangeRequestRepository {

  @Override
  public Collection<ChangeRequest> findAllByState(ChangeRequestState state) {
    return stream()
        .filter(changeRequest -> changeRequest.isInState(state))
        .collect(toList());
  }

  public List<ChangeRequest> findAllByStateIn(
      Set<ChangeRequestState> states, Pageable pageable) {

    return stream()
        .filter(changeRequest -> isInState(changeRequest, states))
        .collect(toList());
  }

  @Override
  public Optional<ChangeRequest> findByChangeRequestId(UUID changeRequestId) {
    return stream()
        .filter(changeRequest -> changeRequest.hasChangeRequestId(changeRequestId))
        .findFirst();
  }

  private static boolean isInState(ChangeRequest changeRequest, Set<ChangeRequestState> states) {
    return states
        .stream()
        .anyMatch(changeRequest::isInState);
  }
}
