package com.silenteight.hsbc.bridge.adjudication;

import java.util.Collection;

public interface DatasetServiceApi {

  DatasetDto createDataset(Collection<String> alerts);
}
