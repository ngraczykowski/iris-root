package com.silenteight.serp.governance.bulkchange

import com.silenteight.proto.serp.v1.api.FeatureSignatures
import com.silenteight.proto.serp.v1.api.FeatureVectorIds
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeRequest
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder
import com.silenteight.serp.governance.featurevector.dto.FeatureVectorView

import com.google.protobuf.ByteString
import spock.lang.Specification

import static com.google.protobuf.ByteString.copyFromUtf8

class ValidateBulkChangeUseCaseSpec extends Specification {

  def decisionTreeFacade = Mock(DecisionTreeFacade)
  def featureVectorFinder = Mock(FeatureVectorFinder)
  def underTest = ValidateBulkChangeUseCase.builder()
      .decisionTreeFacade(decisionTreeFacade)
      .featureVectorFinder(featureVectorFinder)
      .build()

  def decisionTreeId = 1L
  def featureVectorId1 = 1L
  def featureVectorId2 = 2L
  def featureSignature1 = copyFromUtf8("f1")
  def featureSignature2 = copyFromUtf8("f2")

  def 'should validate bulk change request with feature signatures'() {
    given:
    def featureSignatures = FeatureSignatures.newBuilder()
        .addAllFeaturesSignature([featureSignature1, featureSignature2])
        .build()
    def request = ValidateBulkChangeRequest.newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureSignatures(featureSignatures)
        .build()

    when:
    def response = underTest.activate(request)

    then:
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> true
    1 * featureVectorFinder.findExistingFeatureSignatures([featureSignature1, featureSignature2]) >>
        [featureSignature1]

    response.invalidFeatureSignatures.featuresSignatureList == [featureSignature2]
    !response.hasInvalidFeatureVectorIds()
    response.invalidDecisionTreeId != decisionTreeId
  }

  def 'should validate bulk change request with feature vector ids'() {
    given:
    def featureVectorIds = FeatureVectorIds.newBuilder()
        .addAllFeatureVectorId([featureVectorId1, featureVectorId2])
        .build()
    def request = ValidateBulkChangeRequest.newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorIds(featureVectorIds)
        .build()

    when:
    def response = underTest.activate(request)

    then:
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> true
    1 * featureVectorFinder.findExistingFeatureVectorIds([featureVectorId1, featureVectorId2]) >>
        [featureVectorId1]

    response.invalidDecisionTreeId != decisionTreeId
    !response.hasInvalidFeatureSignatures()
    response.invalidFeatureVectorIds.featureVectorIdList == [featureVectorId2]
  }

  def 'should validate bulk change request with decision tree id'() {
    given:
    def request = ValidateBulkChangeRequest.newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .build()

    when:
    def response = underTest.activate(request)

    then:
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> false

    !response.hasInvalidFeatureSignatures()
    !response.hasInvalidFeatureVectorIds()
    response.invalidDecisionTreeId == decisionTreeId
  }

  def 'should validate bulk change request with decision tree id and featureVectorId/signature'() {
    given:
    def featureVectorIds = FeatureVectorIds.newBuilder()
        .addAllFeatureVectorId([featureVectorId1])
        .build()
    def request = ValidateBulkChangeRequest.newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorIds(featureVectorIds)
        .build()

    when:
    def response = underTest.activate(request)

    then:
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> false
    1 * featureVectorFinder.findExistingFeatureVectorIds([featureVectorId1]) >> []

    !response.hasInvalidFeatureSignatures()
    response.hasInvalidFeatureVectorIds()
    response.invalidDecisionTreeId == decisionTreeId
  }

  def createFeatureVectorView(ByteString featureSignature) {
    FeatureVectorView.builder()
        .featuresSignature(featureSignature)
        .vectorSignature(copyFromUtf8("dummy"))
        .build()
  }
}
