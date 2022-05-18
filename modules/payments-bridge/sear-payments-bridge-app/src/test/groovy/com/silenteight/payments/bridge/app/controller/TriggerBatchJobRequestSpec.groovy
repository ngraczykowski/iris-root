package com.silenteight.payments.bridge.app.controller

import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import spock.lang.Specification

class TriggerBatchJobRequestSpec extends Specification {

  def "When feature inputs are added"() {

    given:
    def request = new TriggerBatchJobRequest("kr-job", "funny.csv", List.of("custom1", "custom2"))
    when:
    def result = request.toJobParameters()
    then:
    noExceptionThrown()
    result == expectedParametersWithFeaturesInput()
  }


  def "When feature inputs are not added"() {

    given:
    def request = new TriggerBatchJobRequest("kr-job", "funny.csv", Collections.emptyList())
    when:
    def result = request.toJobParameters()
    then:
    noExceptionThrown()
    result == expectedParametersWithoutFeaturesInput("funny.csv")
  }


  private static JobParameters expectedParametersWithoutFeaturesInput(String fileName) {
    def map = [
        "file-name": buildJobParameter(fileName),
    ] as Map<String, JobParameter>

    new JobParameters(map)
  }

  private static JobParameters expectedParametersWithFeaturesInput() {
    def map = [
        "file-name"      : buildJobParameter("funny.csv"),
        "feature-custom1": buildJobParameter("custom1"),
        "feature-custom2": buildJobParameter("custom2")
    ] as Map<String, JobParameter>

    new JobParameters(map)
  }

  private static JobParameter buildJobParameter(String parameter) {
    new JobParameter(parameter, true)
  }
}
