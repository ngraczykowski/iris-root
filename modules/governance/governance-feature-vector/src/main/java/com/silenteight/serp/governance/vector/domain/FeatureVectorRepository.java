package com.silenteight.serp.governance.vector.domain;

import com.silenteight.serp.governance.common.signature.Signature;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

interface FeatureVectorRepository extends Repository<FeatureVector, Long> {

  FeatureVector save(FeatureVector featureVector);

  Optional<FeatureVector> findByVectorSignature(Signature vectorSignature);

  @QueryHints(value = {
      @QueryHint(name = HINT_FETCH_SIZE, value = "50"),
      @QueryHint(name = HINT_READONLY, value = "true"), })
  @Query(value = "SELECT"
      + "   fv.names as names,"
      + "   fv.values as values,"
      + "   fvu.usage_count as usageCount,"
      + "   fv.vector_signature as signature"
      + " FROM governance_analytics_feature_vector fv"
      + " JOIN governance_analytics_feature_vector_usage fvu "
      + "   ON fv.vector_signature = fvu.vector_signature"
      + " ORDER BY fvu.usage_count DESC", nativeQuery = true)
  Stream<FeatureVectorWithUsage> findAllWithUsage();

  @QueryHints(value = {
      @QueryHint(name = HINT_FETCH_SIZE, value = "50"),
      @QueryHint(name = HINT_READONLY, value = "true"), })
  @Query(value = "SELECT"
      + "   fv.names as names,"
      + "   fv.values as values,"
      + "   fvu.usage_count as usageCount,"
      + "   fv.vector_signature as signature"
      + " FROM governance_analytics_feature_vector fv"
      + " JOIN governance_analytics_feature_vector_usage fvu "
      + "   ON fv.vector_signature = fvu.vector_signature"
      + " ORDER BY fvu.usage_count DESC, fv.vector_signature DESC"
      + " OFFSET :offset"
      + " LIMIT :limit", nativeQuery = true)
  Stream<FeatureVectorWithUsage> findAllWithUsagePageable(long offset, int limit);

  @Query(value = "SELECT DISTINCT fv.names"
      + " FROM governance_analytics_feature_vector fv", nativeQuery = true)
  List<String> findDistinctFeatureNames();

  @Query(value = "SELECT count(*) FROM governance_analytics_feature_vector", nativeQuery = true)
  int countAll();
}
