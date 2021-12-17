package com.silenteight.bridge.core.registration.domain.port.outgoing;

import com.silenteight.bridge.core.registration.domain.Batch;

import java.util.Optional;

public interface BatchRepository {

  Optional<Batch> findById(String id);

  Batch create(Batch batch);
}
