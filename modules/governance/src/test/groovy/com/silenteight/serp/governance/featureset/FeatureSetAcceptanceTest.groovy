package com.silenteight.serp.governance.featureset

import com.silenteight.serp.governance.featureset.dto.FeatureSetToStoreDto
import com.silenteight.serp.governance.featureset.dto.StoreFeatureSetRequest

import com.google.protobuf.ByteString
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

class FeatureSetAcceptanceTest extends Specification {

  static final ByteString FEATURES_1 = ByteString.copyFromUtf8("f1")
  static final ByteString FEATURES_2 = ByteString.copyFromUtf8("f2")

  InMemoryFeatureSetRepository featureSetRepo = new InMemoryFeatureSetRepository()
  InMemoryReasoningBranchFeaturesQueryRepository branchFeaturesRepo =
      new InMemoryReasoningBranchFeaturesQueryRepository();
  FeatureSetService service
  FeatureSetFinder finder

  def setup() {
    def configuration = new FeatureSetConfiguration(this.featureSetRepo, this.branchFeaturesRepo)

    service = configuration.featureSetService()
    finder = configuration.featureSetFinder()
  }

  def "null request throws"() {
    when:
    service.storeFeatureSet(null)

    then:
    thrown(NullPointerException)
  }

  def "empty request stores no feature set"() {
    when:
    storeRequested()

    then:
    featureSetRepo.count() == 0
  }

  def "stored feature sets are findable"() {
    given:
    def set1 = featureSetToStore(FEATURES_1, "v1", "v2")
    def set2 = featureSetToStore(FEATURES_2, "v3", "v4", "v5")

    when:
    storeRequested(set1, set2)

    then:
    finder.getByFeaturesSignature(FEATURES_1).getFeatures() == set1.getFeatures()
    finder.getByFeaturesSignature(FEATURES_2).getFeatures() == set2.getFeatures()
  }

  def "two different features sets with same signatures requested to be stored within one request, throws"() {
    given:
    def set1 = featureSetToStore(FEATURES_1, "v1")
    def set2 = featureSetToStore(FEATURES_1, "v2")

    when:
    storeRequested(set1, set2)

    then:
    thrown(RuntimeException.class)
  }

  def "two same features sets requested stored within two requests, only one is stored"() {
    given:
    def set = featureSetToStore(FEATURES_1, "v1")

    when:
    storeRequested(set)
    storeRequested(set)

    then:
    featureSetRepo.count() == 1
  }

  def "should throw when features not found"() {
    when:
    finder.getByFeaturesSignature(FEATURES_1)

    then:
    thrown(EntityNotFoundException.class)
  }

  def "should throw when branch features not found"() {
    when:
    finder.getByFeatureVectorId(1)

    then:
    thrown(EntityNotFoundException.class)
  }

  def "should find branch features correctly"() {
    given:
    def set1 = featureSetToStore(FEATURES_1, "v1", "v2");
    def set2 = featureSetToStore(FEATURES_2, "v1", "v2", "v3");

    when:
    setBranchFeatures(1, 1, set1)
    setBranchFeatures(2, 2, set2)

    then:
    finder.getByFeatureVectorId(1).getFeatures() == set1.getFeatures()
    finder.getByFeatureVectorId(2).getFeatures() == set2.getFeatures()
  }

  private static FeatureSetToStoreDto featureSetToStore(ByteString signature, String... values) {
    new FeatureSetToStoreDto(signature, List.of(values))
  }

  private void storeRequested(FeatureSetToStoreDto... featureSets) {
    def givenRequest = new StoreFeatureSetRequest(Set.of(featureSets))
    service.storeFeatureSet(givenRequest)
  }

  private void setBranchFeatures(long featureSetId, long featureVectorId, FeatureSetToStoreDto... featureSets) {
    featureSets.each {FeatureSetToStoreDto featureSet ->
      branchFeaturesRepo.store(featureSetId, featureVectorId, featureSet.getFeatures());
    }
  }
}
