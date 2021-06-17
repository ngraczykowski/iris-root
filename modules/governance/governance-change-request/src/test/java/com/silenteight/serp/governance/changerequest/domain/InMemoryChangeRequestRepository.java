package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.*;

import static java.util.stream.Collectors.toList;

class InMemoryChangeRequestRepository
    extends BasicInMemoryRepository<ChangeRequest>
    implements ChangeRequestRepository {

  public Collection<ChangeRequest> findAllByStateInOrderByDecidedAtDesc(
      Set<ChangeRequestState> states) {

    return stream()
        .filter(changeRequest -> isInState(changeRequest, states))
        .sorted(Comparator.comparing(ChangeRequest::getDecidedAt).reversed())
        .collect(toList());
  }

  private static boolean isInState(ChangeRequest changeRequest, Set<ChangeRequestState> states) {
    return states
        .stream()
        .anyMatch(changeRequest::isInState);
  }

  @Override
  public Optional<ChangeRequest> findByChangeRequestId(UUID changeRequestId) {
    return stream()
        .filter(changeRequest -> changeRequest.hasChangeRequestId(changeRequestId))
        .findFirst();
  }

  @Override
  public Optional<ChangeRequest> findByModelName(String modelName) {
    return stream()
        .filter(changeRequest -> changeRequest.hasModelName(modelName))
        .findFirst();
  }
}
