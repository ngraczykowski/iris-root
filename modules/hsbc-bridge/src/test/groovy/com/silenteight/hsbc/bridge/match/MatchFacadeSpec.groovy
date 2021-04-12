package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.alert.AlertComposite
import com.silenteight.hsbc.bridge.alert.AlertRawData
import com.silenteight.hsbc.bridge.domain.CasesWithAlertURL
import com.silenteight.hsbc.bridge.domain.CustomerIndividuals
import com.silenteight.hsbc.bridge.domain.Relationships
import com.silenteight.hsbc.bridge.domain.WorldCheckIndividuals
import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import static java.util.Optional.of

class MatchFacadeSpec extends Specification {

  def eventPublisher = Mock(ApplicationEventPublisher)
  def matchRepository = Mock(MatchRepository)
  def underTest = MatchFacade.builder()
      .eventPublisher(eventPublisher)
      .matchPayloadConverter(new MatchPayloadConverter())
      .matchRepository(matchRepository)
      .build()

  def fixtures = new Fixtures()

  def 'should get match by id'() {
    given:
    def id = 1

    when:
    def result = underTest.getMatch(id)

    then:
    result.rawData == fixtures.matchRawData
    1 * matchRepository.findById(id) >> of(new MatchEntity('match', 2, fixtures.matchPayload))
  }

  def 'should prepare and save matches'() {
    given:
    def alertComposite = AlertComposite.builder()
        .id(1)
        .alertRawData(fixtures.alertRawData)
        .build()

    when:
    def result = underTest.prepareAndSaveMatches(alertComposite)

    then:
    1 * matchRepository.save(_ as MatchEntity) >> {MatchEntity entity -> entity.id = 2}
    1 * eventPublisher.publishEvent(_ as StoredMatchesEvent)
    result.size() == 1
    with(result.first()) {
      internalId == 2
    }
  }

  class Fixtures {

    MatchRawData matchRawData = new MatchRawData()
    byte[] matchPayload = new MatchPayloadConverter().convert(matchRawData)

    Relationships individualRelationship = new Relationships(
        caseId: 1,
        recordId: 2,
        relatedRecordId: 3
    )

    CustomerIndividuals customerIndividual = new CustomerIndividuals(
        caseId: 1,
        recordId: 2
    )

    WorldCheckIndividuals worldCheckIndividual = new WorldCheckIndividuals(
        caseId: 1,
        recordId: 3
    )

    CasesWithAlertURL caseWithAlertURL = new CasesWithAlertURL(
        id: 1
    )

    AlertRawData alertRawData = new AlertRawData(
        casesWithAlertURL: [caseWithAlertURL],
        relationships: [individualRelationship],
        customerIndividuals: [customerIndividual],
        worldCheckIndividuals: [worldCheckIndividual],
        customerEntities: [],
        worldCheckEntities: [],
        privateListEntities: [],
        privateListIndividuals: [],
        countryCtrpScreeningEntities: [],
        countryCtrpScreeningIndividuals: []
    )
  }
}
