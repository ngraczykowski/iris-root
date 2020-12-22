package com.silenteight.serp.governance.featuregroup;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.FeatureGroup;
import com.silenteight.proto.serp.v1.alert.FeatureGroupElement;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.featurevector.dto.StoreFeatureVectorsRequest;

import com.google.protobuf.ByteString;

@RequiredArgsConstructor
public class FeatureGroupService {

  private final FeatureVectorService featureVectorService;
  private final DecisionTreeFacade decisionTreeService;

  public void storeVectors(FeatureGroup featureGroup) {
    FeatureGroupElement matchElement = featureGroup.getMatchElement();
    ByteString matchFeaturesSignature = matchElement.getFeaturesSignature();

    decisionTreeService.ensureAtLeastOneDecisionTreeExists();

    featureVectorService.store(
        StoreFeatureVectorsRequest.of(matchFeaturesSignature, matchElement.getVectorsList()));
  }
}
