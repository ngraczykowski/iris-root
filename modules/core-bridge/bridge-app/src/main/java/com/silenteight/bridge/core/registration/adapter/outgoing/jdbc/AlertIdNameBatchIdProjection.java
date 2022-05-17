package com.silenteight.bridge.core.registration.adapter.outgoing.jdbc;

record AlertIdNameBatchIdProjection(
    long id,
    String alertId,
    String name,
    String batchId) {}
