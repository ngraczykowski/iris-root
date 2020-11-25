package com.silenteight.serp.governance.bulkchange;

import lombok.Builder;

import com.silenteight.proto.serp.v1.api.FeatureSignatures;
import com.silenteight.proto.serp.v1.api.FeatureVectorIds;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest;
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeResponse;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;

import com.google.protobuf.ByteString;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

@Builder
class ValidateBulkChangeUseCase {

  private final DecisionTreeFacade decisionTreeFacade;
  private final FeatureVectorFinder featureVectorFinder;

  ValidateBulkChangeResponse activate(ValidateBulkChangeRequest request) {
    var responseBuilder = ValidateBulkChangeResponse.newBuilder();

    findInvalidDecisionTreeId(request.getDecisionTreeId())
        .ifPresent(responseBuilder::setInvalidDecisionTreeId);

    if (request.hasFeatureSignatures()) {
      var invalidFeatureSignatures = findInvalidFeatureSignatures(request.getFeatureSignatures());
      responseBuilder.setInvalidFeatureSignatures(invalidFeatureSignatures);
    }

    if (request.hasFeatureVectorIds()) {
      var invalidFeatureVectorIds = findInvalidFeatureVectorIds(request.getFeatureVectorIds());
      responseBuilder.setInvalidFeatureVectorIds(invalidFeatureVectorIds);
    }

    return responseBuilder.build();
  }

  private FeatureVectorIds findInvalidFeatureVectorIds(FeatureVectorIds vectorIds) {
    var invalidFeatureIds = findNotExistingFeatureVectorIds(vectorIds.getFeatureVectorIdList());

    return FeatureVectorIds.newBuilder()
        .addAllFeatureVectorId(invalidFeatureIds)
        .build();
  }

  private FeatureSignatures findInvalidFeatureSignatures(FeatureSignatures signatures) {
    var invalidFeatureSignatures = findNotExistingSignatures(signatures.getFeaturesSignatureList());

    return FeatureSignatures.newBuilder()
        .addAllFeaturesSignature(invalidFeatureSignatures)
        .build();
  }

  private List<ByteString> findNotExistingSignatures(List<ByteString> featureSignatures) {
    var existingSignatures = featureVectorFinder.findExistingFeatureSignatures(featureSignatures);

    return featureSignatures.stream()
        .filter(not(existingSignatures::contains))
        .collect(toList());
  }

  private List<Long> findNotExistingFeatureVectorIds(List<Long> featureVectorIds) {
    var existingIds = featureVectorFinder.findExistingFeatureVectorIds(featureVectorIds);

    return featureVectorIds.stream()
        .filter(not(existingIds::contains))
        .collect(toList());
  }

  private Optional<Long> findInvalidDecisionTreeId(long decisionTreeId) {
    return decisionTreeFacade.decisionTreeExists(decisionTreeId) ? empty() : of(decisionTreeId);
  }
}
