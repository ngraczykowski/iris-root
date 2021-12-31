package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import java.util.List;

public interface FeatureExtractor {

  List<FeatureInput> createFeatureInputs(LearningMatch learningMatch);

}
