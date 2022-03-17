package com.silenteight.scb.ingest.adapter.incomming.common.quartz

import org.quartz.JobDataMap
import org.quartz.JobDetail
import org.quartz.JobExecutionContext
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.configuration.JobLocator
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.JobOperator
import spock.lang.Specification

class QuartzBatchJobLauncherSpec extends Specification {

  def jobName = 'testJobName'
  def jobLocator = Mock(JobLocator)
  def jobLauncher = Mock(JobLauncher)
  def jobOperator = Mock(JobOperator)
  def someJob = Mock(Job)
  def someJobExecutionId = 1L

  def objectUnderTest

  def setup() {
    objectUnderTest = new QuartzBatchJobLauncher(
        jobName: jobName,
        jobLocator: jobLocator,
        jobLauncher: jobLauncher,
        jobOperator: jobOperator
    )
  }

  def "should execute job"() {
    given:
    def context = Mock(JobExecutionContext) {
      getJobDetail() >> Mock(JobDetail) {
        getJobDataMap() >> new JobDataMap()
      }
    }

    when:
    objectUnderTest.execute(context)

    then:
    1 * jobLocator.getJob(jobName) >> someJob
    1 * jobLauncher.run(someJob, _ as JobParameters)
    1 * jobOperator.getRunningExecutions(jobName) >> [someJobExecutionId]
    1 * jobOperator.stop(someJobExecutionId)
    1 * jobOperator.abandon(someJobExecutionId)
  }
}
