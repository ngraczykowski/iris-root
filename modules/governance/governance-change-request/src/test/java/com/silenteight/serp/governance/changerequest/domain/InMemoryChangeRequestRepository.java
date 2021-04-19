package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;

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
}
