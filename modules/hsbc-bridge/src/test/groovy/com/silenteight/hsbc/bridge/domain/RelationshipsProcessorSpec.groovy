package com.silenteight.hsbc.bridge.domain

import com.silenteight.hsbc.bridge.domain.RelationshipsProcessor.NoRelatedPairException

import spock.lang.Specification

class RelationshipsProcessorSpec extends Specification {

  def underTest = new RelationshipsProcessor()
  def fixtures = new Fixtures()

  def 'should process relationships into RelationshipsComposite'() {
    given:
    def alertSystemInformation = fixtures.alertSystemInformation

    when:
    def relationshipsComposite = underTest.processRelations(alertSystemInformation)

    then:
    assertIndividualCustomer(relationshipsComposite)
    assertEntityCustomer(relationshipsComposite)
  }

  def 'should throw NoRelatedPairException when relationships exist without matching customer'() {
    given:
    def alertSystemInformation = fixtures.alertSystemInformationWithoutCustomer

    when:
    underTest.processRelations(alertSystemInformation)

    then:
    def noRelatedPairException = thrown(NoRelatedPairException)
    noRelatedPairException.message == "No related objects pairs was found !"
  }

  private assertIndividualCustomer(RelationshipsComposite relationshipsComposite) {
    def individualRelatedPair = relationshipsComposite.getRelatedPairs().first()
    def worldCheckIndividuals = individualRelatedPair.
        relatedCustomerIndividualsPair.
        worldCheckIndividuals.
        first()
    def privateListIndividuals = individualRelatedPair.
        relatedCustomerIndividualsPair.
        privateListIndividuals.
        first()
    def countryCtrpScreeningIndividuals = individualRelatedPair.
        relatedCustomerIndividualsPair.
        countryCtrpScreeningIndividuals.
        first()

    individualRelatedPair.caseId == fixtures.individualRelationship.caseId
    individualRelatedPair.relatedCustomerIndividualsPair.customerIndividuals.caseId ==
        fixtures.customerIndividuals.caseId
    individualRelatedPair.relatedCustomerIndividualsPair.customerIndividuals.recordId ==
        fixtures.customerIndividuals.recordId

    worldCheckIndividuals.caseId == fixtures.worldCheckIndividual.caseId
    worldCheckIndividuals.recordId == fixtures.worldCheckIndividual.recordId

    privateListIndividuals.caseId == fixtures.privateListIndividuals.caseId
    privateListIndividuals.recordId == fixtures.privateListIndividuals.recordId

    countryCtrpScreeningIndividuals.caseId == fixtures.countryCtrpScreeningIndividuals.caseId
    countryCtrpScreeningIndividuals.recordId == fixtures.countryCtrpScreeningIndividuals.recordId
  }

  private assertEntityCustomer(RelationshipsComposite relationshipsComposite) {
    def entitiesRelatedPair = relationshipsComposite.getRelatedPairs().get(1)
    def worldCheckEntities = entitiesRelatedPair.
        relatedCustomerEntitiesPair.
        worldCheckEntities.
        first()
    def privateListEntities = entitiesRelatedPair.
        relatedCustomerEntitiesPair.
        worldCheckEntities.
        first()
    def countryCtrpScreeningEntities = entitiesRelatedPair.
        relatedCustomerEntitiesPair.
        worldCheckEntities.
        first()

    entitiesRelatedPair.caseId == fixtures.entitiesRelationship.caseId
    entitiesRelatedPair.relatedCustomerEntitiesPair.customerEntities.caseId ==
        fixtures.customerEntities.caseId
    entitiesRelatedPair.relatedCustomerEntitiesPair.customerEntities.recordId ==
        fixtures.customerEntities.recordId

    worldCheckEntities.caseId == fixtures.worldCheckEntities.caseId
    worldCheckEntities.recordId == fixtures.worldCheckEntities.recordId

    privateListEntities.caseId == fixtures.privateListEntities.caseId
    privateListEntities.recordId == fixtures.privateListEntities.recordId

    countryCtrpScreeningEntities.caseId == fixtures.countryCtrpScreeningEntities.caseId
    countryCtrpScreeningEntities.recordId == fixtures.countryCtrpScreeningEntities.recordId
  }
}
