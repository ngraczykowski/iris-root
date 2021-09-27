package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter.InputCommand

import spock.lang.Specification

import javax.persistence.EntityManager

import static java.util.stream.Stream.of

class AlertFacadeSpec extends Specification {

  def alertPayloadConverter = Mock(AlertPayloadConverter)
  def entityManger = Mock(EntityManager)
  def repository = Mock(AlertRepository)

  def underTest = AlertFacade.builder()
      .alertPayloadConverter(alertPayloadConverter)
      .repository(repository)
      .entityManager(entityManger)
      .build()

  def 'should create raw alerts'() {
    given:
    def bulkId = "1L"
    def inputStream = Mock(InputStream)

    when:
    underTest.createRawAlerts(bulkId, inputStream)

    then:
    1 * alertPayloadConverter.
        convertAndConsumeAlertData(
            {InputCommand command -> command.bulkId == bulkId && command.inputStream == inputStream},
            underTest)
  }

  def 'should get registered alerts'() {
    given:
    def alerts =
        of('AVIR126SCR5640LU259TEST0018:LU:GR-ESAN:2371395248263046 867ff30e589c42cfeb3c4997fd378a1a24826')

    when:
    def result = underTest.getRegisteredAlerts(alerts)

    then:
    1 * repository.findByExternalIdInAndDiscriminatorInAndNameIsNotNull(_ as Collection) >>
        of(new AlertEntity("bulk-1"))
    result.size() == 1
  }
}
