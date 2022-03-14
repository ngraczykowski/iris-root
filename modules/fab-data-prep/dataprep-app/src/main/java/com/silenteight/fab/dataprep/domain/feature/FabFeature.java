package com.silenteight.fab.dataprep.domain.feature;

import com.silenteight.universaldatasource.api.library.Feature;

public interface FabFeature {

  Feature buildFeature(BuildFeatureCommand buildFeatureCommand);
}
