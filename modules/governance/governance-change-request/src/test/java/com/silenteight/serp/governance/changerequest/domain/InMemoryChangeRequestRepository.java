package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

  @Override
  public List<ChangeRequest> findAllByStateIn(
      Set<ChangeRequestState> states, Pageable pageable) {

    return stream()
        .filter(changeRequest -> isInState(changeRequest, states))
        .collect(toList());
  }

  private static boolean isInState(ChangeRequest changeRequest, Set<ChangeRequestState> states) {
    return states
        .stream()
        .anyMatch(changeRequest::isInState);
  }
}
