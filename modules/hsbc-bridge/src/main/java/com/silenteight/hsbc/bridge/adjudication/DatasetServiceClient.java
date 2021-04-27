package com.silenteight.hsbc.bridge.adjudication;

import java.util.Collection;

public interface DatasetServiceClient {

  String createDataset(Collection<String> alerts);
}
