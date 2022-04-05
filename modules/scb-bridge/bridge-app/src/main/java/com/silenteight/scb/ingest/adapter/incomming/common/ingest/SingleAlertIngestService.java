package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.sep.base.common.messaging.properties.MessagePropertiesProvider;

import java.util.Optional;

// TODO: this should be removed as is part of REST /recommendation endpoint
public interface SingleAlertIngestService {

  Optional<Recommendation> ingestAlertAndTryToReceiveRecommendation(
      Alert alert, MessagePropertiesProvider propertiesProvider);
}
