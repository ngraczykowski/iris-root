package com.silenteight.serp.governance.bulkchange

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.State
import com.silenteight.proto.serp.v1.api.ValidateBulkChangeResponse
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder

import com.google.protobuf.ByteString
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

class BulkChangeAcceptanceTest extends Specification implements BulkChangeFixtures {

  DecisionTreeFacade decisionTreeFacade = Mock(DecisionTreeFacade)
  FeatureVectorFinder featureVectorFinder = Mock(FeatureVectorFinder)

  BulkChangeFacade facade = bulkChangeFacade(
      Mock(ApplicationEventPublisher),
      decisionTreeFacade,
      featureVectorFinder)

  def "should create bulk change"() {
    when:
    facade.createBulkBranchChange(createBulkChange)

    then:
    def changes = facade.listBulkBranchChanges(withStatuses()).changesList
    changes.size() == 1

    and:
    with(changes.first()) {
      id == createBulkChange.id
      enablementChange
      solutionChange.solution == createBulkChange.solutionChange.solution
      reasoningBranchIdsList == createBulkChange.reasoningBranchIdsList
    }
  }

  def "should apply bulk change"() {
    given:
    facade.createBulkBranchChange(createBulkChange)

    when:
    facade.applyBulkBranchChange(applyBulkChange)

    then:
    def changes = facade.listBulkBranchChanges(withStatuses([State.STATE_APPLIED])).changesList
    changes.size() == 1

    and:
    def change = changes.first()
    change.id == applyBulkChange.id
  }

  def "should throw exception when trying to apply nonexistent bulk change"() {
    when:
    facade.applyBulkBranchChange(applyBulkChange)

    then:
    thrown(BulkChangeNotFoundException)
  }

  def "should reject bulk change"() {
    given:
    facade.createBulkBranchChange(createBulkChange)

    when:
    facade.rejectBulkBranchChange(rejectBulkChange)

    then:
    def changes = facade.listBulkBranchChanges(withStatuses([State.STATE_REJECTED])).changesList
    changes.size() == 1

    and:
    def change = changes.first()
    change.id == rejectBulkChange.id
  }

  def "should throw exception when trying to reject nonexistent bulk change"() {
    when:
    facade.rejectBulkBranchChange(rejectBulkChange)

    then:
    thrown(BulkChangeNotFoundException)
  }

  def "should throw exception when trying to apply previously rejected bulk change"() {
    given:
    facade.createBulkBranchChange(createBulkChange)
    facade.rejectBulkBranchChange(rejectBulkChange)

    when:
    facade.applyBulkBranchChange(applyBulkChange)

    then:
    thrown(ChangeAlreadyCompletedException)
  }

  def "should not throw any exception when trying to do the same operation twice"() {
    given:
    facade.createBulkBranchChange(createBulkChange)
    facade.applyBulkBranchChange(applyBulkChange)

    when:
    facade.applyBulkBranchChange(applyBulkChange)

    then:
    noExceptionThrown()
  }

  def "should return invalid decision tree id and invalid feature vector ids"() {
    given: "invalid decision tree"
    Long decisionTreeId = validateFeatureVectorIds.decisionTreeId
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> false

    and: "invalid feature vector ids"
    List<Long> featureVectors = validateFeatureVectorIds.featureVectorIds.featureVectorIdList
    1 * featureVectorFinder.findExistingFeatureVectorIds(featureVectors) >> [1L, 2L]

    when: "validate bulk branch change"
    ValidateBulkChangeResponse response = facade.validateBulkBranchChange(validateFeatureVectorIds)

    then: "response contains invalid decision tree and invalid feature vector ids"
    response.invalidDecisionTreeId == decisionTreeId
    response.invalidFeatureVectorIds.featureVectorIdList == [3L, 4L]
  }

  def "should return nothing if decision tree id and feature vector ids are valid"() {
    given: "valid decision tree"
    Long decisionTreeId = validateFeatureVectorIds.decisionTreeId
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> true

    and: "valid feature vectors ids"
    List<Long> featureVectors = validateFeatureVectorIds.featureVectorIds.featureVectorIdList
    1 * featureVectorFinder.findExistingFeatureVectorIds(featureVectors) >> featureVectors

    when: "validate bulk branch change"
    ValidateBulkChangeResponse response = facade.validateBulkBranchChange(validateFeatureVectorIds)

    then: "response contains nothing"
    !response.invalidDecisionTreeId
    response.invalidFeatureVectorIds.featureVectorIdList.isEmpty()
  }

  def "should return invalid decision tree id and invalid vector signatures"() {
    given: "invalid decision tree"
    Long decisionTreeId = validateFeatureSignatures.decisionTreeId
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> false

    and: "invalid feature signatures"
    List<ByteString> featureSignatures = validateFeatureSignatures.
        featureSignatures.
        featuresSignatureList
    1 * featureVectorFinder.findExistingFeatureSignatures(featureSignatures) >>
        existingFeatureSignatures

    when: "validate bulk branch change"
    ValidateBulkChangeResponse response = facade.validateBulkBranchChange(validateFeatureSignatures)

    then: "response contains invalid decision tree and invalid feature signatures"
    response.invalidDecisionTreeId == decisionTreeId
    response.invalidFeatureSignatures.featuresSignatureList == nonexistingFeatureSignatures
  }

  def "should return nothing if decision tree id and vector signatures are valid"() {
    given: "valid decision tree"
    Long decisionTreeId = validateFeatureSignatures.decisionTreeId
    1 * decisionTreeFacade.decisionTreeExists(decisionTreeId) >> true

    and: "valid feature signatures"
    List<ByteString> featureSignatures = validateFeatureSignatures.
        featureSignatures.
        featuresSignatureList
    1 * featureVectorFinder.findExistingFeatureSignatures(featureSignatures) >> featureSignatures

    when: "validate bulk branch change"
    ValidateBulkChangeResponse response = facade.validateBulkBranchChange(validateFeatureSignatures)

    then: "response contains nothing"
    !response.invalidDecisionTreeId
    response.invalidFeatureSignatures.featuresSignatureList.isEmpty()
  }
}
