package com.silenteight.simulator.dataset.domain;

import com.silenteight.simulator.dataset.domain.exception.NonActiveDatasetInSet;

import java.util.Set;

public interface DatasetValidator {

  void assertAllDatasetsActive(Set<String> datasets) throws NonActiveDatasetInSet;
}
