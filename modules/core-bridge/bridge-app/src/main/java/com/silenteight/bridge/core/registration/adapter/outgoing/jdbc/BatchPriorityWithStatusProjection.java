package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

import com.silenteight.bridge.core.registration.adapter.outgoing.jdbc.BatchEntity.Status;

record BatchPriorityWithStatusProjection(Integer priority, Status status) {}
