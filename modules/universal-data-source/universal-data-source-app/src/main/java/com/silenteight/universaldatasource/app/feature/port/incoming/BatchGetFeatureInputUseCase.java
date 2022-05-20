package com.silenteight.universaldatasource.app.feature.port.incoming;

import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;

import java.util.function.Consumer;

public interface BatchGetFeatureInputUseCase {

  void batchGetFeatureInput(
      BatchFeatureRequest batchFeatureRequest,
      Consumer<BatchFeatureInputResponse> consumer);
}
