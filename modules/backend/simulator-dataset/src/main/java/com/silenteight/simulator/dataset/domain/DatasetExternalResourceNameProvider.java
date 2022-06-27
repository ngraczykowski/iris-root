package com.silenteight.simulator.dataset.domain;

import lombok.NonNull;

import java.util.UUID;

public interface DatasetExternalResourceNameProvider {

  String getExternalResourceName(@NonNull UUID datasetId);
}
