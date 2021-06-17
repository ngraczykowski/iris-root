package com.silenteight.serp.governance.changerequest.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

interface ChangeRequestRepository extends Repository<ChangeRequest, Long> {

  ChangeRequest save(ChangeRequest changeRequest);

  Collection<ChangeRequest> findAllByStateInOrderByDecidedAtDesc(Set<ChangeRequestState> states);

  Optional<ChangeRequest> findByChangeRequestId(UUID changeRequestId);

  Optional<ChangeRequest> findByModelName(String modelName);
}
