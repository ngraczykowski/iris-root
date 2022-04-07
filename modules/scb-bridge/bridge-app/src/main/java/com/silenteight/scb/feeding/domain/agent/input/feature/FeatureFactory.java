package com.silenteight.scb.feeding.domain.agent.input.feature;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.Feature;

public interface FeatureFactory {
  Feature create(Alert alert, Match match);
}
