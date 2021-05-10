package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.json.ObjectConverter

import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class AlertFacadeSpec extends Specification {

  def objectConverter = Mock(ObjectConverter)
  def repository = Mock(AlertRepository)
  def relationshipProcessor = Mock(RelationshipProcessor)

  def underTest = AlertFacade.builder()
      .relationshipProcessor(relationshipProcessor)
      .repository(repository)
      .build()

  def 'should prepare and save alert'() {
    given:
    def bulkItemId = "1L"
    def alertPayload = "".getBytes()

    when:
    def result = underTest.createAndSaveAlerts(bulkItemId, alertPayload)

    then:
    1 * repository.save(_ as AlertEntity) >> {AlertEntity entity -> entity.id = 2}

    with(result) {
      id == 2
    }
  }
}
