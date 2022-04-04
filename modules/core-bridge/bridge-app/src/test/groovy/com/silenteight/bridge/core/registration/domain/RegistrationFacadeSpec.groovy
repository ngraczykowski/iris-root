package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.recommendation.domain.RecommendationFixtures
import com.silenteight.bridge.core.registration.domain.command.CompleteBatchCommand
import com.silenteight.bridge.core.registration.domain.command.MarkAlertsAsDeliveredCommand
import com.silenteight.bridge.core.registration.domain.command.MarkAlertsAsRecommendedCommand
import com.silenteight.bridge.core.registration.domain.command.VerifyBatchTimeoutCommand
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchPriorityWithStatus

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RegistrationFacadeSpec extends Specification {

  def batchService = Mock(BatchService)
  def alertService = Mock(AlertService)
  def alertAnalysisService = Mock(AlertAnalysisService)
  def batchTimeoutService = Mock(BatchTimeoutService)

  @Subject
  def underTest = new RegistrationFacade(
      batchService, alertService, alertAnalysisService, batchTimeoutService)

  def 'should call register batch method'() {
    given:
    def batchId = Fixtures.BATCH_ID
    def registerBatchCommand = RegistrationFixtures.REGISTER_BATCH_COMMAND

    and:
    batchService.register(registerBatchCommand) >> RegistrationFixtures.BATCH_ID_PROJECTION

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId
  }

  def 'should call notify batch error method'() {
    given:
    def notifyBatchErrorCommand = RegistrationFixtures.NOTIFY_BATCH_ERROR_COMMAND

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchService.notifyBatchError(notifyBatchErrorCommand)
  }

  def 'should find batch priority and call register alerts and matches method'() {
    given:
    def registerAlertsCommand = RegistrationFixtures.REGISTER_ALERTS_COMMAND
    def alert = Alert.builder().batchId('batch_id').build()
    def batchPriority = new BatchPriorityWithStatus(RegistrationFixtures.BATCH_PRIORITY, BatchStatus.STORED)

    when:
    def response = underTest.registerAlertsAndMatches(registerAlertsCommand)

    then:
    1 * batchService.findPendingBatch(registerAlertsCommand.batchId()) >> batchPriority
    1 * alertService.registerAlertsAndMatches(registerAlertsCommand, _ as Integer) >> [alert]
    response.size() == 1
  }

  def 'should call add alerts to analysis'() {
    given:
    def commands = [RegistrationFixtures.ADD_ALERT_TO_ANALYSIS_COMMAND]

    when:
    underTest.addAlertsToAnalysis(commands)

    then:
    1 * alertAnalysisService.addAlertsToAnalysis(commands)
  }

  def 'should complete batch if there is no pending alerts for the given batch'() {
    given:
    def batch = RegistrationFixtures.BATCH_ID_PROJECTION
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(
        RegistrationFixtures.ANALYSIS_NAME, alertNames)
    def completeBatchCommand = new CompleteBatchCommand(RegistrationFixtures.BATCH)

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH
    1 * alertService.updateStatusToRecommended(batch.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(RegistrationFixtures.BATCH) >> true
    1 * batchService.completeBatch(completeBatchCommand)
  }

  def 'should not complete batch if there are pending alerts for the given batch'() {
    given:
    def batch = RegistrationFixtures.BATCH_ID_PROJECTION
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(
        RegistrationFixtures.ANALYSIS_NAME, alertNames)
    def completeBatchCommand = new CompleteBatchCommand(RegistrationFixtures.BATCH)

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH
    1 * alertService.updateStatusToRecommended(batch.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(RegistrationFixtures.BATCH) >> false
    0 * batchService.completeBatch(completeBatchCommand)
  }

  def 'should throw NoSuchElementException when batch not found by analysisName'() {
    given:
    def notExistingAnalysisName = 'notExistingAnalysisName'
    def command = new MarkAlertsAsRecommendedCommand(notExistingAnalysisName, [])

    when:
    underTest.markAlertsAsRecommended(command)

    then:
    1 * batchService.findBatchByAnalysisName(notExistingAnalysisName) >>
        {throw new NoSuchElementException()}
    0 * alertService.updateStatusToRecommended(_ as String, _ as List<String>)
    0 * alertService.hasNoPendingAlerts(_ as Batch)
    0 * batchService.completeBatch(_ as CompleteBatchCommand)
    thrown(NoSuchElementException.class)
  }

  def 'should retrieve batch data with alerts by analysis name'() {
    given:
    def command = RegistrationFixtures.GET_BATCH_WITH_ALERTS_COMMAND

    when:
    def result = underTest.getBatchWithAlerts(command)

    then:
    1 * batchService.findBatchIdWithPolicyByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH_ID_WITH_POLICY_PROJECTION
    1 * alertService.getAlertsAndMatches(Fixtures.BATCH_ID) >> RecommendationFixtures.ALERTS

    result == RecommendationFixtures.BATCH_WITH_ALERTS
  }

  def 'markAlertsAsDelivered should call markBatchAsDelivered when alertNames were not provided'() {
    given:
    def batch = RegistrationFixtures.BATCH
    def analysisName = 'analysis/1'
    def markAlertsAsDeliveredCommand = MarkAlertsAsDeliveredCommand.builder()
        .batchId(batch.id())
        .analysisName(analysisName)
        .alertNames(alertNames)
        .build()

    1 * batchService.findBatchByAnalysisName(analysisName) >> batch

    when:
    underTest.markAlertsAsDelivered(markAlertsAsDeliveredCommand)

    then:
    1 * alertService.updateStatusToDelivered(batch.id(), alertNames)
    1 * batchService.markBatchAsDelivered(batch)

    where:
    alertNames << [null, []]
  }

  @Unroll
  def "markAlertsAsDelivered #should call markBatchAsDelivered when #when"() {
    given:
    def batch = RegistrationFixtures.BATCH
    def analysisName = 'analysis/1'
    def alertNames = ['alert1', 'alert2']
    def markAlertsAsDeliveredCommand = MarkAlertsAsDeliveredCommand.builder()
        .batchId(batch.id())
        .analysisName(analysisName)
        .alertNames(alertNames)
        .build()

    1 * batchService.findBatchByAnalysisName(analysisName) >> batch
    1 * alertService.hasAllDeliveredAlerts(batch) >> hasAllDeliveredAlerts

    when:
    underTest.markAlertsAsDelivered(markAlertsAsDeliveredCommand)

    then:
    1 * alertService.updateStatusToDelivered(batch.id(), alertNames)
    if (hasAllDeliveredAlerts) {
      1 * batchService.markBatchAsDelivered(batch)
    }

    where:
    hasAllDeliveredAlerts || should       || when
    true                  || 'should'     || 'all alerts are delivered'
    false                 || 'should not' || 'not all alerts are delivered'
  }

  def 'should call verify batch timeout'() {
    given:
    def command = new VerifyBatchTimeoutCommand(Fixtures.BATCH_ID)

    when:
    underTest.verifyBatchTimeout(command)

    then:
    1 * batchTimeoutService.verifyBatchTimeout(command)
  }
}
