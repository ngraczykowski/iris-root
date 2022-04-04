package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.adapter.outgoing.BatchEntity.Status;

record BatchPriorityWithStatusProjection(Integer priority, Status status) {}
