package com.silenteight.payments.bridge.datasource.feature.port.outgoing;

import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureOutput;
import com.silenteight.payments.bridge.datasource.feature.port.incoming.BatchFeatureInputResponse;

public interface FeatureMapper {

  String getType();

  BatchFeatureInputResponse map(MatchFeatureOutput matchFeatureOutput);

}
