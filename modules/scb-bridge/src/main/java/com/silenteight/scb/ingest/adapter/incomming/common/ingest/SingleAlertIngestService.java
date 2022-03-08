package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import java.util.Optional;

public interface SingleAlertIngestService {

  void ingestOrderedAlert(Alert fetch, MessagePropertiesProvider propertiesProvider);

  Optional<Recommendation> ingestAlertAndTryToReceiveRecommendation(
      Alert alert, MessagePropertiesProvider propertiesProvider);
}
