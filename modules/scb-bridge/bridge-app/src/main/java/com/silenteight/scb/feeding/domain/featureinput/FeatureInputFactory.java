package com.silenteight.scb.feeding.domain.featureinput;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match;
import com.silenteight.universaldatasource.api.library.Feature;

public interface FeatureInputFactory {
  Feature create(Alert alert, Match match);
}
