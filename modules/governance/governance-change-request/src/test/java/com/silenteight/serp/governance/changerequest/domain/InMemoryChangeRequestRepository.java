package com.silenteight.serp.governance.changerequest.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

class InMemoryChangeRequestRepository
    extends BasicInMemoryRepository<ChangeRequest>
    implements ChangeRequestRepository {
}
