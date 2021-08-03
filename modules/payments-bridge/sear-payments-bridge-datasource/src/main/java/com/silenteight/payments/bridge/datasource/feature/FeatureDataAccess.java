package com.silenteight.payments.bridge.datasource.feature;

import java.util.function.Consumer;

interface FeatureDataAccess {

  void saveFeature(Object feature);

  void streamFeatures(Consumer<String> consumer);
}
