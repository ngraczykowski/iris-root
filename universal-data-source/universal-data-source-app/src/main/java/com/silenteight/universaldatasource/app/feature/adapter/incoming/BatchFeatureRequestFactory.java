package com.silenteight.universaldatasource.app.feature.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(FeaturesProperties.class)
class BatchFeatureRequestFactory {

  BatchFeatureRequest create(
      String agentInputType, List<String> matches, List<String> featureNames) {
    return BatchFeatureRequest.builder()
        .agentInputType(agentInputType)
        .matches(matches)
        .features(featureNames)
        .build();
  }
}
