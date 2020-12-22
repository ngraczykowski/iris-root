package com.silenteight.serp.governance.featurevector;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.serp.v1.alert.VectorValue;
import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import com.google.protobuf.ByteString;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestInMemoryFeatureVectorRepository
    extends BasicInMemoryRepository<FeatureVectorEntity> implements FeatureVectorRepository {

  public void store(FeatureVectorToStore vector) {
    FeatureVectorEntity entity = new FeatureVectorEntity(
        vector.getFeatureSignature(), vector.getVectorSignature(), vector.getValues());
    entity.setId(vector.getId());

    save(entity);
  }

  @Override
  public Stream<FeatureVectorEntity> findByFeaturesSignature(String featuresSignature) {
    return getInternalStore()
        .values()
        .stream()
        .filter(fv -> fv.getFeaturesSignature().equals(featuresSignature));
  }

  @Override
  public Optional<FeatureVectorEntity> findByVectorSignature(String vectorSignature) {
    return getInternalStore()
        .values()
        .stream()
        .filter(fv -> fv.getVectorSignature().equals(vectorSignature))
        .findAny();
  }

  @Override
  public Stream<FeatureVectorEntity> findByVectorSignatureIn(List<String> vectorSignatures) {
    return getInternalStore()
        .values()
        .stream().filter(fv -> vectorSignatures.contains(fv.getVectorSignature()));
  }

  @Override
  public List<Long> findExistingIds(List<Long> featureVectorIds) {
    return getInternalStore()
        .values()
        .stream()
        .map(FeatureVectorEntity::getId)
        .filter(featureVectorIds::contains)
        .collect(Collectors.toList());
  }

  @Override
  public List<String> findExistingFeatureSignatures(
      List<String> featureSignatures) {
    return getInternalStore()
        .values()
        .stream()
        .map(FeatureVectorEntity::getFeaturesSignature)
        .filter(featureSignatures::contains)
        .collect(Collectors.toList());
  }

  @Value
  @Builder
  public static class FeatureVectorToStore {

    long id;
    ByteString vectorSignature;
    ByteString featureSignature;
    List<VectorValue> values;
  }
}
