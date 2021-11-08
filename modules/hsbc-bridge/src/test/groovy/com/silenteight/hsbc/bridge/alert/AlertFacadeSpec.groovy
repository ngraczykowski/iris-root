package com.silenteight.hsbc.bridge.alert

import com.silenteight.hsbc.bridge.alert.AlertPayloadConverter.InputCommand

import spock.lang.Specification

import java.util.stream.Stream
import javax.persistence.EntityManager

class AlertFacadeSpec extends Specification {

  def alertPayloadConverter = Mock(AlertPayloadConverter)
  def entityManger = Mock(EntityManager)
  def repository = Mock(AlertRepository)
  def alertReProcessor = Mock(AlertReProcessor)

  def underTest = AlertFacade.builder()
      .alertPayloadConverter(alertPayloadConverter)
      .repository(repository)
      .alertReProcessor(alertReProcessor)
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
        Stream.of('AVIR126SCR5640LU259TEST0018:LU:GR-ESAN:2371395248263046 867ff30e589c42cfeb3c4997fd378a1a24826')

    when:
    def result = underTest.getRegisteredAlerts(alerts)

    then:
    1 * repository.findByExternalIdInAndDiscriminatorInAndNameIsNotNull(_ as Collection) >>
        Stream.of(new AlertEntity("bulk-1"))
    result.size() == 1
  }

  def 'should re-process alerts'() {
    when:
    underTest.reProcessAlerts('someBulkId', List.of('alert/1'))

    then:
    1 * alertReProcessor.reProcessAlerts(_ as String, _ as List<String>)
  }
}
