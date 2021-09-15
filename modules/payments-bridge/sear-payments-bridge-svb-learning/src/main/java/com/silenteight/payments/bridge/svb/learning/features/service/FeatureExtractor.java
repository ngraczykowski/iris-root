package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

interface FeatureExtractor {

  FeatureInput extract(LearningMatch learningMatch);
}
