package com.silenteight.serp.governance.vector.domain;

interface FeatureVectorWithUsage {

  String getSignature();

  String getNames();

  String getValues();

  Long getUsageCount();
}
