package com.silenteight.serp.governance.analytics.featurevector;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.analytics.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.Signature;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class FeatureVectorService {

  private final AnalyticsFeatureVectorRepository analyticsFeatureVectorRepository;

  public void storeUniqueFeatureVector(CanonicalFeatureVector canonicalFeatureVector) {
    Signature vectorSignature = canonicalFeatureVector.getVectorSignature();
    if (isNewFeatureVector(vectorSignature)) {
      store(canonicalFeatureVector);
    }
  }

  private boolean isNewFeatureVector(Signature vectorSignature) {
    return analyticsFeatureVectorRepository
        .findByVectorSignature(vectorSignature)
        .isEmpty();
  }

  private void store(CanonicalFeatureVector canonicalFeatureVector) {
    FeatureVector featureVector = new FeatureVector(
        canonicalFeatureVector.getVectorSignature(),
        canonicalFeatureVector.getNames(),
        canonicalFeatureVector.getValues());
    analyticsFeatureVectorRepository.save(featureVector);
  }

  public Stream<FeatureVectorDto> getFeatureVectorStream() {
    return analyticsFeatureVectorRepository.findAll()
        .map(this::toFeatureVectorDto);
  }

  private FeatureVectorDto toFeatureVectorDto(FeatureVector featureVector) {
    return FeatureVectorDto.builder()
        .names(featureVector.getNames())
        .values(featureVector.getValues())
        .build();
  }
}
