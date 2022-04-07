package com.silenteight.connector.ftcc.callback.response;

import org.springframework.data.repository.CrudRepository;

interface CallbackRequestRepository extends
    CrudRepository<CallbackRequestEntity, Long> {
}
