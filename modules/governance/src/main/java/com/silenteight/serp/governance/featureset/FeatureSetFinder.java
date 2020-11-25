package com.silenteight.serp.governance.featureset;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.featureset.dto.FeatureSetViewDto;

import com.google.protobuf.ByteString;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@RequiredArgsConstructor
public class FeatureSetFinder {

  private final FeatureSetRepository featuresRepository;
  private final ReasoningBranchFeaturesQueryRepository branchFeaturesRepository;

  @Transactional(readOnly = true)
  public FeatureSetViewDto getByFeaturesSignature(ByteString featuresSignature) {
    return featuresRepository.findByFeaturesSignature(toBase64String(featuresSignature))
        .map(FeatureSetEntity::asView)
        .orElseThrow(() -> new EntityNotFoundException(
            "Features not found for given signature: " + featuresSignature));
  }

  @Transactional(readOnly = true)
  public FeatureSetViewDto getByFeatureVectorId(long featureVectorId) {
    return branchFeaturesRepository.findByFeatureVectorId(featureVectorId)
        .map(ReasoningBranchFeaturesQuery::asView)
        .orElseThrow(() -> new EntityNotFoundException(
            "Features not found for given feature vector: " + featureVectorId));
  }
}
