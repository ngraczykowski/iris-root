package com.silenteight.serp.governance.analytics.featurevector;

import com.silenteight.serp.governance.common.signature.Signature;

import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.QueryHint;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;
import static org.hibernate.jpa.QueryHints.HINT_READONLY;

interface AnalyticsFeatureVectorRepository extends Repository<FeatureVector, Long> {

  FeatureVector save(FeatureVector featureVector);

  Optional<FeatureVector> findByVectorSignature(Signature vectorSignature);

  @QueryHints(value = {
      @QueryHint(name = HINT_FETCH_SIZE, value = "50"),
      @QueryHint(name = HINT_READONLY, value = "true"), })
  Stream<FeatureVector> findAll();
}
