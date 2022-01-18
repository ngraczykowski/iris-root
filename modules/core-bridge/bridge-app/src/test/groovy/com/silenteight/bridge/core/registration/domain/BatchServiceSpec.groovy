package com.silenteight.bridge.core.registration.domain

import com.silenteight.bridge.core.registration.domain.model.*
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics.RecommendationsStats
import com.silenteight.bridge.core.registration.domain.port.outgoing.AnalysisService
import com.silenteight.bridge.core.registration.domain.port.outgoing.BatchRepository
import com.silenteight.bridge.core.registration.domain.port.outgoing.DefaultModelService
import com.silenteight.bridge.core.registration.domain.port.outgoing.EventPublisher

import spock.lang.Specification
import spock.lang.Subject

class BatchServiceSpec extends Specification {

  def eventPublisher = Mock(EventPublisher)
  def analysisService = Mock(AnalysisService)
  def batchRepository = Mock(BatchRepository)
  def modelService = Mock(DefaultModelService)
  def batchStatisticsService = Mock(BatchStatisticsService)

  @Subject
  def underTest = new BatchService(
      eventPublisher, analysisService, batchRepository, modelService, batchStatisticsService)

  def 'should register batch'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = 'analysisName'
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25, 'batchMetadata')

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    1 * modelService.getForSolving() >> DefaultModel.builder().build()
    1 * analysisService.create(_ as DefaultModel) >> new Analysis(analysisName)
    1 * batchRepository.create(_ as Batch) >> Batch.newOne(batchId, analysisName, 123, 'batchMetadata')
  }

  def 'should return batch if already exists'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = 'analysisName'
    def registerBatchCommand = new RegisterBatchCommand(batchId, 25, 'batchMetadata')

    and:
    batchRepository.findById(batchId) >> Optional.of(Batch.newOne(batchId, analysisName, 123, 'batchMetadata'))

    when:
    def batchIdDto = underTest.register(registerBatchCommand)

    then:
    batchIdDto.id() == batchId

    and:
    0 * modelService.getForSolving()
    0 * analysisService.create(_ as DefaultModel)
    0 * batchRepository.create(_ as Batch)
  }

  def 'should call updateStatusAndErrorDescription method with status error and error description when batch exists'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def batch = Batch.newOne(batchId, 'analysisName', 25L, 'batchMetadata')
    def errorDescription = 'error occurred'
    def batchMetadata = 'batchMetadata'
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(batchId, errorDescription, 'batchMetadata')
    def recommendationsStats = new RecommendationsStats(1, 2, 3, 4)
    def batchStatistics = new BatchStatistics(1, 2, 3, recommendationsStats)

    def batchError = BatchError.builder()
        .id(batchId)
        .errorDescription(errorDescription)
        .batchMetadata(batchMetadata)
        .statistics(batchStatistics)
        .build()

    and:
    batchRepository.findById(batchId) >> Optional.of(batch)

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchStatisticsService.createBatchErrorStatistics() >> batchStatistics
    1 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR, errorDescription)
    1 * eventPublisher.publish(batchError)
    0 * batchRepository.create(_ as Batch)
  }

  def 'should create new batch as error when batch does not exist'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def errorDescription = 'error occurred'
    def batchMetadata = 'batchMetadata'
    def notifyBatchErrorCommand = new NotifyBatchErrorCommand(batchId, errorDescription, 'batchMetadata')
    def recommendationsStats = new RecommendationsStats(1, 2, 3, 4)
    def batchStatistics = new BatchStatistics(1, 2, 3, recommendationsStats)
    def batchError = BatchError.builder()
        .id(batchId)
        .errorDescription(errorDescription)
        .batchMetadata(batchMetadata)
        .statistics(batchStatistics)
        .build()

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.notifyBatchError(notifyBatchErrorCommand)

    then:
    1 * batchStatisticsService.createBatchErrorStatistics() >> batchStatistics
    1 * batchRepository.create(_ as Batch) >> Batch.error(batchId, errorDescription, 'batchMetadata')
    1 * eventPublisher.publish(batchError)
    0 * batchRepository.updateStatusAndErrorDescription(batchId, BatchStatus.ERROR)
  }

  def 'should find batch by analysis name'() {
    given:
    def analysisName = 'analysisName'
    def batchId = new BatchId('batchId')

    when:
    def result = underTest.findBatchId(analysisName)

    then:
    1 * batchRepository.findBatchIdByAnalysisName(analysisName) >> Optional.of(batchId)
    result.id() == 'batchId'
  }

  def 'should throw NoSuchElementException when can not find a batch by analysis name'() {
    given:
    def analysisName = 'notExistingAnalysisName'

    when:
    underTest.findBatchId(analysisName)

    then:
    1 * batchRepository.findBatchIdByAnalysisName(analysisName) >> Optional.empty()
    thrown(NoSuchElementException.class)
  }

  def 'should update batch status as COMPLETED and publish message when batch exists'() {
    given:
    def batchId = UUID.randomUUID().toString()
    def analysisName = 'analysisName'
    def alertNames = ['firstAlertName', 'secondAlertName']
    def batchMetadata = 'batchMetadata'
    def batch = Batch.newOne(batchId, analysisName, 2L, batchMetadata)
    def command = new CompleteBatchCommand(batchId, alertNames)
    def recommendationsStats = new RecommendationsStats(1, 2, 3, 4)
    def batchStatistics = new BatchStatistics(1, 2, 3, recommendationsStats)
    def batchCompleted = BatchCompleted.builder()
        .id(batchId)
        .analysisId(analysisName)
        .alertIds(alertNames)
        .batchMetadata(batchMetadata)
        .statistics(batchStatistics)
        .build()

    and:
    batchRepository.findById(batchId) >> Optional.of(batch)

    when:
    underTest.completeBatch(command)

    then:
    1 * batchRepository.updateStatusToCompleted(command.id())
    1 * batchStatisticsService.createBatchCompletedStatistics(batchId, analysisName) >> batchStatistics
    1 * eventPublisher.publish(batchCompleted)
  }

  def 'should not update batch status as COMPLETED and publish message when batch does not exists'() {
    given:
    def batchId = 'notExistingBatchId'
    def command = new CompleteBatchCommand(batchId, [])

    and:
    batchRepository.findById(batchId) >> Optional.empty()

    when:
    underTest.completeBatch(command)

    then:
    0 * batchRepository.updateStatusToCompleted(_ as CompleteBatchCommand)
    0 * batchStatisticsService.createBatchCompletedStatistics(_ as String, _ as String)
    0 * eventPublisher.publish(_ as BatchCompleted)
  }
}
