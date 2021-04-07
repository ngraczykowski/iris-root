package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.match.RelationshipsProcessor.NoRelatedPairException

import spock.lang.Specification

class RelationshipsProcessorSpec extends Specification {

  def fixtures = new Fixtures()

  def 'should process relationships into RelationshipsComposite'() {
    given:
    def alertRawData = fixtures.alertRawData

    when:
    def result = new RelationshipsProcessor(alertRawData).process()

    then:
    assertIndividualCustomer(result)
    assertEntityCustomer(result)
  }

  def 'should throw NoRelatedPairException when relationships exist without matching customer'() {
    given:
    def alertRawData = fixtures.alertRawDataWithoutCustomer

    when:
    new RelationshipsProcessor(alertRawData).process()

    then:
    def noRelatedPairException = thrown(NoRelatedPairException)
    noRelatedPairException.message == "No related objects pairs was found !"
  }

  private assertIndividualCustomer(List<MatchRawData> matches) {
    def individualRelatedPair = matches.first()
    def composite = individualRelatedPair.individualComposite
    def worldCheckIndividuals = composite.worldCheckIndividuals.first()
    def privateListIndividuals = composite.privateListIndividuals.first()
    def countryCtrpScreeningIndividuals = composite.countryCtrpScreeningIndividuals.first()

    individualRelatedPair.caseId == fixtures.individualRelationship.caseId
    composite.customerIndividuals.caseId == fixtures.customerIndividuals.caseId
    composite.customerIndividuals.recordId == fixtures.customerIndividuals.recordId

    worldCheckIndividuals.caseId == fixtures.worldCheckIndividual.caseId
    worldCheckIndividuals.recordId == fixtures.worldCheckIndividual.recordId

    privateListIndividuals.caseId == fixtures.privateListIndividuals.caseId
    privateListIndividuals.recordId == fixtures.privateListIndividuals.recordId

    countryCtrpScreeningIndividuals.caseId == fixtures.countryCtrpScreeningIndividuals.caseId
    countryCtrpScreeningIndividuals.recordId == fixtures.countryCtrpScreeningIndividuals.recordId
  }

  private assertEntityCustomer(List<MatchRawData> matches) {
    def entitiesRelatedPair = matches.get(1)
    def entityComposite = entitiesRelatedPair.entityComposite
    def worldCheckEntities = entityComposite.worldCheckEntities.first()
    def privateListEntities = entityComposite.worldCheckEntities.first()
    def countryCtrpScreeningEntities = entityComposite.worldCheckEntities.first()

    entitiesRelatedPair.caseId == fixtures.entitiesRelationship.caseId
    entityComposite.customerEntities.caseId == fixtures.customerEntities.caseId
    entityComposite.customerEntities.recordId == fixtures.customerEntities.recordId

    worldCheckEntities.caseId == fixtures.worldCheckEntities.caseId
    worldCheckEntities.recordId == fixtures.worldCheckEntities.recordId

    privateListEntities.caseId == fixtures.privateListEntities.caseId
    privateListEntities.recordId == fixtures.privateListEntities.recordId

    countryCtrpScreeningEntities.caseId == fixtures.countryCtrpScreeningEntities.caseId
    countryCtrpScreeningEntities.recordId == fixtures.countryCtrpScreeningEntities.recordId
  }
}
