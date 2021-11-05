package com.silenteight.adjudication.api.library.v1.dataset;

import java.util.Collection;

public interface DatasetServiceClient {

  String createDataset(Collection<String> alerts);
}
