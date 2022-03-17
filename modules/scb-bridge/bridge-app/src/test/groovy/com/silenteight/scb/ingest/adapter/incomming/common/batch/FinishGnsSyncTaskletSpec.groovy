package com.silenteight.scb.ingest.adapter.incomming.common.batch

import com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncService

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.scope.context.StepContext
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.repeat.RepeatStatus
import spock.lang.Specification

class FinishGnsSyncTaskletSpec extends Specification {

  def syncService = Mock(GnsSyncService)
  def someStepContribution = Mock(StepContribution)
  def objectUnderTest = new FinishGnsSyncTasklet(syncService)
  def someSyncMode = 'SOLVING_AL'

  def "should try to cleanup jobs that have the same mode"() {
    given:
    def jobExecution = new JobExecution(1L)
    jobExecution.setExecutionContext(new ExecutionContext([
        'GNS_SYNC_ID': 1
    ]))
    def stepExecution = new StepExecution('test', jobExecution)
    stepExecution.setExecutionContext(new ExecutionContext([
        'GNS_SYNC_MODE': someSyncMode
    ]))

    def chunkContext = new ChunkContext(
        new StepContext(stepExecution)
    )

    when:
    def result = objectUnderTest.execute(someStepContribution, chunkContext)

    then:
    result == RepeatStatus.FINISHED
    1 * syncService.finishSync(1L)
    1 * syncService.isRunningSync(someSyncMode) >> true
    1 * syncService.abandonSync(someSyncMode)
  }
}
