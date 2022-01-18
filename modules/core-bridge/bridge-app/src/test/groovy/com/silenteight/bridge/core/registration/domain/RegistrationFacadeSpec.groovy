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

  def 'should call register batch method'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25, 'batchMetadata')

    and:
    batchService.register(registerBatchCommand) >> new BatchId(batchId)

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId
  }

  def 'should call notify batch error method'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def errorDescription = 'error occurred'
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(
        batchId, errorDescription, 'batchMetadata')

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchService.notifyBatchError(notifyBatchErrorCommand)
  }

  def 'should call register alerts and matches method'() {
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

  def 'should call add alerts to analysis'() {
    given:
    def commands = [new AddAlertToAnalysisCommand(
        'batchId',
        'alertId',
        ['matchId'].toSet()
    )]

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    1 * alertAnalysisService.addAlertsToAnalysis(commands)
  }

  def 'should complete batch if there is no pending alerts for the given batch'() {
    given:
    def batch = new BatchId('batchId')
    def analysisName = 'analysisName'
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(analysisName, alertNames)
    def completeBatchCommand = new CompleteBatchCommand(batch.id(), alertNames)

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchId(analysisName) >> batch
    1 * alertService.updateStatusToRecommended(batch.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(batch.id()) >> true
    1 * batchService.completeBatch(completeBatchCommand)
  }

  def 'should not complete batch if there are pending alerts for the given batch'() {
    given:
    def batch = new BatchId('batchId')
    def analysisName = 'analysisName'
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(analysisName, alertNames)
    def completeBatchCommand = new CompleteBatchCommand(batch.id(), alertNames)

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchId(analysisName) >> batch
    1 * alertService.updateStatusToRecommended(batch.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(batch.id()) >> false
    0 * batchService.completeBatch(completeBatchCommand)
  }

  def 'should throw NoSuchElementException when batch not found by analysisName'() {
    given:
    def notExistingAnalysisName = 'notExistingAnalysisName'
    def command = new MarkAlertsAsRecommendedCommand(notExistingAnalysisName, [])

    when:
    underTest.markAlertsAsRecommended(command)

    then:
    1 * batchService.findBatchId(notExistingAnalysisName) >> {throw new NoSuchElementException()}
    0 * alertService.updateStatusToRecommended(_ as String, _ as List<String>)
    0 * alertService.hasNoPendingAlerts(_ as String)
    0 * batchService.completeBatch(_ as CompleteBatchCommand)
    thrown(NoSuchElementException.class)
  }
}
