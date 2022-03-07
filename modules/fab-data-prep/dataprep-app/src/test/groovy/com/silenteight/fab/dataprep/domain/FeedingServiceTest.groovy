package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.Feature
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand

import spock.lang.Specification

class FeedingServiceTest extends Specification {

  def "should fail when no features were initialized"() {
    when:
    new FeedingService([])

    then:
    thrown(IllegalStateException)
  }

  def "should call all features"() {
    given:
    def features = [
        Mock(Feature),
        Mock(Feature)
    ]

    def featureService = new FeedingService(features)

    def command = FeatureInputsCommand.builder()
        .batchId('123')
        .build()

    when:
    featureService.createFeatureInputs(command)

    then:
    features.each {1 * it.createFeatureInput(command)}
  }
}
