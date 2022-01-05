package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.RegisterAlertsCommand.AlertWithMatches
import com.silenteight.bridge.core.registration.domain.model.BatchId

import spock.lang.Specification
import spock.lang.Subject

class RegistrationFacadeSpec extends Specification {

  def batchService = Mock(BatchService)
  def alertService = Mock(AlertService)
  def alertAnalysisService = Mock(AlertAnalysisService)

  @Subject
  def underTest = new RegistrationFacade(batchService, alertService, alertAnalysisService)

  def "should call register batch method"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25, "batchMetadata")

    and:
    batchService.register(registerBatchCommand) >> new BatchId(batchId)

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId
  }

  def "should call notify batch error method"() {
    given:
    def batchId = UUID.randomUUID().toString()
    def errorDescription = "error occurred"
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(batchId, errorDescription, "batchMetadata")

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchService.notifyBatchError(notifyBatchErrorCommand)
  }

  def "should call register alerts and matches method"() {
    given:
    def registerAlertsCommand = new RegisterAlertsCommand(
        'batch_id',
        [AlertWithMatches.builder().build()]
    )

    when:
    underTest.registerAlertsAndMatches(registerAlertsCommand)

    then:
    1 * alertService.registerAlertsAndMatches(registerAlertsCommand)
  }

  def "should call add alerts to analysis"() {
    given:
    def commands = [new AddAlertToAnalysisCommand(
        "batchId",
        "alertId",
        ["matchId"].toSet()
    )]

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    1 * alertAnalysisService.addAlertsToAnalysis(commands)
  }
}
