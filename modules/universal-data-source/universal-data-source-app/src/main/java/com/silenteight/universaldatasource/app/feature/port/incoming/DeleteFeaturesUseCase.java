package com.silenteight.universaldatasource.app.feature.port.incoming;

import java.util.List;

public interface DeleteFeaturesUseCase {

  void delete(List<String> alerts);
}
