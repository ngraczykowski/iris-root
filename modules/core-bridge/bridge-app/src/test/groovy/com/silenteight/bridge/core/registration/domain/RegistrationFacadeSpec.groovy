package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.recommendation.domain.RecommendationFixtures
import com.silenteight.bridge.core.registration.domain.command.*
import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchPriority
import com.silenteight.bridge.core.registration.domain.model.BatchPriorityWithStatus

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class RegistrationFacadeSpec extends Specification {

  def batchService = Mock(BatchService)
  def alertService = Mock(AlertService)
  def udsFedAlertsService = Mock(UdsFedAlertsService)
  def batchTimeoutService = Mock(BatchTimeoutService)
  def dataRetentionService = Mock(DataRetentionService)

  @Subject
  def underTest = new RegistrationFacade(batchService, alertService, udsFedAlertsService, batchTimeoutService, dataRetentionService)

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

  def 'should call process UDS fed alerts'() {
    given:
    def commands = [RegistrationFixtures.PROCESS_UDS_FED_ALERTS_COMMAND]

    when:
    underTest.processUdsFedAlerts(commands)

    then:
    1 * udsFedAlertsService.processUdsFedAlerts(commands)
  }

  def 'should complete batch if there is no pending alerts for the given batch'() {
    given:
    def batchId = RegistrationFixtures.BATCH_ID_PROJECTION
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(
        RegistrationFixtures.ANALYSIS_NAME, alertNames, false)
    def batch = RegistrationFixtures.BATCH

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH
    1 * alertService.updateStatusToRecommended(batchId.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(RegistrationFixtures.BATCH) >> true
    1 * batchService.completeBatch(batch)
  }

  def 'should complete batch if there are pending alerts for the given batch, but batch is timed out'() {
    given:
    def batchId = RegistrationFixtures.BATCH_ID_PROJECTION
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(
        RegistrationFixtures.ANALYSIS_NAME, alertNames, true)
    def batch = RegistrationFixtures.BATCH

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH
    1 * alertService.updateStatusToRecommended(batchId.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(RegistrationFixtures.BATCH) >> false
    1 * batchService.completeBatch(batch)
  }

  def 'should not complete batch if there are pending alerts for the given batch'() {
    given:
    def batchId = RegistrationFixtures.BATCH_ID_PROJECTION
    def alertNames = ['firstAlertName', 'secondAlertName']
    def markAlertsAsRecommendedCommand = new MarkAlertsAsRecommendedCommand(
        RegistrationFixtures.ANALYSIS_NAME, alertNames, false)
    def batch = RegistrationFixtures.BATCH

    when:
    underTest.markAlertsAsRecommended(markAlertsAsRecommendedCommand)

    then:
    1 * batchService.findBatchByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >>
        RegistrationFixtures.BATCH
    1 * alertService.updateStatusToRecommended(batchId.id(), alertNames)
    1 * alertService.hasNoPendingAlerts(RegistrationFixtures.BATCH) >> false
    0 * batchService.completeBatch(batch)
  }

  def 'should throw NoSuchElementException when batch not found by analysisName'() {
    given:
    def notExistingAnalysisName = 'notExistingAnalysisName'
    def command = new MarkAlertsAsRecommendedCommand(notExistingAnalysisName, [], false)

    when:
    underTest.markAlertsAsRecommended(command)

    then:
    1 * batchService.findBatchByAnalysisName(notExistingAnalysisName) >>
        {throw new NoSuchElementException()}
    0 * alertService.updateStatusToRecommended(_ as String, _ as List<String>)
    0 * alertService.hasNoPendingAlerts(_ as Batch)
    0 * batchService.completeBatch(_ as Batch)
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

  def 'should retrieve alerts with matches by batch id'() {
    given:
    def command = RegistrationFixtures.GET_ALERTS_WITH_MATCHES_COMMAND

    when:
    def result = underTest.getAlertsWithMatches(command)

    then:
    1 * alertService.getAlertsAndMatches(Fixtures.BATCH_ID) >> RecommendationFixtures.ALERTS

    result == RecommendationFixtures.ALERTS
  }

  @Unroll
  def "markAlertsAsDelivered should get batch by #desc"() {
    given:
    def markAlertsAsDeliveredCommand = MarkAlertsAsDeliveredCommand.builder()
        .batchId(batchId)
        .analysisName(analysisName)
        .alertNames([])
        .build()

    when:
    underTest.markAlertsAsDelivered(markAlertsAsDeliveredCommand)

    then:
    if (batchId) {
      1 * batchService.findBatchById(batchId) >> RegistrationFixtures.BATCH
    }
    if (analysisName) {
      1 * batchService.findBatchByAnalysisName(analysisName) >> RegistrationFixtures.BATCH
    }
    1 * alertService.updateStatusToDelivered(Fixtures.BATCH_ID, [])

    where:
    batchId           | analysisName                       || desc
    Fixtures.BATCH_ID | null                               || 'batchId'
    Fixtures.BATCH_ID | ''                                 || 'batchId'
    null              | RegistrationFixtures.ANALYSIS_NAME || 'analysisName'
    ''                | RegistrationFixtures.ANALYSIS_NAME || 'analysisName'
  }

  def 'markAlertsAsDelivered should throw exception when neither batchId nor analysisName were provided'() {
    given:
    def markAlertsAsDeliveredCommand = MarkAlertsAsDeliveredCommand.builder().build()

    when:
    underTest.markAlertsAsDelivered(markAlertsAsDeliveredCommand)

    then:
    thrown(IllegalStateException)
  }

  def 'markAlertsAsDelivered should call markBatchAsDelivered when alertNames were not provided'() {
    given:
    def batch = RegistrationFixtures.BATCH
    def markAlertsAsDeliveredCommand = MarkAlertsAsDeliveredCommand.builder()
        .batchId(batch.id())
        .alertNames(alertNames)
        .build()

    1 * batchService.findBatchById(batch.id()) >> batch

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
    def alertNames = ['alert1', 'alert2']
    def markAlertsAsDeliveredCommand = MarkAlertsAsDeliveredCommand.builder()
        .batchId(batch.id())
        .alertNames(alertNames)
        .build()

    1 * batchService.findBatchById(batch.id()) >> batch
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

  def 'should call start data retention'() {
    given:
    def command = StartDataRetentionCommand.builder().build()

    when:
    underTest.startDataRetention(command)

    then:
    1 * dataRetentionService.start(command)
  }

  def 'should find batch priority'() {
    given:
    def command = new GetBatchPriorityCommand(RegistrationFixtures.ANALYSIS_NAME)

    when:
    def result = underTest.getBatchPriority(command)

    then:
    1 * batchService.findBatchPriority(RegistrationFixtures.ANALYSIS_NAME) >> new BatchPriority(RegistrationFixtures.BATCH_PRIORITY)
    result.priority() == RegistrationFixtures.BATCH_PRIORITY
  }

  @Unroll
  def 'should return (#isSimulation) when #text simulation batch'() {
    given:
    def analysisName = RegistrationFixtures.ANALYSIS_NAME

    when:
    def result = underTest.isSimulationBatch(analysisName)

    then:
    1 * batchService.findBatchByAnalysisName(RegistrationFixtures.ANALYSIS_NAME) >> registeredBatch
    result == isSimulation

    where:
    isSimulation | text        || registeredBatch
    false        | 'it is not' || RegistrationFixtures.BATCH
    true         | 'it is '    || RegistrationFixtures.SIMULATION_BATCH
  }
}
