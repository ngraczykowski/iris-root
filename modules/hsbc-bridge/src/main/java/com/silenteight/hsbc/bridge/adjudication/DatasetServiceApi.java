package com.silenteight.hsbc.bridge.adjudication;

import java.util.Collection;

public interface DatasetServiceApi {

  String createDataset(Collection<String> alerts);
}
