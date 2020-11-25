package com.silenteight.serp.governance.featurevector;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface FeatureVectorRepository extends Repository<FeatureVectorEntity, Long> {

  FeatureVectorEntity save(FeatureVectorEntity featuresVector);

  Stream<FeatureVectorEntity> findByFeaturesSignature(String featuresSignature);

  Optional<FeatureVectorEntity> findByVectorSignature(String signature);

  Stream<FeatureVectorEntity> findByVectorSignatureIn(List<String> vectorSignatures);

  @Query("SELECT id FROM FeatureVectorEntity WHERE id IN (:ids)")
  List<Long> findExistingIds(@Param("ids") List<Long> ids);

  @Query("SELECT featuresSignature FROM FeatureVectorEntity "
      + "WHERE featuresSignature IN (:signatures)")
  List<String> findExistingFeatureSignatures(@Param("signatures") List<String> featureSignatures);
}
