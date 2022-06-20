/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.domain.GnsSyncService

import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.scope.context.StepContext
import org.springframework.batch.item.ExecutionContext
import spock.lang.Specification

class CreateNewGnsSyncTaskletSpec extends Specification {

  def syncService = Mock(GnsSyncService)
  def objectUnderTest = new CreateNewGnsSyncTasklet(syncService)

  def someStepContribution = Mock(StepContribution)

  def "should not execute new gns sync if gnsSyncMode is not set"() {
    given:
    def stepExecutionContext = new ExecutionContext()
    def stepExecution =  new StepExecution('test', new JobExecution(1L))
    stepExecution.setExecutionContext(stepExecutionContext)
    def chunkContext = new ChunkContext(
        new StepContext(stepExecution)
    )

    when:
    objectUnderTest.execute(someStepContribution, chunkContext)

    then:
    thrown(IllegalStateException)
    0 * _
  }
}
