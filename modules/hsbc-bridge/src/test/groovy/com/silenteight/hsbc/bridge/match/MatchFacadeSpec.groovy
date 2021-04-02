package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.alert.AlertComposite
import com.silenteight.hsbc.bridge.match.event.StoredMatchesEvent
import com.silenteight.hsbc.bridge.bulk.rest.input.Alert
import com.silenteight.hsbc.bridge.bulk.rest.input.AlertSystemInformation
import com.silenteight.hsbc.bridge.bulk.rest.input.CasesWithAlertURL

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
      .matchRawMapper(new MatchRawMapper())
      .build()

  def fixtures = new Fixtures()

  def 'should get match by id'() {
    given:
    def id = 1

    when:
    def result = underTest.getMatch(id)

    then:
    result.rawData == fixtures.matchRawData
    1 * matchRepository.findById(id) >> of(new MatchEntity(2, "", fixtures.matchPayload))
  }

  def 'should prepare and save matches'() {
    given:
    def alertComposite = AlertComposite.builder()
        .id(1)
        .alert(fixtures.alert)
        .build()

    when:
    def result = underTest.prepareAndSaveMatches(alertComposite)

    then:
    1 * matchRepository.save(_ as MatchEntity) >> {MatchEntity entity -> entity.id = 2}
    1 * eventPublisher.publishEvent(_ as StoredMatchesEvent)
    result == [2]
  }

  class Fixtures {

    MatchRawData matchRawData = new MatchRawData()
    byte[] matchPayload = new MatchPayloadConverter().convert(matchRawData)

    Alert alert = new Alert(
        systemInformation: new AlertSystemInformation(
            casesWithAlertURL: [new CasesWithAlertURL()]
        )
    )
  }
}
