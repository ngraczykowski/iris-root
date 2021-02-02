package com.silenteight.serp.governance.analytics.featurevector;

interface FeatureVectorWithUsage {

  String getSignature();

  String getNames();

  String getValues();

  Long getUsageCount();
}
